@Override
public void flush(final int writeOffset) {
    flush: do {
        while (size == 0) {
            if (writeOffset - processingOffset >= 2) {
                size = last.data[processingOffset];
                size |= (last.data[processingOffset + 1] << 8);
                //          logger.debug("read item = {} of size = {} at offset = {}", item++, size, processingOffset);
            } else {
                if (writeOffset == last.data.length) {
                    processingOffset = 0;
                    size = 0;
                }
                break flush;
            }
        }
        processingOffset += 2;
        if (processingOffset + size <= writeOffset) {
            switch(last.data[processingOffset]) {
                case MessageType.BEGIN_WINDOW_VALUE:
                    Tuple btw = Tuple.getTuple(last.data, processingOffset, size);
                    if (last.starting_window == -1) {
                        last.starting_window = baseSeconds | btw.getWindowId();
                        last.ending_window = last.starting_window;
                    } else {
                        last.ending_window = baseSeconds | btw.getWindowId();
                    }
                    break;
                case MessageType.RESET_WINDOW_VALUE:
                    Tuple rwt = Tuple.getTuple(last.data, processingOffset, size);
                    baseSeconds = (long) rwt.getBaseSeconds() << 32;
                    break;
                default:
                    break;
            }
            processingOffset += size;
            size = 0;
        } else {
            if (writeOffset == last.data.length) {
                processingOffset = 0;
                size = 0;
            }
            break;
        }
    } while (true);
    last.writingOffset = writeOffset;
    notifyListeners();
}