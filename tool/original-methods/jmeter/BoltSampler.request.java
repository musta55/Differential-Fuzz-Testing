private String request() {
    StringBuilder request = new StringBuilder();
    request.append("Query: \n").append(getCypher()).append("\n").append("Parameters: \n").append(getParams()).append("\n").append("Database: \n").append(getDatabase()).append("\n").append("Access Mode: \n").append(getAccessMode());
    return request.toString();
}