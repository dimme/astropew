package client.command;

public class Message implements Comparable<Message> {
	public final String msg;
	public final float time;
	
	public Message(String msg, float time) {
		this.msg = msg;
		this.time = time;
	}
	
	public int compareTo(Message m) {
		if (m.time == time) {
			return 0;
		}
		return time > m.time ? 1 : -1;
	}
	
	public boolean equals(Object rhs) {
		if (rhs instanceof Message) {
			Message m = (Message)rhs;
			return (time == m.time && msg.equals(m.msg));
		}
		return false;
	}
	
	
}
