import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);



        int number_of_ks = sc.nextInt();

        int n = sc.nextInt();
        Graph g = new Graph (n);



        sc.nextLine();

//        g.addEdge(1 , 3);
//        g.addEdge(0 , 3);
//        g.addEdge(0 , 2);
//        g.addEdge(1,4);
//        g.addEdge(2,3);

        int [][] Adj_Matrix = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                Adj_Matrix[i][j] = sc.nextInt();
            }
            sc.nextLine();
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
            ks[i] = sc.nextInt();
            sc.nextLine();
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


        System.out.println(g.findDiameter());

//        g.GprintDist();


        if (Connected){
            System.out.println(g.Center() + 1 ); //zero based indexing
        } else {
            System.out.println("-1");
        }


        for (int k:ks){
//            System.out.println(k);
            g.colony(k);
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
