/**
 * Logs an error to the log file for reporting
 *
 * @param sid The SID from getSession
 * @param message The message to log
 */
@WebMethod
@POST
@Path("/report/")
@Operation(description = "Logs an error to the log file for reporting", responses = { @ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "500", description = "Error in case of invalid credentials or server error") })
public void report(@Parameter(required = true, description = "The SID of the User. This SID must be marked as Loggedin") @WebParam(name = "sid") @QueryParam("sid") String sid, @Parameter(required = true, description = "The message to log") @WebParam(name = "message") @QueryParam("message") String message) {
    if (sid == null || message == null) {
        log.error("[report] Invalid input parameters");
        return;
    }
    Sessiondata sd = check(sid);
    if (sd == null || sd.getId() == null) {
        log.error("[report] Invalid session ID");
        return;
    }
    log.error("[CLIENT MESSAGE] {}", message);
}