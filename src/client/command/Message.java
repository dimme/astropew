package client.command;

public class Message implements Comparable<Message> {
	public final String msg;
	public float time;

	public Message(String msg, float time) {
		this.msg = msg;
		this.time = time;
	}

	public int compareTo(Message m) {
		if (m.time == time) {
			return msg.compareTo(m.msg);
		}
		return time > m.time ? 1 : -1;
	}

	public boolean equals(Object rhs) {
		if (rhs instanceof Message) {
			return compareTo((Message)rhs) == 0;
		}
		return false;
	}


}
