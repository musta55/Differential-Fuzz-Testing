private void checkFlags(final int flags) {
    int modeCount = 0;
    if ((ARGUMENT_REQUIRED & flags) == ARGUMENT_REQUIRED) {
        modeCount++;
    }
    if ((ARGUMENT_OPTIONAL & flags) == ARGUMENT_OPTIONAL) {
        modeCount++;
    }
    if ((ARGUMENT_DISALLOWED & flags) == ARGUMENT_DISALLOWED) {
        modeCount++;
    }
    if ((ARGUMENTS_REQUIRED_2 & flags) == ARGUMENTS_REQUIRED_2) {
        modeCount++;
    }
    if (0 == modeCount) {
        final String message = "No mode specified for option " + this;
        throw new IllegalStateException(message);
    } else if (1 != modeCount) {
        final String message = "Multiple modes specified for option " + this;
        throw new IllegalStateException(message);
    }
}