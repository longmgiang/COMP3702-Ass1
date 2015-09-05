package algorithm;

import java.util.Comparator;

public class NodePrioritize implements Comparator<Node>{

	@Override
	public int compare(Node n1, Node n2) {
        if(n1.getFCost() < n2.getFCost()) {
            return -1;
        } else if (n1.getFCost() > n2.getFCost()) {
            return 1;
        } else {
            return 0;
        }
	}
	
}
