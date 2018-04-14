import java.util.LinkedList;

public class Vertex {
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
