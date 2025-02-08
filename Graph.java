import java.util.ArrayList;

public class Graph {
    private int vertices;
    private int edges;
    private ArrayList<LinkedList<Integer>> adj;
    private ArrayList<Character> color;
    private ArrayList<Integer> distance;
    private ArrayList<Integer> parent;
    private ArrayList<Integer> discoverTime;
    private ArrayList<Integer> finishTime;
    private static int time = 0;

    /** Constructors and Destructors */

    /**
     * initializes an empty graph, with n vertices and 0 edges
     *
     * @param numVtx the number of vertices in the graph
     * @throws IllegalArgumentException when numVtx <= 0
     * @precondition numVtx > 0
     */
    public Graph(int numVtx) throws IllegalArgumentException {
        if (numVtx <= 0) {
            throw new IllegalArgumentException("Number of vertices must be greater than 0");
        }
        vertices = numVtx;
        edges = 0;

        adj = new ArrayList<>();
        color = new ArrayList<>();
        distance = new ArrayList<>();
        parent = new ArrayList<>();
        discoverTime = new ArrayList<>();
        finishTime = new ArrayList<>();

        for (int i = 0; i < numVtx; i++) {
            color.add('W');
            distance.add(-1);
            parent.add(0);
            discoverTime.add(-1);
            finishTime.add(-1);
            adj.add(new LinkedList<>());
        }
    }

    /*** Accessors ***/

    /**
     * Returns the number of edges in the graph
     *
     * @return the number of edges
     */
    public int getNumEdges() {
        return edges;
    }

    /**
     * Returns the number of vertices in the graph
     *
     * @return the number of vertices
     */
    public int getNumVertices() {
        return vertices;
    }

    /**
     * returns whether the graph is empty (no edges)
     *
     * @return whether the graph is empty
     */
    public boolean isEmpty() {
        return edges == 0;
    }

    /**
     * Returns the value of the distance[v]
     *
     * @param v a vertex in the graph
     * @return the distance of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Integer getDistance(Integer v) throws IndexOutOfBoundsException {
        return distance.get(v - 1);
    }

    /**
     * Returns the value of the parent[v]
     *
     * @param v a vertex in the graph
     * @return the parent of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Integer getParent(Integer v) throws IndexOutOfBoundsException {
        return parent.get(v - 1);
    }

    /**
     * Returns the value of the color[v]
     *
     * @param v a vertex in the graph
     * @return the color of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Character getColor(Integer v) throws IndexOutOfBoundsException {
        return color.get(v - 1);
    }

    /**
     * Returns the value of the discoverTime[v]
     *
     * @param v a vertex in the graph
     * @return the discover time of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Integer getDiscoverTime(Integer v) throws IndexOutOfBoundsException {
        return discoverTime.get(v - 1);
    }

    /**
     * Returns the value of the finishTime[v]
     *
     * @param v a vertex in the graph
     * @return the finish time of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public Integer getFinishTime(Integer v) throws IndexOutOfBoundsException {
        return finishTime.get(v - 1);
    }

    /**
     * Returns the LinkedList stored at index v
     *
     * @param v a vertex in the graph
     * @return the adjacency LinkedList at v
     * @throws IndexOutOfBoundsException when v is out of bounds
     * @precondition 0 < v <= vertices
     */
    public LinkedList<Integer> getAdjacencyList(Integer v)
            throws IndexOutOfBoundsException {
        return adj.get(v - 1);
    }

    /*** Manipulation Procedures ***/

    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u) @precondition, 0 < u, v <= vertices
     *
     * @param u a vertex in the graph
     * @param v a vertex in the graph
     * @throws IndexOutOfBoundsException exception when u or v is out of bounds
     */
    public void addDirectedEdge(Integer u, Integer v)
            throws IndexOutOfBoundsException {
        if (u < 1 || u > vertices || v < 1 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex indices are out of bounds");
        }

        adj.get(u - 1).addLast(v);
        edges++;
    }

    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u) and inserts u into the adjacent vertex list of v.
     *
     * @param u a vertex in the graph
     * @param v a vertex in the graph
     * @throws IndexOutOfBoundsException when u or v is out of bounds
     * @precondition, 0 < u, v <= vertices
     */
    public void addUndirectedEdge(Integer u, Integer v)
            throws IndexOutOfBoundsException {
        if (u < 1 || u > vertices || v < 1 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex indices are out of bounds");
        }

        adj.get(u - 1).addLast(v);
        adj.get(v - 1).addLast(u);
        edges++;
    }

    /*** Additional Operations ***/

    /**
     * Creates a String representation of the Graph Prints the adjacency list of
     * each vertex in the graph, vertex: <space separated list of adjacent
     * vertices>
     *
     * @return a space separated list of adjacent vertices
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= vertices; i++) {
            result.append(i).append(": ");
            LinkedList<Integer> adjList = adj.get(i - 1);
            adjList.positionIterator();
            for (int j = 0; j < adjList.getLength(); j++) {
                result.append(adjList.getIterator()).append(" ");
                adjList.advanceIterator();
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Performs breath first search on this Graph give a source vertex
     *
     * @param source the starting vertex
     * @throws IndexOutOfBoundsException when the source vertex is out of bounds
     *                                   of the graph
     * @precondition source is a vertex in the graph
     */
    public void BFS(Integer source) throws IndexOutOfBoundsException {
        if (source < 1 || source > vertices) {
            throw new IndexOutOfBoundsException("Source vertex is out of bounds");
        }

        for (int i = 0; i < vertices; i++) {
            distance.set(i, -1);
            parent.set(i, 0);
            color.set(i, 'W');
        }

        color.set(source - 1, 'G');
        distance.set(source - 1, 0);
        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(source);

        while (!queue.isEmpty()) {
            int current = queue.getFirst();
            queue.removeFirst();
            LinkedList<Integer> adjList = adj.get(current - 1);

            adjList.positionIterator();
            for (int i = 0; i < adjList.getLength(); i++) {
                int neighbor = adjList.getIterator();
                adjList.advanceIterator();

                if (color.get(neighbor - 1) == 'W') {
                    color.set(neighbor - 1, 'G');
                    distance.set(neighbor - 1, distance.get(current - 1) + 1);
                    parent.set(neighbor - 1, current);
                    queue.addLast(neighbor);
                }
            }

            color.set(current - 1, 'B');
        }
    }

    /**
     * Performs depth first search on this Graph in order of vertex lists
     */
    public void DFS() {
        color = new ArrayList<>(vertices);
        parent = new ArrayList<>(vertices);
        discoverTime = new ArrayList<>(vertices);
        finishTime = new ArrayList<>(vertices);
        time = 0;
        for (int i = 0; i < vertices; i++) {
            discoverTime.add(-1);
            finishTime.add(-1);
            color.add('W');
            parent.add(0);
        }

        for (int i = 1; i <= vertices; i++) {
            if (color.get(i - 1) == 'W') {
                visit(i);
            }
        }
    }

    /**
     * Private recursive helper method for DFS
     *
     * @param vertex the vertex to visit
     */
    private void visit(int vertex) {
        time = time + 1;
        discoverTime.set(vertex - 1, time);
        color.set(vertex - 1, 'G');

        LinkedList<Integer> adjacencyList = adj.get(vertex - 1);
        adjacencyList.positionIterator();
        for (int i = 0; i < adjacencyList.getLength(); i++) {
            int neighbor = adjacencyList.getIterator();
            adjacencyList.advanceIterator();

            if (color.get(neighbor - 1) == 'W') {
                parent.set(neighbor - 1, vertex);
                visit(neighbor);
            }
        }

        color.set(vertex - 1, 'B');
        time = time + 1;
        finishTime.set(vertex - 1, time);
    }
}
