package de.damianbuecker.fhkroutenplaner.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.algorithm.Dijkstra;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Edges;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;
import de.damianbuecker.fhkroutenplaner.model.Edge;
import de.damianbuecker.fhkroutenplaner.model.Graph;
import de.damianbuecker.fhkroutenplaner.model.Vertex;

public class PathController extends Controller {

	private DatabaseHelper databaseHelper;
	private Context context;
	private List<Edges> edgesStart;
	private List<Edges> edgesEnd;
	private List<Edges> edgesRemaining;
	
	public PathController(Context context) {
		super(context);
	}

	private static Integer TAGSIZE = 0;

	/*
	 * @Override protected void onCreate(Bundle savedInstanceState) { // TODO
	 * Auto-generated method stub super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_path);
	 * 
	 * this.databaseInteraction("onCreate");
	 * 
	 * testExcute();
	 * 
	 * }
	 * 
	 * 
	 * @Override protected void onDestroy() { super.onDestroy();
	 * 
	 * /** Disposes the DatabaseHelper.
	 * 
	 * if (this.databaseHelper != null) { OpenHelperManager.releaseHelper();
	 * this.databaseHelper = null; } }
	 */

	private List<Vertex> nodes;
	private List<Edge> edges;

	// Reinschreiben
	private void databaseInteraction(String action) {
		try {
			Dao<Tag, Integer> tagDataDao = this.getDatabaseHelper(this.getContext()).getTagDataDao();

			List<Tag> list = tagDataDao.queryForAll();
			StringBuilder sb = new StringBuilder();
			for (Tag s : list) {
				sb.append(s.getTag_id()).append(" ").append(s.getRoom_ID())
						.append(" ").append(s.getX_pos()).append(" ")
						.append(s.getY_pos()).append(" ")
						.append(s.getDescription()).append("\n");
			}

			// Log.v("Test String", sb.toString());
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public LinkedList<Vertex> testExcute(Integer startID, Integer endID) {
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// Datenbank auslesen for schleife solange wie Punkte sind
		try {
			Dao<Tag, Integer> TagListlooper = this.getDatabaseHelper(this.getContext()).getTagDataDao();

			for (Tag t : TagListlooper) {

				// in die location die Punkte schreiben
				Vertex location = new Vertex(String.valueOf(t.getTag_id()),
						t.getDescription(), t.getFloor());

				nodes.add(location);
				//Log.v("Valueof", String.valueOf(t.getTag_id()));

			}

			TAGSIZE = nodes.size() - 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			
			this.edgesStart = this.getDatabaseHelper(this.getContext()).getEdgesBySource(startID.toString());			
			System.out.println(edgesStart);
			StringBuilder sb = new StringBuilder();
			for (Edges s : edgesStart) {
				sb.append(s.getKante_id());
				addLane(sb.toString(), s.getSource(), s.getDestination(),
						s.getCost());
				Log.v("SCHNUBBI",sb.toString()+ " " + s.getSource() + " " + s.getDestination()+ " " +
						s.getCost());
			}
			
			this.edgesRemaining = this.getDatabaseHelper(this.getContext()).getRemainingEdges(startID.toString(), endID.toString());
			
			for(Edges v : edgesRemaining){
				addLane(String.valueOf(v.getKante_id()), v.getSource(), v.getDestination(), v.getCost());
				Log.v("SCHNUBBI",v.getKante_id() + " " + v.getSource() + " " + v.getDestination()+ " " +
						v.getCost());
			}
			
			this.edgesEnd = this.getDatabaseHelper(this.getContext()).getEdgesByDestination(endID.toString());
			for(Edges w : edgesEnd){
				addLane(String.valueOf(w.getKante_id()),w.getSource(),w.getDestination(),w.getCost());
				
				Log.v("SCHNUBBI", String.valueOf(w.getKante_id()) + " " + w.getSource() + " " + w.getDestination()+ " " +
						w.getCost());
			}
			
		} catch (SQLException e) {
			// tv.setText(e.toString());
			e.printStackTrace();
		}

		// Lets check from location Loc_1 to Loc_10
		Graph graph = new Graph(nodes, edges);
		Dijkstra dijkstra = new Dijkstra(graph);
		
		int nase = 0;
		int arsch = 0;
		for(Vertex v : nodes) {
			if(Integer.parseInt(v.getId()) == startID) {
				nase = nodes.indexOf(v);
			} else if(Integer.parseInt(v.getId()) == endID) {
				arsch = nodes.indexOf(v);
			}
		}
		dijkstra.execute(nodes.get(arsch));
		LinkedList<Vertex> path = dijkstra.getPath(nodes.get(nase));

		for (Vertex vertex : path) {
			//System.out.println(vertex);
			Log.v("ENDPATH", vertex.toString());
		}
		return path;
	}

	private void addLane(String laneId, int sourceLocNo, int destLocNo,
			int duration) {
		Integer sourceLocNoIndex = 0;
		Integer destLocNoIndex = 0;
		for(Vertex v : nodes) {
			if(Integer.parseInt(v.getId()) == sourceLocNo) {
				sourceLocNoIndex = nodes.indexOf(v);
			}
			else if(Integer.parseInt(v.getId()) == destLocNo) {
				destLocNoIndex = nodes.indexOf(v);
			}
		}
		Edge lane = new Edge(laneId, nodes.get(sourceLocNoIndex),
				nodes.get(destLocNoIndex), duration);
		edges.add(lane);
		//System.out.println(edges);
	}
}
