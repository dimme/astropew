package common;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractExecutorTask implements Runnable {

	public abstract void execute();
	
	public void run() {
		try {
			execute();
		} catch (Throwable t) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, t.getMessage(), t);
			t.printStackTrace();
		}
	}
	
}
