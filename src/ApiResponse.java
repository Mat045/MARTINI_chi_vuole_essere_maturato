import java.util.List;

public class ApiResponse {
    private int response_code;
    private List<ApiQuestion> results;

    public int getResponseCode() {
        return response_code;
    }

    public List<ApiQuestion> getResults() {
        return results;
    }
}
