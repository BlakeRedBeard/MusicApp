import java.util.ArrayList;

public class Node {
	 
	 private ArrayList<Node> sons;   //array contenente i nodi figli, essi fanno parte tutti dello stesso livello
	 private Node father;			 //nodo padre, nel caso del primo nodo esso avrà valore nullo
	 private String path, name;		 //le informazioni che si vogliono salvare nel nodo
	 
	 
	 public Node() {
		 this.sons = null;
		 this.setFather(null);
		 this.setPath(null);
		 this.setName(null);
	 }
	 
	 public Node(Node father) {
		 this.sons = null;
		 this.setFather(father);
		 this.setPath(null);
		 this.setName(null);
	 }
	 
	 public Node(String path, String name) {
		 this.sons = null;
		 this.setFather(null);
		 this.setPath(path);
		 this.setName(name);
	 }
	 
	 public Node(Node father, String path, String name) {
		 this.sons = null;
		 this.setFather(father);
		 this.setPath(path);
		 this.setName(name);
	 }


	public Node getFather() {
		return father;
	}


	public void setFather(Node father) {
		this.father = father;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	public void addSon(Node son) {
		this.sons.add(son);
		son.setFather(this);
	}
	
	public void addSon(String path, String name) {
		this.sons.add(new Node(this, path, name));
	}
	
	
	 
	 
}
