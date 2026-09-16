/**
 * @return the response code , '0' if the code is empty
 */
public String getResponseCode() {
    if (responseCode.isEmpty()) {
        return "0";
    } else {
        return responseCode.trim();
    }
}