package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class ClientFrame extends JFrame {
	GameClient jc;
	public ClientFrame(GameClient jc) {
		this.jc = jc;

		init();
		setVisible(true);
	}

	private void init() {
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				//jc.stop();
				System.exit(0);
			}
		});
		setSize(800, 600);
	}
}
