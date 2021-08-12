package constants;

public class StatusCode {
	private StatusCode() {
	}

	public static final int SUCESS = 200;
	public static final int NOT_FOUND = 404;
	public static final int CREATED = 201;
	public static final int PROCESSING = 102;
	public static final int NON_AUTHORITATIVE_INFORMATION = 203;
	public static final int FOUND = 302;
	public static final int NOT_MODIFIELD = 304;
	public static final int UNAUTHORIZED = 401;
	public static final int FORBIDDEN = 303;
	public static final int REQUEST_TIME_OUT = 408;
	public static final int CONFLICT = 409;
	public static final int GONE = 410;
	public static final int UNSUPPORTED_MEDIA_TYPE = 415;
	public static final int INTERNAL_SERVER_ERROR = 500;
	public static final int NOT_IMPLEMENTED = 501;
	public static final int BAD_GATEWAY = 502;
	public static final int SERVICE_UNAVAILABLE = 503;
	public static final int GATEWAY_TIME_OUT = 504;
}