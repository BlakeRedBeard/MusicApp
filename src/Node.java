import java.util.ArrayList;

public class Node {
	 
	 private ArrayList<Node> sons;   //array contenente i nodi figli, essi fanno parte tutti dello stesso livello
	 private ArrayList<String> sonsName;
	 private Node father;			 //nodo padre, nel caso del primo nodo esso avrà valore nullo
	 private String path, name;		 //le informazioni che si vogliono salvare nel nodo
	 private String primaryKey;		 //l'attributo è composto dal tipo della PK seguito dal valore (es: int;5)
	 
	 public Node() {
		 this.sons = null;
		 this.setFather(null);
		 this.setPath(null);
		 this.setName(null);
		 this.setPrimaryKey(null);
	 }
	 
	 public Node(Node father, String path, String name, String primaryKey) {
		 this.sons = null;
		 this.setFather(father);
		 this.setPath(path);
		 this.setName(name);
		 this.setPrimaryKey(primaryKey);
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
	
	
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void addSon(Node son) {
		this.sons.add(son);
		son.setFather(this);
	}
	
	public void addSon(String path, String name, String primaryKey) {
		this.sons.add(new Node(this, path, name, primaryKey));
		this.sonsName.add(name);
	}
	
	public ArrayList<String> getSonsName(){
		this.sons.
		return this.sonsName;
	}
	
	
	 
	 
}
