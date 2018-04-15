
public class Node {
	 private Integer informazione;
	 private Node sx, dx;
	 private int posizione;

	     
	    public Node(int posizione){
	    	this.posizione = posizione;
	        this.setInf(0);
	        setSx(null);
	        setDx(null);
	    }
	     
	    public Node(int posizione, int number) 
	    {
	    	this.posizione = posizione;
	        this.setInf(number);
	    }
	    
	    public int getPos(){
	    	return this.posizione;
	    }

		public int getInf(){
			return informazione;
		}
		
		public void setInf(int informazione){
			this.informazione = informazione;
		}
		
		public void setInf(Integer informazione){
			this.informazione = informazione;
		}

		public Node getSx(){
			return sx;
		}

		public void setSx(Node sx){
			this.sx = sx;
		}

		public Node getDx(){
			return dx;
		}

		public void setDx(Node dx){
			this.dx = dx;
		}
	    
	    
	    
}
