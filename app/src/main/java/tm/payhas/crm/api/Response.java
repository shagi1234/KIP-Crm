package tm.payhas.crm.api;

public class Response {
    int statusCode;
    boolean success;
    String message;

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return success;
    }
}
