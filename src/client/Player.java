package client;

public class Player {

	private String name;
	private int id;
	
	public Player(String name, int id){
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Player) {
			return id == ((Player)o).id;
		}
		return false;
	}
	
	public int hashCode() {
		return id;
	}
	
}
