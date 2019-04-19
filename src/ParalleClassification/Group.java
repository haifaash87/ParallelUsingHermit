package ParalleClassification;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Group {

	private ArrayList<Integer> nodes;
	private int index;//Create a group with a unique index
	private List<Integer> mark = null;//Record all the index in one group
	
	public Group(int index)
	{
		this.nodes = new ArrayList<Integer>();
		this.index = index;
		this.mark = new ArrayList<Integer>();
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getGroupSize() {
		return nodes.size();
	}
	
	public ArrayList<Integer> getNodes() {
		return nodes;
	}
	
	public boolean checkExsitingNode(int index) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i) == index)
				return true;
		}

		return false;
	}
	
	public void addNode(int nodeIndex) {
		nodes.add(nodeIndex);
	}
	
	
	public boolean checkExistingFlag(int flag) {
		for (int i = 0; i < mark.size(); i++) {
			if (mark.get(i) == flag)
				return true;
		}

		return false;
	}
}
