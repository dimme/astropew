package common.network;

public class SendWindowFullException extends NetworkException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SendWindowFullException() {
		this("Send window overflow");
	}

	public SendWindowFullException(String msg) {
		super(msg);
	}

}
