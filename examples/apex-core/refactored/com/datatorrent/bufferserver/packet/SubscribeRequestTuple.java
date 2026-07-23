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
package com.datatorrent.bufferserver.packet;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.bufferserver.util.Codec;
import com.datatorrent.netlet.util.VarInt;

/**
 * <p>SubscribeRequestTuple class.</p>
 *
 * @since 0.3.2
 */
public class SubscribeRequestTuple extends RequestTuple
{
  public static final String EMPTY_STRING = new String();
  private String version;
  private String identifier;
  private int baseSeconds;
  private int windowId;
  private String streamType;
  private String upstreamIdentifier;
  private int mask;
  private int[] partitions;
  private int bufferSize;

  @Override
  public MessageType getType()
  {
    return MessageType.SUBSCRIBER_REQUEST;
  }

  @Override
  public void parse()
  {
    parsed = true;
    try {
      version = readString();
      identifier = readString();
      baseSeconds = readVarInt();
      windowId = readVarInt();
      streamType = readString();
      upstreamIdentifier = readString();
      readPartitions();
      bufferSize = readVarInt();
      if (bufferSize == -1) {
        return;
      }
      valid = true;
    } catch (NumberFormatException nfe) {
      logger.warn("Unparseable Tuple", nfe);
    }
  }

  private String readString() {
    int idlen = readVarInt();
    if (idlen > 0) {
      String result = new String(buffer, offset, idlen);
      offset += idlen;
      return result;
    } else if (idlen == 0) {
      return EMPTY_STRING;
    } else {
      return null;
    }
  }

  private void readPartitions() {
    int count = readVarInt();
    if (count > 0) {
      mask = readVarInt();
      if (mask <= 0) {
        return;
      }
      partitions = new int[count];
      for (int i = 0; i < count; i++) {
        partitions[i] = readVarInt();
        if (partitions[i] == -1) {
          return;
        }
      }
    }
  }

  public boolean isParsed()
  {
    return parsed;
  }

  public String getStreamType()
  {
    return streamType;
  }

  public SubscribeRequestTuple(byte[] array, int offset, int length)
  {
    super(array, offset, length);
  }

  @Override
  public int getWindowId()
  {
    return windowId;
  }

  @Override
  public int getBaseSeconds()
  {
    return baseSeconds;
  }

  /**
   * @return the version
   */
  @Override
  public String getVersion()
  {
    return version;
  }

  /**
   * @return the identifier
   */
  @Override
  public String getIdentifier()
  {
    return identifier;
  }

  /**
   * @return the upstreamIdentifier
   */
  public String getUpstreamIdentifier()
  {
    return upstreamIdentifier;
  }

  /**
   * @return the mask
   */
  public int getMask()
  {
    return mask;
  }

  /**
   * @return the partitions
   */
  @SuppressWarnings(value = "ReturnOfCollectionOrArrayField")
  public int[] getPartitions()
  {
    return partitions;
  }

  public int getBufferSize()
  {
    return bufferSize;
  }

  public static byte[] getSerializedRequest(final String version, final String id, final String down_type,
      final String upstream_id, final int mask, final Collection<Integer> partitions, final long startingWindowId,
      final int bufferSize)
  {
    byte[] array = new byte[4096];
    int offset = 0;

    /* write the type */
    array[offset++] = MessageType.SUBSCRIBER_REQUEST_VALUE;

    /* write the version */
    offset = Tuple.writeString(version == null ? CLASSIC_VERSION : version, array, offset);

    /* write the identifier */
    offset = Tuple.writeString(id, array, offset);

    /* write the baseSeconds */
    int baseSeconds = (int)(startingWindowId >> 32);
    offset = VarInt.write(baseSeconds, array, offset);

    /* write the windowId */
    int windowId = (int)startingWindowId;
    offset = VarInt.write(windowId, array, offset);

    /* write the type */
    offset = Tuple.writeString(down_type, array, offset);

    /* write upstream identifier */
    offset = Tuple.writeString(upstream_id, array, offset);

    /* write the partitions */
    if (partitions == null || partitions.isEmpty()) {
      offset = VarInt.write(0, array, offset);
    } else {
      offset = VarInt.write(partitions.size(), array, offset);
      offset = VarInt.write(mask, array, offset);
      for (int i : partitions) {
        offset = VarInt.write(i, array, offset);
      }
    }

    /* write the buffer size */
    offset = VarInt.write(bufferSize, array, offset);

    return Arrays.copyOfRange(array, 0, offset);
  }

  @Override
  public String toString()
  {
    return "SubscribeRequestTuple{" + "version=" + version + ", identifier=" + identifier +
        ", windowId=" + Codec.getStringWindowId((long)baseSeconds << 32 | windowId) + ", type=" + streamType +
        ", upstreamIdentifier=" + upstreamIdentifier + ", mask=" + mask +
        ", partitions=" + (partitions == null ? "null" : Arrays.toString(partitions)) +
        ", bufferSize=" + bufferSize + '}';
  }

  private static final Logger logger = LoggerFactory.getLogger(SubscribeRequestTuple.class);
}