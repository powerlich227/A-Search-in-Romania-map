import java.util.*;

/**
 * Implement Romania Map by using 
 * 1) straight distance
 * 2) f = (1 - w)g + wh, where w = 0.25
 * 3) f = (1 - w)g + wh, where w = 0.75
 * as the Heuristic function.
 * Where g() function is the path length from the source, h() function is the direct distance to the destination.
 * @author Jinglin Li (jxl163530, 2021323767)
 *
 */
public class Data {

	/**
	 * get the expanded nodes list, also get the path
	 * @param hm			data
	 * @param cityToDir		get the direct distance (hValue) of the city
	 * @param source		"Zerind"
	 * @param destination	"Bucharest"
	 * @param path			get the path from the source to destination
	 * @param w				value w
	 * @return the expanded node list
	 */
	
	public HashSet<String> getPath(HashMap<String, ArrayList<Node>> hm, 
			HashMap<String, Integer> cityToDir, String source, String destination,
			ArrayList<String> path, double w) {
		
		// store the expanded node number when search the destination
		HashSet<String> count = new HashSet<>();
		
		// the priority queue to store the leaf node
		PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
			public int compare(Node o1, Node o2) {
				double temp = (1.0 - w) * (o1.gValue - o2.gValue) + w * (o1.hValue - o2.hValue);
				
				if (temp == 0)
					return 0;
				if (temp > 0)
					return 1;
				else
					return -1;
			}
		});
		
		HashSet<String> visited = new HashSet<>();
		ArrayList<String> pathRoute = new ArrayList<>();
		
		// store the source node
		pathRoute.add(source);
		pq.add(new Node(source, 0, cityToDir.get(source), pathRoute));
		
		count.add(source);
		
		while (!pq.isEmpty()) {
			Node temp = pq.poll();
			visited.add(temp.cityName);
			if (visited.contains(destination)) {
				
				// get the final path as the output
				for (String s : temp.path) {
					path.add(s);
				}
				
				// get the final expanded list as the output
				return count;
			}
			
			
			ArrayList<Node> neighbors = hm.get(temp.cityName);
			for (int i = 0; i < neighbors.size(); i++) {
				Node node = neighbors.get(i);
				
				if (!count.contains(node.cityName))
					count.add(node.cityName);
				
				// update the gValue of the node's neighbors
				ArrayList<String> tempRoute = new ArrayList<>(temp.path);			
				if (!visited.contains(node.cityName)) {
					
					tempRoute.add(node.cityName);
					Node newNode = new Node(node.cityName, node.gValue + temp.gValue, 
							cityToDir.get(node.cityName), tempRoute);
					neighbors.remove(i);
					neighbors.add(i, newNode);
					
					pq.add(newNode);
				}
			}
		}
		return null;
	}
	
	/**
	 * this function return the cost of the resulting path, that is, the length of path
	 * @param path		the final path according to the corresponding heuristic value
	 * @param hm		data
	 * @return			the cost
	 */
	
	public int getCost(ArrayList<String> path, HashMap<String, ArrayList<Node>> hm) {
		
		// return the g Value of the destination
		ArrayList<Node> neighbors = hm.get(path.get(path.size() - 2));
		for (Node node : neighbors) {
			if (node.cityName.equals(path.get(path.size() - 1)))
				return (int) node.gValue;
		}
		return -1;
	}
	
	/**
	 * Main function
	 * parse the data, output the path, expanded node number, cost
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		String[] cities = {"Arad", "Bucharest", "Craiova", "Dobreta", "Eforie", "Fagaras", "Giurgiu", "Hirsova", "Iasi", "Lugoj", 
				"Mehadia", "Neamt", "Oradea", "Pitesti",  "Rimnicu Vilcea", "Sibiu", "Timisoara", "Urziceni", "Vaslui", "Zerind"};
		
		int[] dirDis = {366, 0, 160, 242, 161, 176, 77, 151, 226, 244, 241, 234, 380, 98, 193, 253, 329, 80, 199, 374};
		
		
		// map the city to its direct distance to "Bucharest"
		HashMap<String, Integer> cityToDir = new HashMap<>();
		for (int i = 0; i < cities.length; i++) {
			cityToDir.put(cities[i], dirDis[i]);
		}
		
		// map the city to its neighbors
		HashMap<String, ArrayList<Node>> hm = new HashMap<>();
		HashMap<String, ArrayList<Node>> hm2 = new HashMap<>();
		HashMap<String, ArrayList<Node>> hm3 = new HashMap<>();
		
		String[][] nData = {{"Zerind", "75", "Sibiu", "140", "Timisoara", "118"},
				{"Fagaras", "211", "Pitesti", "101", "Giurgiu", "90", "Urziceni", "85"}, 
				{"Dobreta", "120", "Rimnicu Vilcea", "146", "Pitesti", "138"}, 
				{"Mehadia", "75", "Craiova", "120"}, 
				{"Hirsova", "86"},				
				{"Sibiu", "99", "Bucharest", "211"},
				{"Bucharest", "90"}, 
				{"Urziceni", "98", "Eforie", "86"}, 
				{"Neamt", "87", "Vaslui", "92"}, 
				{"Timisoara", "111", "Mehadia", "70"},
				{"Lugoj", "70", "Dobreta", "75"},
				{"Iasi", "87"},
				{"Zerind", "71", "Sibiu", "151"},
				{"Rimnicu Vilcea", "97", "Craiova", "138", "Bucharest", "101"},
				{"Sibiu", "80", "Pitesti", "97", "Craiova", "146"},
				{"Arad", "140", "Oradea", "151", "Fagaras", "99", "Rimnicu Vilcea", "80"},
				{"Arad", "118", "Lugoj", "111"},
				{"Bucharest", "85", "Hirsova", "98", "Vaslui", "142"},
				{"Iasi", "92", "Urziceni", "142"},
				{"Arad", "75", "Oradea", "71"}};
		
		
		// parse the neighbors into the HashMap
		for (int i = 0; i < nData.length; i++) {
			ArrayList<Node> list = new ArrayList<>();
			for (int j = 0; j < nData[i].length; j += 2) {
				Node n = new Node(nData[i][j], Integer.parseInt(nData[i][j + 1]), cityToDir.get(nData[i][j]), null);
				list.add(n);
			}
			hm.put(cities[i], list);
		}	

		for (int i = 0; i < nData.length; i++) {
			ArrayList<Node> list = new ArrayList<>();
			for (int j = 0; j < nData[i].length; j += 2) {
				Node n = new Node(nData[i][j], Integer.parseInt(nData[i][j + 1]), cityToDir.get(nData[i][j]), null);
				list.add(n);
			}
			hm2.put(cities[i], list);
		}
		
		for (int i = 0; i < nData.length; i++) {
			ArrayList<Node> list = new ArrayList<>();
			for (int j = 0; j < nData[i].length; j += 2) {
				Node n = new Node(nData[i][j], Integer.parseInt(nData[i][j + 1]), cityToDir.get(nData[i][j]), null);
				list.add(n);
			}
			hm3.put(cities[i], list);
		}
		
		
		Data test = new Data();
	
		// output
		
		System.out.println("From \"Zerind\" to \"Bucharest\"\n");
		
		System.out.println("Straint Distance as Heuristic: (w = 1.0)"); 
		ArrayList<String> path = new ArrayList<String>();
		HashSet<String> count = test.getPath(hm, cityToDir, "Zerind", "Bucharest", path, 1);	
		
		System.out.println("Q(a)    Path: " + path);
		System.out.println("Q(b)    Expanded Node Number: " + count.size());
		System.out.println("Q(c)    Cost(Path Length): " + test.getCost(path, hm) +"\n") ;
		
		
		System.out.println("Function f = (1 - w)g + wh as Heuristic, where w = 0.25"); 
		ArrayList<String> path2 = new ArrayList<String>();	
		HashSet<String> count2 = test.getPath(hm2, cityToDir, "Zerind", "Bucharest", path2, 0.25);
		System.out.println("Q(a)    Path: " + path2);
		System.out.println("Q(b)    Expanded Node Number: " + count2.size());
		System.out.println("Q(c)    Cost(Path Length): " + test.getCost(path2, hm2) +"\n") ;
		
		
		System.out.println("Function f = (1 - w)g + wh as Heuristic, where w = 0.75"); 
		ArrayList<String> path3 = new ArrayList<String>();	
		HashSet<String> count3 = test.getPath(hm3, cityToDir, "Zerind", "Bucharest", path3, 0.75);
		System.out.println("Q(a)    Path: " + path3);
		System.out.println("Q(b)    Expanded Node Number: " + count3.size());
		System.out.println("Q(c)    Cost(Path Length): " + test.getCost(path3, hm3)) ;
		
	
		
	}
}
