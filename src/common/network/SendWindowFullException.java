package common.network;

public class SendWindowFullException extends NetworkException {

	public SendWindowFullException() {
		this("Send window overflow");
	}
	
	public SendWindowFullException(String msg) {
		super(msg);
	}

}
