package client;

import java.util.Iterator;
import java.util.TreeSet;

import client.command.Message;

public class MessageBox extends TextBox {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final TreeSet<Message> messages = new TreeSet<Message>();
	private static final int MAX_NUM_MESSAGES = 5;
	private static final float MESSAGE_TTL = 3f;

	public MessageBox(String name, float xbase, float ybase) {
		super(name, xbase, ybase);
	}

	public void addMessage(Message msg) {
		messages.add(msg);
		while(messages.size() > MAX_NUM_MESSAGES) {
			messages.remove(messages.first());
		}

		update();
		
		System.out.println(msg.msg);
	}
	
	private void update() {
		String[] lines = new String[messages.size()];
		int i = 0;
		Iterator<Message> it = messages.descendingIterator();
		while(it.hasNext()) {
			lines[i++] = it.next().msg;
		}
		updateText(lines);
	}

	public void update(float time) {
		
		Iterator<Message> it = messages.iterator();
		while (it.hasNext()) {
			final Message m = it.next();
			if (m.time+MESSAGE_TTL < time) {
				it.remove();
			}
		}
		
		update();
	}

}
