/**
 * This program provides a graphical interface for visualizing and optimizing network topologies.
 * The application allows the user to add nodes and edges to the graph and perform two main operations:
 * 1. Compute the Minimum Spanning Tree (MST) to minimize the total cost of connecting all nodes.
 * 2. Compute the Shortest Path between two nodes based on bandwidth, with the bandwidth inversely represented as weight.
 * The graph is rendered in a graphical user interface (GUI) using Java Swing. 
 * Users can interact with the graph to add nodes, create edges, and trigger the calculations of MST and shortest paths.
 * 
 * Key Features:
 * - Add nodes to the graph.
 * - Add edges with cost and bandwidth information.
 * - Calculate and display the Minimum Spanning Tree (MST).
 * - Calculate and display the Shortest Path between two selected nodes based on bandwidth.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Question_5 extends JFrame {
    private GraphPanel graphPanel;
    private JLabel costLabel, latencyLabel;

    /**
     * Constructor to set up the window, initialize components, and define layout.
     * The components include buttons for adding nodes, adding edges, finding the MST, and finding the shortest path.
     */
    public Question_5() {
        setTitle("Network Topology Optimizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    /**
     * Initializes the UI components including the control panel and action listeners for buttons.
     * Buttons for adding nodes, adding edges, finding MST, and finding the shortest path are set up here.
     */
    private void initComponents() {
        graphPanel = new GraphPanel();
        JPanel controlPanel = new JPanel(new GridLayout(0, 1));

        JButton addNodeBtn = new JButton("Add Node");
        JButton addEdgeBtn = new JButton("Add Edge");
        JButton mstBtn = new JButton("Find MST (Cost)");
        JButton shortestPathBtn = new JButton("Find Shortest Path (Bandwidth)");

        costLabel = new JLabel("Total Cost: 0");
        latencyLabel = new JLabel("Latency: N/A");

        controlPanel.add(addNodeBtn);
        controlPanel.add(addEdgeBtn);
        controlPanel.add(mstBtn);
        controlPanel.add(shortestPathBtn);
        controlPanel.add(costLabel);
        controlPanel.add(latencyLabel);

        add(graphPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        // Button actions
        addNodeBtn.addActionListener(e -> graphPanel.setMode(GraphPanel.Mode.ADD_NODE));
        addEdgeBtn.addActionListener(e -> graphPanel.setMode(GraphPanel.Mode.ADD_EDGE));
        mstBtn.addActionListener(e -> {
            graphPanel.findMST();
            updateStats();
        });
        shortestPathBtn.addActionListener(e -> graphPanel.startShortestPathSelection());
    }

    /**
     * Updates the total cost label after calculating the MST.
     * This method calls the `calculateTotalCost` method from `GraphModel` to get the total cost of the MST.
     */
    private void updateStats() {
        int totalCost = graphPanel.getGraphModel().calculateTotalCost();
        costLabel.setText("Total Cost: " + totalCost);
    }

    /**
     * Main method to run the application.
     * It initializes the frame and makes the application visible.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Question_5().setVisible(true));
    }
}

/**
 * GraphPanel is a custom JPanel class that handles the graphical representation of nodes and edges.
 * It provides interaction functionalities such as adding nodes, adding edges, selecting paths, and visualizing MST and shortest paths.
 */
class GraphPanel extends JPanel {
    enum Mode { ADD_NODE, ADD_EDGE, SELECT_PATH }
    private Mode currentMode = Mode.ADD_NODE;
    private GraphModel graphModel = new GraphModel();
    private GraphModel.GraphNode selectedNode;
    private GraphModel.GraphNode pathStartNode;

    /**
     * Constructor to initialize the GraphPanel and set up mouse event listeners.
     * The mouse listener handles the addition of nodes, edges, and path selections.
     */
    public GraphPanel() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    /**
     * Handles mouse click events based on the current mode (add node, add edge, select path).
     * Depending on the selected mode, this method will either add a node, create an edge between nodes, or select a path.
     */
    private void handleClick(int x, int y) {
        switch (currentMode) {
            case ADD_NODE:
                graphModel.addNode(new GraphModel.GraphNode(x, y));
                repaint();
                break;
            case ADD_EDGE:
                GraphModel.GraphNode node = graphModel.getNodeAt(x, y);
                if (node != null) {
                    if (selectedNode == null) {
                        selectedNode = node;
                    } else {
                        try {
                            String costStr = JOptionPane.showInputDialog("Enter cost:");
                            String bwStr = JOptionPane.showInputDialog("Enter bandwidth:");
                            int cost = Integer.parseInt(costStr);
                            int bandwidth = Integer.parseInt(bwStr);
                            graphModel.addEdge(selectedNode, node, cost, bandwidth);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid number format!");
                        }
                        selectedNode = null;
                        repaint();
                    }
                }
                break;
            case SELECT_PATH:
                GraphModel.GraphNode clicked = graphModel.getNodeAt(x, y);
                if (clicked != null) {
                    if (pathStartNode == null) {
                        pathStartNode = clicked;
                    } else {
                        graphModel.computeShortestPath(pathStartNode, clicked);
                        pathStartNode = null;
                        repaint();
                    }
                }
                break;
        }
    }

    /**
     * Starts the shortest path selection mode. This mode allows users to select two nodes to find the shortest path.
     */
    public void startShortestPathSelection() {
        setMode(Mode.SELECT_PATH);
        pathStartNode = null;
    }

    /**
     * Finds the Minimum Spanning Tree (MST) for the graph by calling the `computeMST` method of `GraphModel`.
     * The edges selected in the MST are highlighted in red.
     */
    public void findMST() {
        graphModel.computeMST();
        repaint();
    }

    /**
     * Returns the GraphModel object associated with this panel.
     * This allows access to the model for operations such as calculating MST and shortest paths.
     */
    public GraphModel getGraphModel() { return graphModel; }

    /**
     * Paints the graph on the panel. This method is called whenever the graph needs to be redrawn.
     * It calls the `draw` method of `GraphModel` to display the nodes, edges, MST, and shortest path.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphModel.draw(g);
    }

    /**
     * Sets the current mode of interaction in the GraphPanel (e.g., adding nodes, adding edges, or selecting paths).
     */
    public void setMode(Mode mode) { currentMode = mode; }
}

/**
 * GraphModel manages the underlying graph structure, including nodes, edges, and algorithms like MST and shortest path.
 * It provides methods for adding nodes, edges, computing MST, computing the shortest path, and drawing the graph.
 */
class GraphModel {
    private List<GraphNode> nodes = new ArrayList<>();
    private List<GraphEdge> edges = new ArrayList<>();
    private List<GraphEdge> mstEdges = new ArrayList<>();
    private List<GraphEdge> shortestPath = new ArrayList<>();

    static class GraphNode {
        public int x, y;
        public GraphNode(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void draw(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillOval(x-10, y-10, 20, 20);
        }
    }

    static class GraphEdge {
        public GraphNode from, to;
        public int cost, bandwidth;
        
        public GraphEdge(GraphNode from, GraphNode to, int cost, int bandwidth) {
            this.from = from;
            this.to = to;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
        
        public void draw(Graphics g, boolean highlight) {
            g.setColor(highlight ? g.getColor() : Color.BLACK);
            g.drawLine(from.x, from.y, to.x, to.y);
            g.drawString(cost + "/" + bandwidth, (from.x + to.x)/2, (from.y + to.y)/2);
        }
    }

    /**
     * Adds a node to the graph model.
     */
    public void addNode(GraphNode node) { 
        nodes.add(node); 
    }

    /**
     * Adds an edge between two nodes with specified cost and bandwidth to the graph model.
     */
    public void addEdge(GraphNode from, GraphNode to, int cost, int bandwidth) {
        edges.add(new GraphEdge(from, to, cost, bandwidth));
    }

    /**
     * Returns the node located at the specified (x, y) coordinates. It checks proximity to determine which node is clicked.
     */
    public GraphNode getNodeAt(int x, int y) {
        for (GraphNode node : nodes) {
            if (Math.hypot(node.x - x, node.y - y) < 15) {
                return node;
            }
        }
        return null;
    }

    /**
     * Computes the Minimum Spanning Tree (MST) using Kruskal's algorithm. It sorts the edges by cost and uses Disjoint Set Union (DSU) to find the MST.
     */
    public void computeMST() {
        mstEdges.clear();
        List<GraphEdge> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort(Comparator.comparingInt(e -> e.cost));
        DisjointSetUnion dsu = new DisjointSetUnion(nodes.size());

        for (GraphEdge edge : sortedEdges) {
            int u = nodes.indexOf(edge.from);
            int v = nodes.indexOf(edge.to);
            if (dsu.find(u) != dsu.find(v)) {
                mstEdges.add(edge);
                dsu.union(u, v);
            }
        }
    }

    /**
     * Computes the shortest path from the start node to the end node based on bandwidth.
     * This uses Dijkstra’s algorithm with bandwidth as the inverse weight.
     */
    public void computeShortestPath(GraphNode start, GraphNode end) {
        Map<GraphNode, Integer> dist = new HashMap<>();
        Map<GraphNode, GraphNode> prev = new HashMap<>();
        PriorityQueue<GraphNode> queue = new PriorityQueue<>( 
            Comparator.comparingInt(n -> dist.getOrDefault(n, Integer.MAX_VALUE))
        );

        for (GraphNode node : nodes) {
            dist.put(node, Integer.MAX_VALUE);
            prev.put(node, null);
        }
        dist.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            GraphNode u = queue.poll();
            if (u == end) break;

            for (GraphEdge edge : getAdjacentEdges(u)) {
                GraphNode v = (edge.from == u) ? edge.to : edge.from;
                int weight = 1000 / edge.bandwidth;
                int alt = dist.get(u) + weight;
                
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    if (!queue.contains(v)) {
                        queue.add(v);
                    }
                }
            }
        }

        shortestPath.clear();
        GraphNode current = end;
        while (prev.get(current) != null) {
            GraphNode predecessor = prev.get(current);
            GraphEdge edge = getEdgeBetween(predecessor, current);
            if (edge != null) {
                shortestPath.add(edge);
                current = predecessor;
            } else {
                break;
            }
        }
        Collections.reverse(shortestPath);
    }

    /**
     * Returns the list of edges adjacent to a specific node.
     */
    private List<GraphEdge> getAdjacentEdges(GraphNode node) {
        List<GraphEdge> adjacent = new ArrayList<>();
        for (GraphEdge edge : edges) {
            if (edge.from == node || edge.to == node) {
                adjacent.add(edge);
            }
        }
        return adjacent;
    }

    /**
     * Returns the edge between two nodes, if it exists.
     */
    private GraphEdge getEdgeBetween(GraphNode a, GraphNode b) {
        for (GraphEdge edge : edges) {
            if ((edge.from == a && edge.to == b) || (edge.from == b && edge.to == a)) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Calculates the total cost of the Minimum Spanning Tree (MST) by summing up the costs of the MST edges.
     */
    public int calculateTotalCost() {
        return mstEdges.stream().mapToInt(e -> e.cost).sum();
    }

    /**
     * Draws all nodes and edges on the graph. The edges are drawn in different colors for MST and shortest paths.
     */
    public void draw(Graphics g) {
        for (GraphEdge edge : edges) edge.draw(g, false);
        g.setColor(Color.RED);
        for (GraphEdge edge : mstEdges) edge.draw(g, true);
        g.setColor(Color.GREEN);
        for (GraphEdge edge : shortestPath) edge.draw(g, true);
        for (GraphNode node : nodes) node.draw(g);
    }

    /**
     * Disjoint Set Union (DSU) data structure for efficiently performing union and find operations.
     * It is used in the MST calculation to avoid cycles while adding edges.
     */
    static class DisjointSetUnion {
        int[] parent;
        public DisjointSetUnion(int size) { 
            parent = new int[size]; 
            Arrays.setAll(parent, i -> i); 
        }
        public int find(int x) { 
            return parent[x] == x ? x : (parent[x] = find(parent[x])); 
        }
        public void union(int x, int y) { 
            parent[find(x)] = find(y); 
        }
    }
}
