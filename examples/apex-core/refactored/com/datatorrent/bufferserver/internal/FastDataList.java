/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datatorrent.bufferserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.bufferserver.packet.MessageType;
import com.datatorrent.bufferserver.packet.Tuple;
import com.datatorrent.bufferserver.util.SerializedData;

/**
 * <p>FastDataList class.</p>
 *
 * @since 0.3.2
 */
public class FastDataList extends DataList
{
  public FastDataList(String identifier)
  {
    super(identifier);
  }

  public FastDataList(String identifier, int blocksize, int numberOfCacheBlocks, boolean backPressureEnabled)
  {
    super(identifier, blocksize, numberOfCacheBlocks, backPressureEnabled);
  }

  long item;

  @Override
  public void flush(final int writeOffset)
  {
    flush:
    do {
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
    switch (last.data[processingOffset]) {
      case MessageType.BEGIN_WINDOW_VALUE:
        Tuple btw = Tuple.getTuple(last.data, processingOffset, size);
        updateWindowBounds(btw);
        break;

      case MessageType.RESET_WINDOW_VALUE:
        Tuple rwt = Tuple.getTuple(last.data, processingOffset, size);
        baseSeconds = (long)rwt.getBaseSeconds() << 32;
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

  @Override
  protected FastDataListIterator getIterator(Block block)
  {
    return new FastDataListIterator(block);
  }

  /**
   * <p>FastDataListIterator class.</p>
   *
   * @since 0.3.2
   */
  protected class FastDataListIterator extends DataListIterator
  {
    FastDataListIterator(Block da)
    {
      super(da);
    }

    @Override
    public boolean hasNext()
    {
      while (size == 0) {
        if (canReadSize()) {
          readSize();
        } else {
          if (isAtEndAndSwitchToNextBlock()) {
            continue;
          } else {
            return false;
          }
        }
      }

      if (canReturnCurrentItem()) {
        setCurrentItem();
        return true;
      } else {
        if (isAtEndAndSwitchToNextBlock()) {
          nextOffset.integer = da.readingOffset;
          return hasNext();
        } else {
          return false;
        }
      }
    }

    private boolean canReadSize() {
      return da.writingOffset - readOffset >= 2;
    }

    private void readSize() {
      size = buffer[readOffset];
      size |= (buffer[readOffset + 1] << 8);
    }

    private boolean isAtEndAndSwitchToNextBlock() {
      return da.writingOffset == buffer.length && switchToNextBlock();
    }

    private boolean canReturnCurrentItem() {
      return readOffset + size + 2 <= da.writingOffset;
    }

    private void setCurrentItem() {
      current = new SerializedData(buffer, readOffset, size + 2);
      current.dataOffset = readOffset + 2;
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(FastDataList.class);
}