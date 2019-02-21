// Jonty Shah
// CSE 373 AD 
// TA: Chloe Lathe
// Homework 5
// 05/19/17

// This is a representation of a graph and assumes that
// it does not contain negative edge cost.

import java.util.*;

public class MyGraph implements Graph {

   private Map<Vertex, HashSet<Edge>> graph;
   private Set<Edge> edge; 

   // This is a constructor that takes in a Collection of vertices and edges.
   // It throws IllegalArgumentException if the passed in parameter is null.
   // It also initializes the field in the class.
   // It also adds the vertices and edges to the graph.
   // It also calls a private helper method to add edges in the graph.
   public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
      if(v == null || e == null) {
         throw new IllegalArgumentException();
      } 
      
      graph = new HashMap<Vertex, HashSet<Edge>>();
      edge = new  HashSet<Edge>();
      
      for(Vertex ver : v) {
         if(!graph.containsKey(ver)){
            graph.put(ver, new HashSet<Edge>());
         }
      }
      addEdge(e);
   }
   
   // This is a private helper method. It takes in a collection of edges
   // as a parameter and throws IllegalArgumentException if the graph does not
   // contain the source or destination vertex or the weight of the edge is negative.
   // The method also throws an IllegalArgumentException if the edge is not valid.
   private void addEdge(Collection<Edge> e) {
      for(Edge ed: e) {
         if(!graph.containsKey(ed.getDestination()) || !graph.containsKey(ed.getSource()) 
         || ed.getWeight() < 0 ) {
            throw new IllegalArgumentException();
         }
         
         for(Edge check: graph.get(ed.getSource())) {
            if(check.getDestination().equals(ed.getDestination()) && 
            check.getWeight() != ed.getWeight()) {
               throw new IllegalArgumentException();
            }  
         }        
         edge.add(ed);
         graph.get(ed.getSource()).add(ed);
      }      
   }

   // This method returns a collection of vertices of the graph.
   public Collection<Vertex> vertices() {
      return graph.keySet();
   }

   // This method returns a collection of edges of the graph.
   public Collection<Edge> edges() {
      return edge;
   }
   
   // This method takes in a Vertex as a parameter and throws an IllegalArgumentException
   // if the passsed in vertex is null or is not present in the graph. The method returns
   // a collection of vertices adjacent to the passed in vertex. It returns an empty collection
   // if no adjacent vertices are present.
   public Collection<Vertex> adjacentVertices(Vertex v) {
      if(v == null || !graph.containsKey(v)) {
         throw new IllegalArgumentException();
      }
      
      Set<Vertex> result = new HashSet<Vertex>();
      
      for(Edge ed: graph.get(v)){
         result.add(ed.getDestination());
      }
      
      return result;
   }

   // This method takes in two vertices as a parameter and throws an IllegalArgumentException
   // if the vertices are null or not present in the graph. The method returns the edge weight if
   // there exist a directed edge from vertex a to vertex b. If does not exist then it returns -1. 
   public int edgeCost(Vertex a, Vertex b) {
      if(a == null || b == null || !graph.containsKey(a) || !graph.containsKey(b)) {
         throw new IllegalArgumentException();
      }
      
      for(Edge ed: graph.get(a)){
         if(ed.getDestination().equals(b)) {
            return ed.getWeight();
         }
      }
      return -1;
   }
   
   // This method returns the shortest path from the vertices given as the parameter
   // The method assumes that all the edge weight are non-negative.It throws Illegal
   // ArgumentException if the passed in vertex is null or does not exist in the 
   // graph. The method returns null if no path is possible between the passed 
   // vertices.
   public Path shortestPath(Vertex a, Vertex b) {
      if(a == null || b == null || !graph.containsKey(a) || !graph.containsKey(b)){
         throw new IllegalArgumentException();
      }
   
      List<Vertex> vertices = new LinkedList<Vertex>();
      if(a.equals(b)){
         vertices.add(a);
         return new Path(vertices, 0);
      }
      for(Vertex vert: graph.keySet()){
         if(vert.equals(b)){
            b = vert;
         } else if(vert.equals(a)){
            a = vert;
         }
         vert.cost = Integer.MAX_VALUE;
         vert.visited = false;
         vert.path = null;
      } 
      return ShortestPathHelper(b, a, vertices);
   }
    
   // This is private method and returns the shortest path possible between two vertices
   // and uses Dijkstra's algorithm to caluclate the shortest path.
   private Path ShortestPathHelper(Vertex b, Vertex a, List<Vertex> vertices){
      Queue<Vertex> process = new LinkedList<Vertex>();
      a.cost = 0;
      b.visited = true;
      process.add(a);
      while(!process.isEmpty()){
         Vertex temp = process.poll();
         for(Edge ed: graph.get(temp)){
            Vertex destination = find(ed.getDestination());
            Vertex source = find(ed.getSource());

            if(!destination.visited){
               destination.visited = true;
               process.add(destination);
            }

            int cost1 = source.cost + ed.getWeight();
            int cost2 = destination.cost;
            if(cost1 < cost2){
               destination.cost = cost1;
               destination.path = source;
            }   
         }
      }
      if(b.visited){
         Vertex ver = b;
         while(ver != null){
            vertices.add(ver);
            ver = ver.path;
         }
         Collections.reverse(vertices); // reverses the order of the list
         return new Path(vertices, b.cost);
      }
      return null;
  }
  
  // This private method returns a Vertex and takes in a vertex as
  // parameter. This method finds the given vertex in the graph and 
  // returns it.
   private Vertex find(Vertex x){
      for(Vertex y: graph.keySet()){
         if(y.getLabel().equals(x.getLabel())){
            return y;
         }
      }
      return x;
   }
}

