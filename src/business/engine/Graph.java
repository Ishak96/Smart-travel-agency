package business.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Graph {
	private HashMap<String, Float> dist;
	private HashMap<String, Node> prev;
	private HashMap<String, Object> objectMapping;
	
    private Set<String> settled; 
    private PriorityQueue<Node> pq;
    private HashMap<String, List<Node>> adj;
    
    // TODO: remove V
    public Graph(int V) {
    	this(V, new HashMap<String, List<Node>>());
    }
	public Graph(int V, HashMap<String, List<Node>> adj) {
		dist = new HashMap<String, Float>();
    	prev = new HashMap<String, Node>();
    	objectMapping = new HashMap<String, Object>();

    	settled = new HashSet<String>(); 
    	pq = new PriorityQueue<Node>(V, new Node()); 
    	this.adj = adj;
	}
	
    // Function for Dijkstra's Algorithm 
    public void dijkstra(String src) {
        // Add source node to the priority queue 
        pq.add(new Node(src, 0)); 
  
        // Distance to the source is 0 
        dist.put(src, (float) 0); 
        //while (settled.size() != V) { 
        while(pq.size() > 0) {
            // remove the minimum distance node  
            // from the priority queue  
            Node u = pq.remove(); 
            // adding the node whose distance is 
            // finalized 
            settled.add(u.node); 
  
            dijkstraHelper(u); 
        }
    } 
  
    public List<Object> getPath(String src, String dst) {
    	List<Object> res = new ArrayList<Object>();
    	
		while(!dst.equals(src)) {
			res.add(objectMapping.get(dst)); 

			Node n = prev.get(dst);
			if(n == null) {
				return null;
			}
			
			dst = n.node;
		}
		res.add(objectMapping.get(src));
		
		Collections.reverse(res);
		return res;
    }
    
    // Function to process all the neighbors  
    // of the passed node 
    private void dijkstraHelper(Node u) {
        float edgeDistance = -1; 
        float newDistance = -1; 
  
        // All the neighbors of v 
        List<Node> lst = adj.get(u.node);
        if(lst == null) { return ; } // sanity check
        for (Node v: lst) {  
            // If current node hasn't already been processed 
            if (!settled.contains(v.node)) { 
                edgeDistance = v.cost;
            	newDistance = getDist(u.node) + edgeDistance;

                
                // If new distance is cheaper in cost 
                if (newDistance < getDist(v.node)) {
                    dist.put(v.node, newDistance); 
                    prev.put(v.node, u);
                }
                // Add the current node to the queue 
                pq.add(new Node(v.node, getDist(v.node))); 
            } 
        } 
    }
    
    /**
     * @param k the key to access from
     * @return Float.MAX_VALUE as a default, otherwise the actual value
     */
    private float getDist(String k) {
        Float distVal = dist.get(k);
        float val = Float.MAX_VALUE;
        if(distVal != null) {
        	val = dist.get(k); 
        }
        
        return val;
    }
    
	public HashMap<String, List<Node>> getAdj() {
		return adj;
	}

	public void setAdj(HashMap<String, List<Node>> adj) {
		this.adj = adj;
	}
	
	public void setObjectMapping(HashMap<String, Object> objectMapping) {
		this.objectMapping = objectMapping;
	}
	
	/*public void addObjectMappingEntry(String src, Object o) {
		objectMapping.put(src, o);
	}*/
	
	// TODO: maybe put back Object here, and add both src and entry to object mapping
	public void addAdjacencyEntry(String src, Object o, Node entry) {
		// objectMapping.put(src, o);
		objectMapping.put(entry.node, o);

		// TODO: refactor this
		List<Node> lst = adj.get(src);
	    if(lst == null) {
	    	lst = new ArrayList<Node>();
	    	adj.put(src, lst);
	    }
	    lst.add(entry);
	}
	
	public void removeAdjacencyList(String src) {
		adj.remove(src);
	}
	
	
	public void addAdjacencyEntries(String src, Object o, List<Node> entries) {
	    List<Node> lst = adj.get(src);
	    if(lst == null) {
	    	lst = new ArrayList<Node>();
	    	adj.put(src, lst);
	    }

	    lst.addAll(entries);
	    // TODO: should change
		// objectMapping.put(src, o);
	    for(Node n: entries) {
			objectMapping.put(n.node, o);			
	    }
	}
	
	public HashMap<String, Node> getPrev() {
		return prev;
	}   
}