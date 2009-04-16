package common;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractExecutorTask implements Runnable {

	public abstract void execute();

	public final void run() {
		try {
			execute();
		} catch (final Throwable t) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, t.getMessage(), t);
			t.printStackTrace();
		}
	}

}
