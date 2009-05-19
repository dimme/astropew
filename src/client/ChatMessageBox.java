package client;

import java.util.List;

public class ChatMessageBox extends MessageBox {

	private static final long serialVersionUID = 1L;
	private Object writing = "";
	
	public ChatMessageBox(String name, float xbase, float ybase) {
		super(name, xbase, ybase);
	}
	
	public void setWritingObj(Object obj) {
		writing = obj;
	}

	public void updateText(List<String> lines) { 
		if (!writing.toString().equals("")) {
			lines.add(0, writing.toString());
		}
		super.updateText(lines);
	}
}
