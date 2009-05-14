package client;

import java.awt.Font;
import java.util.Iterator;
import java.util.TreeSet;

import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;
import common.world.Ship;

import client.command.Message;
import client.world.NodeThatCanRemoveAllChildren;

public class MessageBox extends TextBox {

	private final TreeSet<Message> messages = new TreeSet<Message>();
	private static final int MAX_NUM_MESSAGES = 5;
	
	public MessageBox(String name, float xbase, float ybase) {
		super(name, xbase, ybase);
	}
	
	public void addMessage(Message msg) {
		messages.add(msg);
		while(messages.size() > MAX_NUM_MESSAGES) {
			messages.remove(messages.first());
		}
		
		String[] lines = new String[messages.size()];
		int i = 0;
		Iterator<Message> it = messages.descendingIterator();
		while(it.hasNext()) {
			lines[i++] = it.next().msg;
		}
		updateText(lines);
		System.out.println(msg.msg);
	}
	
}
