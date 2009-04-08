package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class ClientFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GameClient gc;
	public ClientFrame(GameClient gc) {
		this.gc = gc;

		init();
		setVisible(true);
	}

	private void init() {
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				gc.stop();
				System.exit(0);
			}
		});
		setSize(100, 60);
	}
}
