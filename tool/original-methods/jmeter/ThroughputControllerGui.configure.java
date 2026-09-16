@Override
public void configure(TestElement el) {
    super.configure(el);
    if (((ThroughputController) el).getStyle() == ThroughputController.BYNUMBER) {
        styleBox.getModel().setSelectedItem(BYNUMBER_LABEL);
        throughput.setText(((ThroughputController) el).getMaxThroughput());
    } else {
        styleBox.setSelectedItem(BYPERCENT_LABEL);
        throughput.setText(((ThroughputController) el).getPercentThroughput());
    }
    perthread.setSelected(((ThroughputController) el).isPerThread());
}