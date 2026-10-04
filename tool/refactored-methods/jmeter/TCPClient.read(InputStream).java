/**
 * @param is -
 *            InputStream for socket
 * @return String read from socket
 * @throws ReadException exception that can contain partial response (Response until error occurred)
 * @deprecated since 3.3, implement {@link TCPClient#read(InputStream, SampleResult)} instead, will be removed in future version
 */
@Deprecated
default String read(InputStream is) throws ReadException {
    throw new UnsupportedOperationException("This method is deprecated. Use read(InputStream, SampleResult) instead.");
}