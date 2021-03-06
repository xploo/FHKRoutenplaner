package de.damianbuecker.fhkroutenplaner.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import de.damianbuecker.fhkroutenplaner.activity.R;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;
import de.damianbuecker.fhkroutenplaner.model.Vertex;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageController.
 */
public class ImageController extends Controller {

	/** The Constant PNG. */
	private static final String PNG = ".png";

	/** The Constant PREFIXFILENAME. */
	private static final String PREFIXFILENAME = "TestIMG-";

	/** The tag dao. */
	private Dao<Tag, Integer> tagDao;

	/** The canvas. */
	private Canvas canvas;

	/** The my paint. */
	private Paint myPaint;

	/** The vertex hashmap. */
	private HashMap<Integer, LinkedList<Vertex>> vertexHashmap;

	/** The context. */
	private Context mContext;

	/** The m path controller. */
	private PathController mPathController;

	/** The end tag list. */
	private List<Tag> endTagList;

	/** The end floor. */
	private Integer endFloor;
	
	/** The start_ id. */
	private Integer start_ID;
	
	/**
	 * Gets the start_ id.
	 *
	 * @return the start_ id
	 */
	public Integer getStart_ID() {
		return start_ID;
	}

	/**
	 * Sets the start_ id.
	 *
	 * @param start_ID the new start_ id
	 */
	public void setStart_ID(Integer start_ID) {
		this.start_ID = start_ID;
	}

	/**
	 * Gets the end_ id.
	 *
	 * @return the end_ id
	 */
	public Integer getEnd_ID() {
		return end_ID;
	}

	/**
	 * Sets the end_ id.
	 *
	 * @param end_ID the new end_ id
	 */
	public void setEnd_ID(Integer end_ID) {
		this.end_ID = end_ID;
	}

	/** The end_ id. */
	private Integer end_ID;              

	/**
	 * Instantiates a new image controller.
	 * 
	 * @param context
	 *            the context
	 */
	public ImageController(Context context) {
		super(context);
	}

	/**
	 * Gets the end floor.
	 * 
	 * @param endID
	 *             end id of destination node
	 * @return the floor of the destination node
	 */	
	public Integer getEndFloor(Integer endID) {

		try {
			this.endTagList = this.getDatabaseHelper(this.getContext()).getTagById(endID.toString());
			if (this.endTagList.size() != 0) {
				this.endFloor = this.endTagList.get(0).getFloor();
			}
		} catch (SQLException e) {
			this.logMessage("ERROR", e.getMessage());
		}

		return endFloor;
	}

	/**
	 * Initiates the drawing of the navigation
	 * 
	 * @param startFloor
	 *             start floor
	 * @param startID
	 *             id of the start node
	 * @param endID
	 *             id of the destination node
	 * @param endFloor
	 *             end floor
	 */
	public void testAlgorithm(Integer startFloor, Integer startID, Integer endID, Integer endFloor) {		

		setEnd_ID(endID);
		setStart_ID(startID);	
		
		this.myPaint = new Paint();
		this.myPaint.setAntiAlias(true);
		this.myPaint.setStyle(Paint.Style.FILL);
		this.myPaint.setColor(Color.RED);
		this.myPaint.setStrokeWidth(5.0f);

		this.mPathController = new PathController(mContext);
		LinkedList<Vertex> list = this.mPathController.testExcute(startID, endID);
		this.splitVertexList(list, startFloor, endFloor);

		if (this.vertexHashmap.size() > 0 && this.vertexHashmap.size() < 2) {			
			this.saveFinalImage(this.vertexHashmap.get(0), startID);
		} else if (this.vertexHashmap.size() > 0 && this.vertexHashmap.size() < 3) {			
			this.saveFinalImage(this.vertexHashmap.get(0), startID); // anfang
			this.saveFinalImage(this.vertexHashmap.get(1), endID); // ende
		}

	}
	
	

	/**
	 * this methode draws the route of the navigation onto a Bitmap
	 * and saves the resulting picture to SDcard
	 * 
	 * 
	 * @param list
	 *             list of nodes which represent the path
	 * @param ID
	 *             id of the start or destination node
	 */
	private void saveFinalImage(LinkedList<Vertex> list, Integer ID) {

		if ((list != null) && (list.get(0) != null)) {
			Integer etage = list.get(0).getFloor();
			int ressourceId = this.getResourceForEtage(etage);
			Options options = new Options();
			options.inJustDecodeBounds = true;
			Bitmap b = BitmapFactory.decodeResource(this.getContext().getResources(), ressourceId, options);
			Bitmap.Config config = Config.ARGB_8888;
			options.inScaled = false;
			options.inJustDecodeBounds = false;
			Bitmap bitmapOut = Bitmap.createBitmap(options.outWidth, options.outHeight, config);
			b = BitmapFactory.decodeResource(this.getContext().getResources(), ressourceId, options);
			bitmapOut.setDensity(b.getDensity());
			for (int x = 0; x < options.outWidth; x++) {
				for (int y = 0; y < options.outHeight; y++) {
					int pixel = b.getPixel(x, y);
					bitmapOut.setPixel(x, y, Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
				}
			}

			b.recycle();
			b = null;

			this.canvas = new Canvas(bitmapOut);

			float bufferX = 0;
			float bufferY = 0;
			try {
				if (this.tagDao == null) {

					this.tagDao = this.getDatabaseHelper(this.getContext()).getTagDataDao();
				}
				QueryBuilder<Tag, Integer> queryBuilder = this.tagDao.queryBuilder();
				
				 Integer startID = getEnd_ID();
				 Integer endID = getStart_ID();
				
				
				for (Vertex vertex : list) {
					
					queryBuilder.where().eq(Tag.DESCRIPTION, vertex);
					PreparedQuery<Tag> preparedQuery = queryBuilder.prepare();
					List<Tag> tagList = this.tagDao.query(preparedQuery);
					
					if (tagList.get(0).getTag_id() == ID && ID == startID) {
						
						Bitmap mBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.ziel, options);
						canvas.drawBitmap(mBitmap, Float.parseFloat(String.valueOf(tagList.get(0).getX_pos())),
								Float.parseFloat(String.valueOf(tagList.get(0).getY_pos())) - 32, null);

					}
					if (tagList.get(0).getTag_id() == ID && ID == endID) {			

						Bitmap mBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.start, options);
						canvas.drawBitmap(mBitmap, Float.parseFloat(String.valueOf(tagList.get(0).getX_pos())),
								Float.parseFloat(String.valueOf(tagList.get(0).getY_pos())) - 32, null);
					}					

					canvas.drawCircle(Float.parseFloat(String.valueOf(tagList.get(0).getX_pos())),
							Float.parseFloat(String.valueOf(tagList.get(0).getY_pos())), 2.0f, this.myPaint);

					if (bufferX != 0 && bufferY != 0) {

						canvas.drawLine(bufferX, bufferY, Float.parseFloat(String.valueOf(tagList.get(0).getX_pos())),
								Float.parseFloat(String.valueOf(tagList.get(0).getY_pos())), this.myPaint);	
						
							Paint paint = new Paint();
							paint.setStyle(Paint.Style.FILL);
							paint.setColor(Color.RED);
							
							
						    float deltaX = Float.parseFloat(String.valueOf(tagList.get(0).getX_pos())) - bufferX;
						    float deltaY = Float.parseFloat(String.valueOf(tagList.get(0).getY_pos())) - bufferY;						  
						    float frac = (float) 0.1;
						    if(Math.abs(deltaX) >= 100)
						    {
						    	frac = (float) 0.03;
						    }
						    if(Math.abs(deltaY) >= 100){
						    	frac = (float) 0.03;
						    }
						    if(Math.abs(deltaX) <= 120 && Math.abs(deltaY)<= 30)
						    {
						    	frac = (float) 0.1;
						    }
						    if(Math.abs(deltaY) <= 120 && Math.abs(deltaX) <=30){
						    	frac = (float) 0.1;
						    }
						    if(Math.abs(deltaX) >= 40 && Math.abs(deltaX) <= 55 && Math.abs(deltaY) >= 40 && Math.abs(deltaY) <= 50){
						    	frac = (float) 0.15;
						    }
						    

						    float point_x_1 = Float.parseFloat(String.valueOf(tagList.get(0).getX_pos())) - (float) ((1 - frac) * deltaX + frac * deltaY);
						    float point_y_1 = Float.parseFloat(String.valueOf(tagList.get(0).getY_pos())) - (float) ((1 - frac) * deltaY - frac * deltaX);
						    
						    float point_x_2 = bufferX;
						    float point_y_2 = bufferY;
						    
						    float point_x_3 = Float.parseFloat(String.valueOf(tagList.get(0).getX_pos())) - (float) ((1 - frac) * deltaX - frac * deltaY);
						    float point_y_3 = Float.parseFloat(String.valueOf(tagList.get(0).getY_pos())) - (float) ((1 - frac) * deltaY + frac * deltaX);						    
						   						    						  
						    Path path = new Path();
						    path.setFillType(Path.FillType.EVEN_ODD);

						    path.moveTo(point_x_1, point_y_1);
						    path.lineTo(point_x_2, point_y_2);
						    path.lineTo(point_x_3, point_y_3);
						    path.lineTo(point_x_1, point_y_1);
						    path.lineTo(point_x_1, point_y_1);
						    path.close();

						    canvas.drawPath(path, paint);
					}

					bufferX = (float) tagList.get(0).getX_pos();
					bufferY = (float) tagList.get(0).getY_pos();
				}

			} catch (SQLException e) {
				this.logMessage("ERROR", e.getMessage());
			}
			File folder = new File(Environment.getExternalStorageDirectory() + this.getContext().getString(R.string.workingDirectory));
			if (!folder.exists())
				folder.mkdirs();

			try {
				File outputFile = new File(Environment.getExternalStorageDirectory() + this.getContext().getString(R.string.workingDirectory) + PREFIXFILENAME + etage.hashCode() + ID
						+ PNG);
				FileOutputStream fos = new FileOutputStream(outputFile);
				bitmapOut.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				bitmapOut.recycle();
				bitmapOut = null;
				this.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));				
			} catch (FileNotFoundException e) {
				this.logMessage("ERROR", e.getMessage());
			} catch (IOException e) {
				this.logMessage("ERROR", e.getMessage());
			}
		}
	}

	/**
	 * Splits the list of nodes into two separated list.
	 * 
	 * 
	 * @param list
	 *            list with calculated nodes
	 * @param startFloor
	 *             start floor
	 * @param endFloor
	 *             end floor
	 */
	@SuppressLint("UseSparseArrays")
	private void splitVertexList(LinkedList<Vertex> list, Integer startFloor, Integer endFloor) {
		LinkedList<Vertex> listeVertexStartEtage = new LinkedList<Vertex>();
		LinkedList<Vertex> listeVertexZielEtage = new LinkedList<Vertex>();

		for (Vertex v : list) {
			if (startFloor != endFloor) {
				if ((startFloor == v.getFloor())) {
					listeVertexStartEtage.add(v);					
				} else if (endFloor == v.getFloor()) {
					listeVertexZielEtage.add(v);					
				}
			} else {
				listeVertexStartEtage.add(v);
			}
		}
		this.vertexHashmap = new HashMap<Integer, LinkedList<Vertex>>();
		this.vertexHashmap.put(0, listeVertexStartEtage);
		if (listeVertexZielEtage.size() != 0) {
			this.vertexHashmap.put(1, listeVertexZielEtage);
		}
	}

	/**
	 * Gets the resource for Floor.
	 * 
	 * @param etage
	 *            Floor
	 * @return the resource id of the image 
	 */
	private Integer getResourceForEtage(Integer etage) {
		if (etage == 1) {
			return R.drawable.cn_tower_grundriss1;
		} else if (etage == 2) {
			return R.drawable.cn_tower_grundriss2;
		} else if (etage == 3) {
			return R.drawable.cn_tower_grundriss3;
		} else if (etage == 4) {
			return R.drawable.cn_tower_grundriss4;
		} else if (etage == 5) {
			return R.drawable.cn_tower_grundriss5;
		} else if (etage == 6) {
			return R.drawable.ebene6klein;
		} else if (etage == 7) {
			return R.drawable.ebene7klein;
		} else if (etage == 8) {
			return R.drawable.ebene8klein;
		} else {
			return null;
		}
	}
}
