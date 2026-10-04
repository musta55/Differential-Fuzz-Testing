/**
 * {@inheritDoc}
 */
@Override
public void keyReleased(KeyEvent e) {
    String fieldName = e.getComponent().getName();
    if (fieldName.equals(ProxyControlGui.PREFIX_HTTP_SAMPLER_NAME)) {
        recorderGui.setPrefixHTTPSampleName(prefixHTTPSampleName.getText());
    } else if (fieldName.equals(ProxyControlGui.HTTP_SAMPLER_NAME_FORMAT)) {
        recorderGui.setSampleNameFormat(sampleNameFormat.getText());
    } else if (fieldName.equals(ProxyControlGui.PROXY_PAUSE_HTTP_SAMPLER)) {
        try {
            Long.parseLong(proxyPauseHTTPSample.getText());
        } catch (NumberFormatException nfe) {
            int length = proxyPauseHTTPSample.getText().length();
            if (length > 0) {
                // $NON-NLS-1$
                JOptionPane.// $NON-NLS-1$
                showMessageDialog(// $NON-NLS-1$
                this, // $NON-NLS-1$
                JMeterUtils.getResString("proxy_settings_pause_error_digits"), // $NON-NLS-1$
                JMeterUtils.getResString("proxy_settings_pause_error_invalid_data"), JOptionPane.WARNING_MESSAGE);
                // Drop the last character:
                proxyPauseHTTPSample.setText(proxyPauseHTTPSample.getText().substring(0, length - 1));
            }
        }
        recorderGui.setProxyPauseHTTPSample(proxyPauseHTTPSample.getText());
        recorderGui.enableRestart();
    }
}