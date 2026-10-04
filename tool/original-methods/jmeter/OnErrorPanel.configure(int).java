public void configure(int errorAction) {
    stopTestNowBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_STOPTEST_NOW);
    startNextThreadLoopBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_START_NEXT_THREAD_LOOP);
    startNextIterationOfCurrentLoopBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_START_NEXT_ITERATION_OF_CURRENT_LOOP);
    stopTestBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_STOPTEST);
    stopThrdBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_STOPTHREAD);
    continueBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_CONTINUE);
    breakLoopBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_BREAK_CURRENT_LOOP);
}