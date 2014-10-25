package de.damianbuecker.fhkroutenplaner.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.algorithm.Dijkstra;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Edges;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;
import de.damianbuecker.fhkroutenplaner.model.Edge;
import de.damianbuecker.fhkroutenplaner.model.Graph;
import de.damianbuecker.fhkroutenplaner.model.Vertex;

// TODO: Auto-generated Javadoc
/**
 * The Class PathController.
 */
public class PathController extends Controller {

	/** The edges start. */
	private List<Edges> edgesStart;

	/** The edges end. */
	private List<Edges> edgesEnd;

	/** The dao edge. */
	private Dao<Edges, Integer> daoEdge;

	/** The edges remaining. */
	private List<Edges> edgesRemaining;

	/** The tagsize. */
	@SuppressWarnings("unused")
	private static Integer TAGSIZE = 0;

	/** The nodes. */
	private List<Vertex> nodes;

	/** The edges. */
	private List<Edge> edges;

	/**
	 * Instantiates a new path controller.
	 * 
	 * @param context
	 *            the context
	 */
	public PathController(Context context) {
		super(context);
	}

	/**
	 * the method provides all nodes and edges to initiate a new navigation 
	 * 
	 * @param startID
	 *            the starting node
	 * @param endID
	 *            the end node
	 * @return the calculated path
	 */
	public LinkedList<Vertex> testExcute(Integer startID, Integer endID) {
		this.nodes = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();

		try {
			Dao<Tag, Integer> TagListlooper = this.getDatabaseHelper(this.getContext()).getTagDataDao();

			for (Tag t : TagListlooper) {
				Vertex location = new Vertex(String.valueOf(t.getTag_id()), t.getDescription(), t.getFloor());
				this.nodes.add(location);
			}

			TAGSIZE = this.nodes.size() - 1;
		} catch (SQLException e) {
			this.logMessage("ERROR", e.toString());
		}

		try {

			this.daoEdge = this.getDatabaseHelper(this.getContext()).getEdgesDataDao();
			for (Edges s : daoEdge) {

				addLane(String.valueOf(s.getKante_id()), s.getSource(), s.getDestination(), s.getCost());
			}
		} catch (SQLException e) {
			this.logMessage("ERROR", e.toString());
		}

		Graph graph = new Graph(nodes, edges);
		Dijkstra dijkstra = new Dijkstra(graph);

		int checkStart = 0;
		int checkEnd = 0;
		for (Vertex v : nodes) {
			if (Integer.parseInt(v.getId()) == startID) {
				checkStart = nodes.indexOf(v);
			} else if (Integer.parseInt(v.getId()) == endID) {
				checkEnd = nodes.indexOf(v);
			}
		}

		dijkstra.execute(nodes.get(checkEnd));
		LinkedList<Vertex> path = dijkstra.getPath(nodes.get(checkStart));
		return path;
	}

	/**
	 * defines all edges between nodes
	 * 
	 * @param laneId
	 *            the lane id
	 * @param sourceLocNo
	 *            the source node no
	 * @param destLocNo
	 *            the destination node no
	 * @param duration
	 *            the cost between two nodes
	 */
	private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
		Integer sourceLocNoIndex, destLocNoIndex;
		sourceLocNoIndex = destLocNoIndex = 0;

		for (Vertex v : nodes) {
			if (Integer.parseInt(v.getId()) == sourceLocNo) {
				sourceLocNoIndex = this.nodes.indexOf(v);
			} else if (Integer.parseInt(v.getId()) == destLocNo) {
				destLocNoIndex = this.nodes.indexOf(v);
			}
		}
		Edge lane = new Edge(laneId, this.nodes.get(sourceLocNoIndex), this.nodes.get(destLocNoIndex), duration);
		this.edges.add(lane);
	}
}
