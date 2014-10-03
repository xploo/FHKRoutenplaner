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
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import de.damianbuecker.fhkroutenplaner.activity.R;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;
import de.damianbuecker.fhkroutenplaner.model.Vertex;

/**
 * The Class ImageController.
 */
public class ImageController extends Controller {

	/** The tag dao. */
	private Dao<Tag, Integer> tagDao;

	/** The canvas. */
	private Canvas canvas;

	/** The my paint. */
	private Paint myPaint;

	/** The vertex hashmap. */
	private HashMap<Integer, LinkedList<Vertex>> vertexHashmap;

	/** The context. */
	private Context context;

	/** The m path controller. */
	private PathController mPathController;

	/** The end tag list. */
	private List<Tag> endTagList;

	/** The end floor. */
	private Integer endFloor;

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
	 *            the end id
	 * @return the end floor
	 */
	public Integer getEndFloor(Integer endID) {

		try {

			this.endTagList = this.getDatabaseHelper(this.getContext())
					.getTagById(endID.toString());
			if (endTagList.size() != 0) {
				this.endFloor = endTagList.get(0).getFloor();
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}

		return endFloor;

	}

	/**
	 * Test algorithm.
	 * 
	 * @param etage
	 *            the etage
	 * @param startID
	 *            the start id
	 * @param endID
	 *            the end id
	 */
	public void testAlgorithm(Integer etage, Integer startID, Integer endID) {

		Log.d("OnDraw", "ONDRAW CHECK");

		// Bitmap bitmapIn = this.getResourceForEtage(etage);
		// this.canvas = new Canvas(bitmapIn);

		this.myPaint = new Paint();
		this.myPaint.setAntiAlias(true);
		this.myPaint.setStyle(Paint.Style.FILL);
		this.myPaint.setColor(Color.GREEN);
		this.myPaint.setStrokeWidth(5.0f);

		// if (!bitmapIn.isMutable()) {
		// Abfangen lol
		// }

		this.mPathController = new PathController(context);
		LinkedList<Vertex> list = this.mPathController.testExcute(startID, endID);
		this.splitVertexList(list);

		if (this.vertexHashmap.size() > 0 && this.vertexHashmap.size() < 2) {
			this.saveFinalImage(this.vertexHashmap.get(0));
		} else if (this.vertexHashmap.size() > 0 && this.vertexHashmap.size() < 3) {
			this.saveFinalImage(this.vertexHashmap.get(0));
			this.saveFinalImage(this.vertexHashmap.get(1));
		}

	}

	/**
	 * Save final image.
	 * 
	 * @param list
	 *            the list
	 */
	private void saveFinalImage(LinkedList<Vertex> list) {

		if ((list != null) && (list.get(0) != null)) {
			Integer etage = list.get(0).getFloor();
			int ressourceId = this.getResourceForEtage(etage);
			Options options = new Options();
			options.inJustDecodeBounds = true;
			Bitmap b = BitmapFactory.decodeResource(this.getContext().getResources(), ressourceId,
					options);
			this.log("Width Options: " + options.outWidth);
			this.log("Height Options: " + options.outHeight);
			Bitmap.Config config = Config.ARGB_8888;
			options.inScaled = false;
			options.inJustDecodeBounds = false;
			Bitmap bitmapOut = Bitmap.createBitmap(options.outWidth, options.outHeight, config);
			b = BitmapFactory
					.decodeResource(this.getContext().getResources(), ressourceId, options);
			bitmapOut.setDensity(b.getDensity());
			this.log("Width Bitmap: " + b.getWidth());
			this.log("Height Bitmap: " + b.getHeight());
			for (int x = 0; x < options.outWidth; x++) {
				for (int y = 0; y < options.outHeight; y++) {
					int pixel = b.getPixel(x, y);
					bitmapOut.setPixel(x, y,
							Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
				}
			}

			b.recycle();
			b = null;

			this.canvas = new Canvas(bitmapOut);

			float bufferX = 0;
			float bufferY = 0;
			try {
				if (tagDao == null) {

					tagDao = this.getDatabaseHelper(this.getContext()).getTagDataDao();
				}
				QueryBuilder<Tag, Integer> queryBuilder = tagDao.queryBuilder();

				for (Vertex vertex : list) {

					queryBuilder.where().eq(Tag.DESCRIPTION, vertex);
					PreparedQuery<Tag> preparedQuery = queryBuilder.prepare();
					List<Tag> TagList = tagDao.query(preparedQuery);

					// Auf null prüfen
					canvas.drawCircle(Float.parseFloat(String.valueOf(TagList.get(0).getX_pos())),
							Float.parseFloat(String.valueOf(TagList.get(0).getY_pos())), 5.0f,
							myPaint);

					if (bufferX != 0 && bufferY != 0) {

						canvas.drawLine(bufferX, bufferY,
								Float.parseFloat(String.valueOf(TagList.get(0).getX_pos())),
								Float.parseFloat(String.valueOf(TagList.get(0).getY_pos())),
								myPaint);

					}
					bufferX = (float) TagList.get(0).getX_pos();
					bufferY = (float) TagList.get(0).getY_pos();

					Log.v("ViewImageVertex", vertex.toString());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			File folder = new File(Environment.getExternalStorageDirectory() + "/FMS/");
			if (!folder.exists())
				folder.mkdirs();

			try {
				File outputFile = new File(Environment.getExternalStorageDirectory()
						+ "/FMS/TestIMG-" + etage.hashCode() + ".png");
				FileOutputStream fos = new FileOutputStream(outputFile);
				bitmapOut.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				bitmapOut.recycle();
				bitmapOut = null;
				this.getContext()
						.sendBroadcast(
								new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
										.fromFile(outputFile)));

				Log.d("WRITE IMAGE", "CHECK");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Vertex liste aufteilen.
	 * 
	 * @param list
	 *            the list
	 */
	@SuppressLint("UseSparseArrays")
	private void splitVertexList(LinkedList<Vertex> list) {
		Integer startEtage = list.get(0).getFloor();
		Integer zielEtage = list.get(list.size() - 1).getFloor();
		LinkedList<Vertex> listeVertexStartEtage = new LinkedList<Vertex>();
		LinkedList<Vertex> listeVertexZielEtage = new LinkedList<Vertex>();

		for (Vertex v : list) {
			if (startEtage != zielEtage) {
				if ((startEtage == v.getFloor())) {
					listeVertexStartEtage.add(v);
				} else if (zielEtage == v.getFloor()) {
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
	 * Gets the resource for etage.
	 * 
	 * @param etage
	 *            the etage
	 * @return the resource for etage
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
			return R.drawable.cn_tower_grundriss6;
		} else if (etage == 7) {
			return R.drawable.cn_tower_grundriss7;
		} else if (etage == 8) {
			return R.drawable.cn_tower_grundriss8;
		}

		return null;
	}

}
