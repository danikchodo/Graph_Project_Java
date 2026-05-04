import java.io.*;
import java.nio.*;
import java.util.Scanner;

public interface Parser {
    void load(File file, Graph graph) throws IOException;
}

class txt_Parser implements Parser {
    @Override
    public void load(File file, Graph graph) throws IOException {
        graph.getNodes().clear();
        graph.getEdges().clear();

        try (Scanner sc = new Scanner(file)) {
            if (!sc.hasNextInt()) 
            return;
            
            int num_nodes = sc.nextInt();
            int num_edges = sc.nextInt();

            for (int i = 0; i < num_nodes; i++) {
                int id = sc.nextInt();
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                graph.addNode(new Graph.Node(id, x, y));
            }

            for (int i = 0; i < num_edges; i++) {
                int src = sc.nextInt();
                int dst = sc.nextInt();
                double weight = sc.nextDouble();
                graph.addEdge(new Graph.Edge(src, dst, weight));
            }
        }
    }
}

class bin_Parser implements Parser {
    @Override
    public void load(File file, Graph graph) throws IOException {
        graph.getNodes().clear();
        graph.getEdges().clear();

        byte[] allBytes = java.nio.file.Files.readAllBytes(file.toPath());
        ByteBuffer buffer = ByteBuffer.wrap(allBytes);
        
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        if (buffer.remaining() < 8) 
        return;

        int num_nodes = buffer.getInt();
        int num_edges = buffer.getInt();

        for (int i = 0; i<num_nodes; i++) {
            int id = buffer.getInt();
            double x = buffer.getDouble();
            double y = buffer.getDouble();
            graph.addNode(new Graph.Node(id, x, y));
        }

    
        for (int i = 0;i< num_edges; i++) {
            int src = buffer.getInt();
            int dst = buffer.getInt();
            double weight = buffer.getDouble();
            graph.addEdge(new Graph.Edge(src, dst, weight));
        }
    }
}