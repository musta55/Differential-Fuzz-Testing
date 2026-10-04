/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    //$NON-NLS-1$
    String decodeString = "";
    try {
        String rawString = ((CompoundVariable) values[0]).execute();
        decodeString = URLDecoder.decode(rawString, CHARSET_ENCODING);
    } catch (UnsupportedEncodingException uee) {
        return null;
    }
    return decodeString;
}