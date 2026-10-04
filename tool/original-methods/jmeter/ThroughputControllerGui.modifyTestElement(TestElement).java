/**
 * Modifies a given TestElement to mirror the data in the gui components.
 *
 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
 */
@Override
public void modifyTestElement(TestElement tc) {
    configureTestElement(tc);
    ((ThroughputController) tc).setStyle(style);
    ((ThroughputController) tc).setPerThread(isPerThread);
    if (style == ThroughputController.BYNUMBER) {
        try {
            ((ThroughputController) tc).setMaxThroughput(Integer.parseInt(throughput.getText().trim()));
        } catch (NumberFormatException e) {
            // In case we are converting back from floating point, drop the decimal fraction
            // $NON-NLS-1$
            ((ThroughputController) tc).setMaxThroughput(throughput.getText().trim().split("\\.")[0]);
        }
    } else {
        try {
            ((ThroughputController) tc).setPercentThroughput(Float.parseFloat(throughput.getText().trim()));
        } catch (NumberFormatException e) {
            ((ThroughputController) tc).setPercentThroughput(throughput.getText());
        }
    }
}