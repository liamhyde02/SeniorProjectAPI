package helloworld.Patterns;

import helloworld.GraphBuilder.DataStorage;
import helloworld.GraphBuilder.Edge;
import helloworld.GraphBuilder.Node;

import java.util.ArrayList;

public class SingletonPatternStep extends PatternChainLink{
    public SingletonPatternStep(PatternChain nextLink) {
        super(nextLink);
    }

    @Override
    public void ParsePattern() {
        ArrayList<Node> nodes = DataStorage.getInstance().getNodes();
        ArrayList<Edge> edges = DataStorage.getInstance().getEdges();
        for (Node node : nodes) {
            for (Edge edge : edges) {
                if (edge.getFrom().equals(node.getName()) && edge.getTo().equals(node.getName())) {
                    node.setPattern("Singleton");
                }
            }
        }
        if (nextLink != null) {
            nextLink.ParsePattern();
        }
    }
}
