@Override
public void stopConsuming() {
    for (int i = 0; i < channelsCount; i++) {
        csvWriters[i].close();
    }
    if (!getWorkingDirectory().delete()) {
        LOG.warn("Was not able to delete folder {}", getWorkingDirectory());
    }
}