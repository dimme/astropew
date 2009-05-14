package common;

/** Why isn't there a class like this in Java!? */
public class Pair<E,F> {
	
	public E item1;
	public F item2;
	
	public Pair(E e, F f) {
		this.item1 = e;
		this.item2 = f;
	}

}
