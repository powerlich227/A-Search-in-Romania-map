import java.util.*;
public class Node {
	String cityName;
	
	// connect distance from the source
	double gValue;
	
	// direct distance
	double hValue;

	ArrayList<String> path;
	
	public Node(String cityName, double gValue, double fValue, ArrayList<String> path) {
		this.cityName = cityName;
		this.gValue = gValue;
		this.hValue = fValue;
		this.path = path;
	}
}
