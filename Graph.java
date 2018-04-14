import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    int num_of_vertices = 0;
    int num_of_edges = 0;
    ArrayList <Vertex> Vertices = null;
    int [][] Adj_Matrix;

    int Number_of_DFS_Visited = 0;
    int Num_of_Segs = 0;

    Graph(int n){
        this.num_of_vertices = n;
        Vertices = new ArrayList <>();
        for (int i=0; i<n; i++) {
            Vertices.add(new Vertex(i));
        }
        Adj_Matrix = new int[n][n];

//        for (int i=0; i<n; i++) {
//            for (int j=0; j<n ; j++){
//                Adj_Matrix[i][j] = 0;
//            }
//        }

    }

    public void addEdge (int ID1 , int ID2){
        Vertices.get(ID1).addEdge(Vertices.get(ID2));
        Vertices.get(ID2).addEdge(Vertices.get(ID1));
        Adj_Matrix[ID1][ID2] = 1;
        Adj_Matrix[ID2][ID1] = 1;
        num_of_edges++;

    }

    public void Gprint (){
        for (int i=0; i<num_of_vertices; i++) {
            for (int j=0; j<num_of_vertices ; j++){
                System.out.print(Adj_Matrix[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.println("Number of Segments = " + Num_of_Segs);

    }

    public boolean isConnected (){
        Number_of_DFS_Visited = 0;

        for (int i = 0; i < num_of_vertices ; i++){
            if (Vertices.get(i).marked == false){
                Num_of_Segs++;
                DFS_Check (i);
            }
        }

        if (Num_of_Segs == num_of_vertices)
            return true;
        else
            return false;
    }


    public void DFS_Check (int num){
        Vertex V = Vertices.get(num);
        V.marked = true;
        Number_of_DFS_Visited++;
        Iterator<Vertex> I = V.Adjacent.iterator();
        Vertex v = null;
        while (I.hasNext()){
            v = I.next();
            if (!v.marked){
                DFS_Check (v.ID);
            }
        }
    }


    public int Furthest (int idx){
        for (int i = 0; i < num_of_vertices; i++){
            Vertices.get(i).marked = false;
        }
        return BFS_Diameter (Vertices.get(idx));
    }

    public int BFS_Diameter  (Vertex v){
        int max_distance = 0;
        Vertex farthest = null;
        LinkedList<Vertex> l = new LinkedList<>();
        LinkedList<Integer> d = new LinkedList<>();
        v.marked = true;
        l.addLast(v);
        d.addLast(0);

        while (l.size() > 0){
            Vertex V = l.removeLast();
            int dist = d.removeLast();

            for (int i = 0 ; i < V.num_edges; i++){
                Vertex u = V.Adjacent.get(i);
                if (!u.marked){
                    u.marked = true;
                    farthest = u;
                    max_distance = dist + 1;

                    l.addLast(u);
                    d.addLast(dist + 1);

                }
            }
        }


        return max_distance;
    }


    public int findDiameter (){
        int max_dist = 0;

        for (int i = 0; i < num_of_edges; i++){
            int dist = Furthest(i);
            if (dist > max_dist){
                max_dist = dist;
            }
        }

        return max_dist;
    }

}
