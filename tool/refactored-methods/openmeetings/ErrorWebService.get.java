/**
 * loads an Error-Object. If a Method returns a negative Result, its an
 * Error-id, it needs a languageId to specify in which language you want to
 * display/read the error-message. English has the Language-ID one, for
 * different one see the list of languages
 *
 * @param key
 *            the error key for ex. `error.unknown`
 * @param lang
 *            The id of the language
 *
 * @return - error with the code given
 */
@WebMethod
@GET
@Path("/{key}/{lang}")
@Operation(description = """
    Loads an Error-Object. If a Method returns a negative Result, its an
     Error-id, it needs a languageId to specify in which language you want to
     display/read the error-message. English has the Language-ID one, for
     different one see the list of languages""", responses = { @ApiResponse(responseCode = "200", description = "error with the code given", content = @Content(schema = @Schema(implementation = ServiceResultWrapper.class))), @ApiResponse(responseCode = "500", description = "Server error") })
public ServiceResult get(@Parameter(required = true, description = "the error key for ex. `error.unknown`") @WebParam(name = "key") @PathParam("key") String key, @Parameter(required = true, description = "The id of the language") @WebParam(name = "lang") @PathParam("lang") long lang) {
    try {
        if (key == null || lang <= 0) {
            return new ServiceResult("Invalid input parameters", Type.ERROR);
        }
        String eValue = LabelDao.getString(key, lang);
        if (eValue == null) {
            return new ServiceResult("Error key not found", Type.ERROR);
        }
        return new ServiceResult(eValue, Type.SUCCESS);
    } catch (Exception err) {
        log.error("[get] ", err);
        return new ServiceResult("Internal server error", Type.ERROR);
    }
}