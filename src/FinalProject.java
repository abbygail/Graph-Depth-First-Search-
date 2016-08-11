
import java.util.Scanner;

class graph {

    //fields: array of vertex values, list of edges between vertices, and integer value of first invalid index in vertex array
    private vertex vertices[];
    private Edge edges;
    private int lastIndex;

    //initializes vertex and edge arrays
    public graph(int n) {
        vertices = new vertex[n];
        edges = new Edge(n);
    }

    //method adds next vertex of value n at index nextIndex
    public void addVertex(int n) {
        
        //check to see if graph is full - if so, prints statement and returns.
        if (lastIndex == vertices.length) {
            System.out.println("Graph is full, cannot add more vertices.");
            return;
        }
        vertices[lastIndex] = new vertex(n);
        lastIndex++;
    }

    //finds array index of vertex value
    public int findIndex(int n) {
        for (int i = 0; i < lastIndex; i++) {
            if (vertices[i].vert == n) {
                return i;
            }
        }
        System.out.println("Vertex not found in graph.");
        return lastIndex;
    }

    //takes vertex values as input, adds edge between vertices
    public void joinVerts(int a, int b) {
        int from, to;
        from = findIndex(a);
        to = findIndex(b);
        if (from == lastIndex || to == lastIndex) //check to make sure vertices were found
        {
            System.out.println("Seleted vertices do not exist in this graph.");
            return;
        }
        edges.addEdge(from, to);
    }

    //finds next vertex in the map process
    public int findNextVert(int currIndex) {
        vertices[currIndex].changeFlag(false);
        return edges.findConnection(currIndex);
    }

    //returns how many vertices it's connected to
    public int howManyVerts() {
        return lastIndex;
    }

    //returns the value of the vertex at index n
    public int vertexValue(int n) {
        return vertices[n].vert;
    }

    //checks if vertex at index n is connected to other vertices
    public boolean connected(int n) {
        return edges.hasConnects(n);
    }

    //checks to see if vertex has been found before  
    public boolean beenFound(int n) {
        return vertices[n].flag;
    }

    //displays DSF
    public void display() {
        edges.display(vertices);
    }

    //checks to see if graph has any unexplored vertices
    public int check() {
        for (int i = 0; i < lastIndex; i++) {
            if (edges.hasConnects(i)) {
                return i;
            }
        }
        return lastIndex;
    }
}//end class graph

class vertex {

    //fields: integer for the vertex value, and flag that indicates whether the vertex has been found
    protected int vert;
    protected boolean flag = true;

    public vertex(int n) {
        vert = n;
    }

    public void changeFlag(boolean b) {
        flag = b;
    }
}//end class vertex

//Edge connects two vertexes
class Edge {

    private int edgeList[][];
    private int size;

    public Edge() {
    }

    public Edge(int n) {
        edgeList = new int[n][n];
        size = n;
    }

    //takes index values as input, NOT vertex values. draws edge between vertices 
    public void addEdge(int a, int b) {
        edgeList[a][b] = 1;
    }

    //takes current vertex and finds next vertex in search process
    public int findConnection(int index) {

        //returns index in vertex array of first connection from given vertex
        //changes value to 2 to indicate that this edge has been traversed
        for (int i = 0; i < size; i++) {
            if (edgeList[index][i] == 1) {
                edgeList[index][i] = 2;
                return i;
            }
        }
        return size;
    }

    //checks to see if vertex is conntected to any other vertices        
    public boolean hasConnects(int index) {
        for (int i = 0; i < size; i++) {
            if (edgeList[index][i] == 1) {
                return true;
            }
        }
        return false;
    }

    //displays DSF tree
    public void display(vertex[] v) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (edgeList[i][j] != 0) {
                    System.out.print(v[i].vert + "-->" + v[j].vert + "  ");
                }
            }
            System.out.println("");
        }
    }
}//end class edge

class DSF {

    graph Graph;
    int size;
    int time;

    public DSF(graph G) {
        Graph = G;
        size = Graph.howManyVerts();
    }

    //takes vertex value as input and calls find index method on value
    public void startSearch(int n) {
        
        //finds array index of vertex
        int start = Graph.findIndex(n);
        Search(start);
        int c = Graph.check();
        while (c != size) {
            Search(c);
            c = Graph.check();
        }
        Graph.display();
    }

    //searches through graph to find vertex value
    public void Search(int currInd) {

        time++;
        int nextInd;
        int d = time;

        System.out.println("Discovery time for vertex " + Graph.vertexValue(currInd) + " is " + time);
        while (Graph.connected(currInd)) {
            nextInd = Graph.findNextVert(currInd);
            if (Graph.beenFound(nextInd)) //if nextInd hasn't been found already, search it.
            {
                System.out.println("The parent node of " + Graph.vertexValue(nextInd) + " is " + Graph.vertexValue(currInd));
                Search(nextInd);
            }
        }
        time++;
        System.out.println("Finishing time for vertex " + Graph.vertexValue(currInd) + " is " + time + ". Total time spent in vertex " + Graph.vertexValue(currInd) + " is " + (time - d));
    }
}//end class DSF

class FinalProject {

    public static void main(String args[]) {

        //initialize new graph and add vertices
        graph Graph = new graph(7);
        for (int i = 1; i < 8; i++) {
            Graph.addVertex(i);
        }

        //manually build graph
        Graph.joinVerts(1, 2);
        Graph.joinVerts(1, 6);
        Graph.joinVerts(2, 3);
        Graph.joinVerts(2, 4);
        Graph.joinVerts(2, 5);
        Graph.joinVerts(3, 5);
        Graph.joinVerts(4, 5);
        Graph.joinVerts(5, 1);
        Graph.joinVerts(6, 4);
        Graph.joinVerts(6, 7);

        DSF dsf = new DSF(Graph);
        Scanner s = new Scanner(System.in);
        int first;
        System.out.println("Start search from which vertex? Choose a number 1-7");
        first = s.nextInt();
        try {
            dsf.startSearch(first);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("This value is not in the graph.");
        }

        //add vertices for second graph
        graph Graph2 = new graph(10);
        for (int i = 2; i < 40; i = i + 4) {
            Graph2.addVertex(i);
        }

        //manually build second gaph
        Graph2.joinVerts(2, 6);
        Graph2.joinVerts(2, 22);
        Graph2.joinVerts(6, 18);
        Graph2.joinVerts(6, 26);
        Graph2.joinVerts(10, 6);
        Graph2.joinVerts(14, 34);
        Graph2.joinVerts(18, 14);
        Graph2.joinVerts(18, 30);
        Graph2.joinVerts(22, 10);
        Graph2.joinVerts(16, 34);
        Graph2.joinVerts(30, 10);
        Graph2.joinVerts(30, 14);
        Graph2.joinVerts(34, 2);

        //start new search
        DSF dsf2 = new DSF(Graph2);
        System.out.println("Start search from which vertex? Choose one of the following: 2,6,10,14,18,22,26,30,34");
        first = s.nextInt();
        try {
            dsf2.startSearch(first);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("This value is not in the graph.");
        }
    }
}
