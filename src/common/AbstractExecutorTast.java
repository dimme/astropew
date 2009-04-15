package common;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractExecutorTast implements Runnable {

	public abstract void execute() throws RuntimeException;
	
	public void run() {
		try {
			execute();
		} catch (RuntimeException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, e.getMessage(), e);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
