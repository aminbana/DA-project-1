import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    int num_of_vertices = 0;
    int num_of_edges = 0;
    ArrayList <Vertex> Vertices = null;
    ArrayList <Edge> Edges = null;
    int [][] Adj_Matrix;
    int [][] Dist_Matrix;

    int Number_of_DFS_Visited = 0;
    int Num_of_Segs = 0;

    Graph(int n){
        this.num_of_vertices = n;
        Vertices = new ArrayList <>();
        Edges = new ArrayList<>();

        for (int i=0; i<n; i++) {
            Vertices.add(new Vertex(i));
        }
        Adj_Matrix = new int[n][n];
        Dist_Matrix = new int[n][n];
        for (int i=0; i<n; i++) {
            for (int j=0; j<n ; j++){
                Dist_Matrix[i][j] = -1;
            }
        }

    }

    public void addEdge (int ID1 , int ID2){
        Vertices.get(ID1).addEdge(Vertices.get(ID2));
        Vertices.get(ID2).addEdge(Vertices.get(ID1));

        Edges.add(new Edge(Vertices.get(ID1) , Vertices.get(ID2)));
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


    public void GprintDist (){
        for (int i=0; i<num_of_vertices; i++) {
            for (int j=0; j<num_of_vertices ; j++){
                System.out.print(Dist_Matrix[i][j] + " ");
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

        if (Num_of_Segs == 1)
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


    public void BFS_Distance  (Vertex v , Vertex u){


        LinkedList<Vertex> l = new LinkedList<>();
        LinkedList<Integer> d = new LinkedList<>();
        v.marked = true;
        l.addLast(v);
        d.addLast(0);
        System.out.println("HEERE");

        while (l.size() > 0){
            Vertex V = l.removeLast();
            int dist = d.removeLast();

            for (int i = 0 ; i < V.num_edges; i++){
                Vertex U = V.Adjacent.get(i);

                if (!U.marked){
                    if (Dist_Matrix[U.ID][v.ID] == -1){
                        Dist_Matrix[U.ID][v.ID] = dist + 1;
                        Dist_Matrix[v.ID][U.ID] = dist + 1;
                    }

                    if (U.ID == u.ID)
                        return;

                    U.marked = true;
                    l.addLast(U);
                    d.addLast(dist + 1);

                }
            }
        }

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


    public void findDistance (int i , int j){
        for (int k = 0; k < num_of_vertices; k++){
            Vertices.get(k).marked = false;
        }
        BFS_Distance (Vertices.get(i) , Vertices.get(j));
    }


    public int Center (){

        for (int i = 0; i < num_of_vertices - 1; i++){
            for (int j = i+1; j < num_of_vertices; j++) {
                    if (Dist_Matrix[i][j] == -1){
                        findDistance(i , j);
                        System.out.println("i = " + i + " j = " + j + " Dist(i,j):" + Dist_Matrix[i][j]);
                    }
            }
        }

        int min_distance_sum = 1000000000;
        int center = 0;

        int distance_sum = 0;
        for (int i = 0; i < num_of_vertices; i++){

            distance_sum = 0;
            for (int j = 0; j < num_of_vertices; j++) {
                distance_sum += Dist_Matrix[i][j];
            }

            System.out.println(distance_sum);
            if (min_distance_sum > distance_sum ){
                System.out.println("here");
                center = i;
                min_distance_sum = distance_sum;
            }
        }


        return center;
    }

    public void colony (int k){
        ArrayList<ArrayList<ArrayList<Vertex>>> l = partition_generator (0 , num_of_edges - 1, k);


        for (int i = 0; i < l.size(); i++){
            ArrayList<ArrayList<Vertex>> partition = l.get(i);
            for (int j = 0; j < partition.size(); j++) {
                ArrayList<Vertex> sub_partition = partition.get(j);
                System.out.print("{ ");
                for (int t = 0; t < sub_partition.size(); t++) {
                    System.out.print(sub_partition.get(t).ID);
                    if (t < sub_partition.size() - 1){
                        System.out.print(" ,");
                    }
                }
                System.out.print(" }");
                if (j < partition.size() - 1){
                    System.out.print(" , ");
                }
            }

            System.out.println("");
        }

        System.out.println("********************");


        int max_value = -1000000;
        ArrayList<ArrayList<Vertex>> best_partition = null;
        for (int i = 0; i < l.size(); i++){
            int value = colony_value(l.get(i));
            System.out.println("Value   :" + value);
            if (value > max_value){
                max_value = value;
                best_partition = l.get(i);
            }
        }

        System.out.println("Best Partition: ");
        for (int j = 0; j < best_partition.size(); j++) {
            ArrayList<Vertex> sub_partition = best_partition.get(j);
            System.out.print("{ ");
            for (int t = 0; t < sub_partition.size(); t++) {
                System.out.print(sub_partition.get(t).ID);
                if (t < sub_partition.size() - 1){
                    System.out.print(" ,");
                }
            }
            System.out.print(" }");
            if (j < best_partition.size() - 1){
                System.out.print(" , ");
            }
        }


    }




    public int colony_value (ArrayList<ArrayList<Vertex>> partition){
        int value = 0;

        for (int j = 0; j < partition.size(); j++) {
            ArrayList<Vertex> sub_partition = partition.get(j);
            for (int t = 0; t < sub_partition.size(); t++) {
                sub_partition.get(t).Colony_ID = j;
                System.out.println("j : " + j);
            }
        }

        for (Edge edge:Edges){

            if (edge.v1.Colony_ID == edge.v2.Colony_ID){
                value++;
            } else {
                value--;
            }
        }
        return value;
    }

    public ArrayList<ArrayList<ArrayList<Vertex>>> partition_generator (int start , int stop ,int k){
        if ((stop - start + 1) == k){
            ArrayList<ArrayList<ArrayList<Vertex>>> list = new ArrayList<>();
            ArrayList<ArrayList<Vertex>> sublist = new ArrayList<>();

            for (int i = start; i <= stop; i++){
                ArrayList<Vertex> l = new ArrayList<>();
                l.add(Vertices.get(i));
                sublist.add(l);
            }
             list.add(sublist);
            return list;
        }

        ArrayList<ArrayList<ArrayList<Vertex>>> list_k_1 = partition_generator(start + 1 , stop , k);
        ArrayList<ArrayList<ArrayList<Vertex>>> list_k = new ArrayList<>();

       for (int i = 0; i < list_k_1.size(); i++){
           ArrayList<ArrayList<Vertex>> partition_k_1 = list_k_1.get(i);
           for (int t = 0; t < k; t++){
               ArrayList<ArrayList<Vertex>> partition_k = new ArrayList<>();
                for (int j = 0; j < partition_k_1.size(); j++){
                    ArrayList<Vertex> partition_subset_k_1 = partition_k_1.get(j);
                   ArrayList<Vertex> partition_subset_k = new ArrayList<> (partition_subset_k_1);

                   //partition_subset_k.addAll(partition_subset_k_1);


//                   for (int l = 0; l < partition_subset_k_1.size(); l++){
//                       partition_subset_k.add(partition_subset_k_1.get(l));
//                   }
                    partition_k.add(partition_subset_k);
               }
               partition_k.get(t).add(Vertices.get(start));
               list_k.add(partition_k);
           }
       }


        return list_k;
    }

}
