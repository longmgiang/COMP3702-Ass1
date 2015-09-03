package algorithm;

import java.util.Comparator;

public class NodePrioritize implements Comparator<Node>{

	@Override
	public int compare(Node o1, Node o2) {
        if(o1.getFCost() < o2.getFCost()) {
            return -1;
        } else if (o1.getFCost() > o2.getFCost()) {
            return 1;
        } else {
            return 0;
        }
	}
	
}
