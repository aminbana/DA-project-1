import java.util.Scanner;
        import javax.security.auth.Subject;
        import java.awt.*;
        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.LinkedList;
        import java.util.List;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);




        String [] str = sc.nextLine().split(" ");
        int n = Integer.parseInt(str[0]);
        Graph g = new Graph (n);
        int number_of_ks  = Integer.parseInt(str[1]);


//        g.addEdge(1 , 3);
//        g.addEdge(0 , 3);
//        g.addEdge(0 , 2);
//        g.addEdge(1,4);
//        g.addEdge(2,3);

        int [][] Adj_Matrix = new int[n][n];
        for (int i = 0; i < n; i++){
            String[] lines = sc.nextLine().split(" ");
            for (int j = 0; j < n; j++){
                Adj_Matrix[i][j] = Integer.parseInt(lines[j]);
                //System.out.println(Adj_Matrix[i][j]);
            }
//            sc.nextLine();
        }


        for (int i = 0; i < n - 1; i++){
            for (int j = i + 1 ; j < n; j++){
                if (Adj_Matrix [i][j] == 1){
                    g.addEdge(i , j);
                }
            }
        }


        int [] ks = new int[number_of_ks];
        for (int i = 0; i < number_of_ks; i++) {
            ks[i] = Integer.parseInt(sc.nextLine());
        }


        boolean Connected = g.isConnected();

        if (Connected){
            System.out.println("1");
        } else {
            System.out.println("0");
        }

//        g.Gprint();

//        g.unmarkAll();
//        System.out.println(g.Furthest(3));

        if (Connected){
            System.out.println(g.findDiameter());
        } else {
            System.out.println("-1");
        }


//        g.GprintDist();


        if (Connected){
            System.out.println(g.Center() + 1 ); //zero based indexing
        } else {
            System.out.println("-1");
        }


        for (int k:ks){
//            System.out.println(k);
            g.Colony(k);
//            System.out.println("**************");
        }



//        2 10
//        0 1 0 0 0 0 0 0 1 1
//        1 0 0 0 0 0 0 1 1 0
//        0 0 0 1 1 1 0 0 1 0
//        0 0 1 0 1 1 0 0 0 0
//        0 0 1 1 0 1 0 0 0 0
//        0 0 1 1 1 0 1 1 0 0
//        0 0 0 0 0 1 0 1 0 0
//        0 1 0 0 0 1 1 0 0 1
//        1 1 1 0 0 0 0 0 0 1
//        1 0 0 0 0 0 0 1 1 0
//        2
//        3

        //    g.colony(2);

        //g.colony(3);

        // g.colony(4);

        //g.colony(5);

        // System.out.println(g.subset(3 , 0 , 9).size());
        //g.printPartition(g.subset(3 , 0 , 9));


    }
}

class Graph {
    int num_of_vertices = 0;
    int num_of_edges = 0;
    ArrayList <Vertex> Vertices = null;
    ArrayList <Edge> Edges = null;
    int [][] Adj_Matrix;
    int [][] Dist_Matrix;

    int [] Colony_Value;

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

        Colony_Value = new int[num_of_vertices];

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
        unmarkAll();

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



    public void unmarkAll(){
        for (int i = 0; i < num_of_vertices; i++){
            Vertices.get(i).marked = false;
        }
    }

    //*********************************************************


    public int findDiameter (){
        int max_dist = 0;
        //   System.out.println("***********************");
        for (int i = 0; i < num_of_vertices; i++){
            unmarkAll();
            int dist = Furthest(i);
            //      System.out.println("dist( : " + i + ") = " + dist);
            if (dist > max_dist){
                max_dist = dist;
            }
        }

        return max_dist;
    }


    public int Furthest (int idx){
        //    System.out.println("BFS init = " + idx);
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
//                    System.out.println("visiting vertix : " + u.ID);
//                    System.out.println("distance : " + max_distance);

                    l.addFirst(u);
                    d.addFirst(dist + 1);

                }
            }
        }

        //System.out.println("max_distance : " + max_distance);
        return max_distance;
    }


    //*********************************************************




    public int Center (){

        for (int i = 0; i < num_of_vertices - 1; i++){
            for (int j = i+1; j < num_of_vertices; j++) {
                if (Dist_Matrix[i][j] == -1){
                    findDistance(i , j);
                    //      System.out.println("i = " + i + " j = " + j + " Dist(i,j):" + Dist_Matrix[i][j]);
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

//            System.out.println(distance_sum);
            if (min_distance_sum > distance_sum ){
//                System.out.println("here");
                center = i;
                min_distance_sum = distance_sum;
            }
        }


        return center;
    }

    public void findDistance (int i , int j){
        for (int k = 0; k < num_of_vertices; k++){
            Vertices.get(k).marked = false;
        }
        BFS_Distance (Vertices.get(i) , Vertices.get(j));
    }


    public void BFS_Distance  (Vertex v , Vertex u){


        LinkedList<Vertex> l = new LinkedList<>();
        LinkedList<Integer> d = new LinkedList<>();
        v.marked = true;
        l.addLast(v);
        d.addLast(0);

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
                    l.addFirst(U);
                    d.addFirst(dist + 1);

                }
            }
        }

    }


    public void Colony (int k){

        for (int i = 0; i < num_of_vertices; i++){
            Colony_Value[i] = 0;
        }

        int max_value = -1000000;
        int [] max_colony_value = new int[num_of_vertices];
        for (int i = 0 ; i < (int) Math.pow(k , num_of_vertices); i++){

            int value = Colony_Value();
            boolean valid = isColonyValid (k);


            if ((max_value < value) & (valid)){

                max_value = value;
                for (int j = 0; j < num_of_vertices; j++){
                    max_colony_value[j] = Colony_Value[j];
                }
            }

            if (i < (int) Math.pow(k , num_of_vertices))
                nextColony(k);

        }

        printColony (max_colony_value , k);

    }

    public void printColony(int [] colony ,int k){

        ArrayList <ArrayList <Integer>> List = new ArrayList<>();
        ArrayList<Integer> AList = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < num_of_vertices; j++) {
                if (colony[j] == i) {
                    int jp1 = j + 1;
                    AList.add(jp1);
//                    System.out.print(jp1 + " ");
                }
            }
            List.add(AList);
            AList = new ArrayList<>();
        }


        for (int i = 0; i < List.size() ; i++){
            ArrayList<Integer> Alist = List.get(i);
            for (int j = 0; j < Alist.size(); j++){
                System.out.print(Alist.get(j));
                if (j < Alist.size() - 1){
                    System.out.print(" ");
                }
            }

            if (i < List.size() - 1){
                System.out.print(",");
            }


        }
        System.out.println("");
    }


    public boolean isColonyValid(int k){
        int i;
        for (int j = 0; j < k; j++){
            for (i = 0; i < num_of_vertices; i++ ){
                if (Colony_Value[i] == j){
                    break;
                }
            }
            if (i == num_of_vertices){
                return false;
            }
        }
        return true;
    }


    public void nextColony (int k){
        Colony_Value[0]++;
        for (int i = 0; i < num_of_vertices; i++){
            if (Colony_Value[i] == k){
                Colony_Value[i] = 0;
                if (i < num_of_vertices - 1)
                    Colony_Value[i+1]++;
            } else if (Colony_Value[i] == k + 1){
                Colony_Value[i] = 1;
                if (i < num_of_vertices - 1)
                    Colony_Value[i+1]++;

            }
        }
    }





    public int Colony_Value (){
        int value = 0;
        for (Edge edge:Edges){
            if (Colony_Value[edge.v1.ID] == Colony_Value[edge.v2.ID]){
                value++;
            } else {
                value--;
            }
        }
        return value;
    }


}


class Edge {
    Vertex v1;
    Vertex v2;

    Edge(Vertex v1 , Vertex v2){
        this.v1 = v1;
        this.v2 = v2;
    }


}


class Vertex {
    public int ID = -1;
    public LinkedList <Vertex> Adjacent = new LinkedList<>();
    public int num_edges = 0;
    public boolean marked = false;
    public int Colony_ID = -1;

    Vertex (int ID){
        this.ID = ID;
    }

    public void addEdge (Vertex Adj){
        this.Adjacent.add(Adj);
        num_edges++;
    }

}




