public class Main {
    public static void main(String[] args) {
        Graph g = new Graph (5);
        g.addEdge(1 , 3);
        g.addEdge(0 , 3);
        g.addEdge(0 , 2);
        g.addEdge(1,4);
        g.addEdge(2,3);

        boolean Connected = g.isConnected();

        System.out.println(Connected);
        g.Gprint();
        System.out.println(g.Furthest(3));
        System.out.println(g.findDiameter());

        g.GprintDist();

        if (Connected){
            System.out.println(g.Center());
        } else {
            System.out.println("-1");
        }


        g.GprintDist();


    }
}
