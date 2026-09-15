@Override
public void flush(final int writeOffset) {
    flush: do {
        while (size == 0) {
            if (canReadSize(writeOffset)) {
                readSize(writeOffset);
            } else {
                handleWriteOffsetAtEnd(writeOffset);
                break flush;
            }
        }
        processingOffset += 2;
        if (canProcessCurrentItem(writeOffset)) {
            processCurrentItem();
            processingOffset += size;
            size = 0;
        } else {
            handleWriteOffsetAtEnd(writeOffset);
            break;
        }
    } while (true);
    last.writingOffset = writeOffset;
    notifyListeners();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean canReadSize(int writeOffset) {
    return writeOffset - processingOffset >= 2;
}

private void readSize(int writeOffset) {
    size = last.data[processingOffset];
    size |= (last.data[processingOffset + 1] << 8);
}

private void handleWriteOffsetAtEnd(int writeOffset) {
    if (writeOffset == last.data.length) {
        processingOffset = 0;
        size = 0;
    }
}

private boolean canProcessCurrentItem(int writeOffset) {
    return processingOffset + size <= writeOffset;
}

private void processCurrentItem() {
    switch(last.data[processingOffset]) {
        case MessageType.BEGIN_WINDOW_VALUE:
            Tuple btw = Tuple.getTuple(last.data, processingOffset, size);
            updateWindowBounds(btw);
            break;
        case MessageType.RESET_WINDOW_VALUE:
            Tuple rwt = Tuple.getTuple(last.data, processingOffset, size);
            baseSeconds = (long) rwt.getBaseSeconds() << 32;
            break;
        default:
            break;
    }
}

private void updateWindowBounds(Tuple btw) {
    if (last.starting_window == -1) {
        last.starting_window = baseSeconds | btw.getWindowId();
        last.ending_window = last.starting_window;
    } else {
        last.ending_window = baseSeconds | btw.getWindowId();
    }
}

