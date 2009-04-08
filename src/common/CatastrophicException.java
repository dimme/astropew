package common;

public class CatastrophicException extends GameException{
	private static final long serialVersionUID = 1L;
	public CatastrophicException(Throwable cause) {
		super(cause);
	}
}
