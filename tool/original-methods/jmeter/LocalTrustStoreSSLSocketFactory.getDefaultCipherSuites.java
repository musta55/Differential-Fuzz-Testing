/**
 * {@inheritDoc}
 */
@Override
public String[] getDefaultCipherSuites() {
    return factory.getSupportedCipherSuites();
}