package de.damianbuecker.fhkroutenplaner.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.algorithm.Dijkstra;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Edges;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;
import de.damianbuecker.fhkroutenplaner.model.Edge;
import de.damianbuecker.fhkroutenplaner.model.Graph;
import de.damianbuecker.fhkroutenplaner.model.Vertex;

/**
 * The Class PathController.
 */
public class PathController extends Controller {

	/** The edges start. */
	private List<Edges> edgesStart;
	
	/** The edges end. */
	private List<Edges> edgesEnd;
	
	/** The edges remaining. */
	private List<Edges> edgesRemaining;

	/**
	 * Instantiates a new path controller.
	 *
	 * @param context the context
	 */
	public PathController(Context context) {
		super(context);
	}

	/** The tagsize. */
	@SuppressWarnings("unused")
	private static Integer TAGSIZE = 0;

	/** The nodes. */
	private List<Vertex> nodes;
	
	/** The edges. */
	private List<Edge> edges;

	/**
	 * Test excute.
	 *
	 * @param startID the start id
	 * @param endID the end id
	 * @return the linked list
	 */
	public LinkedList<Vertex> testExcute(Integer startID, Integer endID) {
		this.nodes = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();

		// Datenbank auslesen for schleife solange wie Punkte sind
		try {
			Dao<Tag, Integer> TagListlooper = this.getDatabaseHelper(this.getContext())
					.getTagDataDao();

			for (Tag t : TagListlooper) {

				// in die location die Punkte schreiben
				Vertex location = new Vertex(String.valueOf(t.getTag_id()), t.getDescription(),
						t.getFloor());

				this.nodes.add(location);
				// Log.v("Valueof", String.valueOf(t.getTag_id()));

			}

			TAGSIZE = this.nodes.size() - 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {

			this.edgesStart = this.getDatabaseHelper(this.getContext()).getEdgesBySource(
					startID.toString());
			StringBuilder sb = new StringBuilder();
			for (Edges s : edgesStart) {
				sb.append(s.getKante_id());
				addLane(sb.toString(), s.getSource(), s.getDestination(), s.getCost());
				this.logInfo("Edge: " + sb.toString() + " " + s.getSource() + " " + s.getDestination() + " " + s.getCost());
			}

			this.edgesRemaining = this.getDatabaseHelper(this.getContext()).getRemainingEdges(
					startID.toString(), endID.toString());

			for (Edges v : edgesRemaining) {
				addLane(String.valueOf(v.getKante_id()), v.getSource(), v.getDestination(),
						v.getCost());
				Log.v("SCHNUBBI", v.getKante_id() + " " + v.getSource() + " " + v.getDestination()
						+ " " + v.getCost());
			}

			this.edgesEnd = this.getDatabaseHelper(this.getContext()).getEdgesByDestination(
					endID.toString());
			for (Edges w : edgesEnd) {
				addLane(String.valueOf(w.getKante_id()), w.getSource(), w.getDestination(),
						w.getCost());

				Log.v("SCHNUBBI",
						String.valueOf(w.getKante_id()) + " " + w.getSource() + " "
								+ w.getDestination() + " " + w.getCost());
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
		for (Vertex v : nodes) {
			if (Integer.parseInt(v.getId()) == startID) {
				nase = nodes.indexOf(v);
			} else if (Integer.parseInt(v.getId()) == endID) {
				arsch = nodes.indexOf(v);
			}
		}
		dijkstra.execute(nodes.get(arsch));
		LinkedList<Vertex> path = dijkstra.getPath(nodes.get(nase));

		for (Vertex vertex : path) {
			// System.out.println(vertex);
			Log.v("ENDPATH", vertex.toString());
		}
		return path;
	}

	/**
	 * Adds the lane.
	 *
	 * @param laneId the lane id
	 * @param sourceLocNo the source loc no
	 * @param destLocNo the dest loc no
	 * @param duration the duration
	 */
	private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
		Integer sourceLocNoIndex = 0;
		Integer destLocNoIndex = 0;
		for (Vertex v : nodes) {
			if (Integer.parseInt(v.getId()) == sourceLocNo) {
				sourceLocNoIndex = nodes.indexOf(v);
			} else if (Integer.parseInt(v.getId()) == destLocNo) {
				destLocNoIndex = nodes.indexOf(v);
			}
		}
		Edge lane = new Edge(laneId, nodes.get(sourceLocNoIndex), nodes.get(destLocNoIndex),
				duration);
		edges.add(lane);
	}
}
