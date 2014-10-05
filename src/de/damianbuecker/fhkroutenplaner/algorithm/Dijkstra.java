package de.damianbuecker.fhkroutenplaner.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.damianbuecker.fhkroutenplaner.model.Edge;
import de.damianbuecker.fhkroutenplaner.model.Graph;
import de.damianbuecker.fhkroutenplaner.model.Vertex;



/**
 * The Class Dijkstra.
 */
public class Dijkstra {

  /** The nodes. */
  @SuppressWarnings("unused")
  private final List<Vertex> nodes;
  
  /** The edges. */
  private final List<Edge> edges;
  
  /** The settled nodes. */
  private Set<Vertex> settledNodes;
  
  /** The un settled nodes. */
  private Set<Vertex> unSettledNodes;
  
  /** The predecessors. */
  private Map<Vertex, Vertex> predecessors;
  
  /** The distance. */
  private Map<Vertex, Integer> distance;

  /**
   * Instantiates a new dijkstra.
   *
   * @param graph the graph
   */
  public Dijkstra(Graph graph) {
    // create a copy of the array so that we can operate on this array
    this.nodes = new ArrayList<Vertex>(graph.getVertexes());
    this.edges = new ArrayList<Edge>(graph.getEdges());
  }

  /**
   * Execute.
   *
   * @param source the source
   */
  public void execute(Vertex source) {
    settledNodes = new HashSet<Vertex>();
    unSettledNodes = new HashSet<Vertex>();
    distance = new HashMap<Vertex, Integer>();
    predecessors = new HashMap<Vertex, Vertex>();
    distance.put(source, 0);
    unSettledNodes.add(source);
    while (unSettledNodes.size() > 0) {
      Vertex node = getMinimum(unSettledNodes);
      settledNodes.add(node);
      unSettledNodes.remove(node);
      findMinimalDistances(node);
    }
  }

  /**
   * Find minimal distances.
   *
   * @param node the node
   */
  private void findMinimalDistances(Vertex node) {
    List<Vertex> adjacentNodes = getNeighbors(node);
    for (Vertex target : adjacentNodes) {
      if (getShortestDistance(target) > getShortestDistance(node)
          + getDistance(node, target)) {
        distance.put(target, getShortestDistance(node)
            + getDistance(node, target));
        predecessors.put(target, node);
        unSettledNodes.add(target);
      }
    }

  }

  /**
   * Gets the distance.
   *
   * @param node the node
   * @param target the target
   * @return the distance
   */
  private int getDistance(Vertex node, Vertex target) {
    for (Edge edge : edges) {
      if (edge.getSource().equals(node)
          && edge.getDestination().equals(target)) {
        return edge.getWeight();
      }
    }
    throw new RuntimeException("Should not happen");
  }

  /**
   * Gets the neighbors.
   *
   * @param node the node
   * @return the neighbors
   */
  private List<Vertex> getNeighbors(Vertex node) {
    List<Vertex> neighbors = new ArrayList<Vertex>();
    for (Edge edge : edges) {
      if (edge.getSource().equals(node)
          && !isSettled(edge.getDestination())) {
        neighbors.add(edge.getDestination());
      }
    }
    return neighbors;
  }

  /**
   * Gets the minimum.
   *
   * @param vertexes the vertexes
   * @return the minimum
   */
  private Vertex getMinimum(Set<Vertex> vertexes) {
    Vertex minimum = null;
    for (Vertex vertex : vertexes) {
      if (minimum == null) {
        minimum = vertex;
      } else {
        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
          minimum = vertex;
        }
      }
    }
    return minimum;
  }

  /**
   * Checks if is settled.
   *
   * @param vertex the vertex
   * @return true, if is settled
   */
  private boolean isSettled(Vertex vertex) {
    return settledNodes.contains(vertex);
  }

  /**
   * Gets the shortest distance.
   *
   * @param destination the destination
   * @return the shortest distance
   */
  private int getShortestDistance(Vertex destination) {
    Integer d = distance.get(destination);
    if (d == null) {
      return Integer.MAX_VALUE;
    } else {
      return d;
    }
  }

  /*
   * This method returns the path from the source to the selected target and
   * NULL if no path exists
   */
  /**
   * Gets the path.
   *
   * @param target the target
   * @return the path
   */
  public LinkedList<Vertex> getPath(Vertex target) {
    LinkedList<Vertex> path = new LinkedList<Vertex>();
    Vertex step = target;
    // check if a path exists
    if (predecessors.get(step) == null) {
      return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
      step = predecessors.get(step);
      path.add(step);
    }
    // Put it into the correct order
    Collections.reverse(path);
    return path;
  }

} 