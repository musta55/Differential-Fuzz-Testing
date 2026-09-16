/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    boolean executionResult;
    try {
        executionResult = this.writeToFile();
    } catch (UnsupportedCharsetException ue) {
        // NOSONAR
        executionResult = false;
        log.error("The encoding of file is not supported", ue);
    } catch (IllegalCharsetNameException ie) {
        // NOSONAR
        executionResult = false;
        log.error("The encoding of file contains illegal characters", ie);
    } catch (IOException e) {
        // NOSONAR
        executionResult = false;
        log.error("IOException occurred", e);
    }
    return String.valueOf(executionResult);
}