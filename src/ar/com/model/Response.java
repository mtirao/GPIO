package ar.com.model;

public class Response {
	
	private final int responseCode;
	private final String message;
	private final Boolean high;
	
	public Response(int responseCode, String message) {
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.high = null;
	}
	
	public Response(int responseCode, String message, Boolean high) {
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.high = high;
	}
	
	@Override
	public String toString() {
		return "Response [responseCode=" + responseCode + ", message=" + message + ", high=" + high + "]";
	}
	

}
