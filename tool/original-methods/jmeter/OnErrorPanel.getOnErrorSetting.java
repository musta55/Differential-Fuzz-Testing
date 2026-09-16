public int getOnErrorSetting() {
    if (stopTestNowBox.isSelected()) {
        return OnErrorTestElement.ON_ERROR_STOPTEST_NOW;
    }
    if (stopTestBox.isSelected()) {
        return OnErrorTestElement.ON_ERROR_STOPTEST;
    }
    if (stopThrdBox.isSelected()) {
        return OnErrorTestElement.ON_ERROR_STOPTHREAD;
    }
    if (startNextThreadLoopBox.isSelected()) {
        return OnErrorTestElement.ON_ERROR_START_NEXT_THREAD_LOOP;
    }
    if (startNextIterationOfCurrentLoopBox.isSelected()) {
        return OnErrorTestElement.ON_ERROR_START_NEXT_ITERATION_OF_CURRENT_LOOP;
    }
    if (breakLoopBox.isSelected()) {
        return OnErrorTestElement.ON_ERROR_BREAK_CURRENT_LOOP;
    }
    // Defaults to continue
    return OnErrorTestElement.ON_ERROR_CONTINUE;
}