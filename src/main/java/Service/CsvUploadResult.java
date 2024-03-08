package Service;

import org.springframework.http.HttpStatus;

public class CsvUploadResult {

    private final HttpStatus statusCode;
    private final String message;

    public CsvUploadResult(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }



	public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}

