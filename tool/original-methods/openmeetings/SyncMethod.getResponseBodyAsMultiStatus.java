/**
 * Adapted from DavMethodBase to handle MultiStatus responses.
 *
 * @param response {@link HttpResponse} to be converted to {@link MultiStatus}
 * @return MultiStatus response
 * @throws DavException if the response body could not be parsed
 */
@Override
public MultiStatus getResponseBodyAsMultiStatus(HttpResponse response) throws DavException {
    if (!processedResponse) {
        processResponseBody(response);
    }
    if (multiStatus != null) {
        return multiStatus;
    } else {
        DavException dx = getResponseException(response);
        if (dx != null) {
            throw dx;
        } else {
            throw new DavException(response.getStatusLine().getStatusCode(), getMethod() + " resulted with unexpected status: " + response.getStatusLine());
        }
    }
}