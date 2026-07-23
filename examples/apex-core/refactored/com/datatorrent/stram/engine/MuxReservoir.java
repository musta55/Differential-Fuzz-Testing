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
package com.datatorrent.stram.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.api.Sink;
import com.datatorrent.netlet.util.CircularBuffer;
import com.datatorrent.stram.tuple.Tuple;

/**
 * <p>Abstract MuxReservoir class.</p>
 *
 * @since 0.3.2
 */
public abstract class MuxReservoir
{
  @SuppressWarnings("VolatileArrayField")
  private volatile SubReservoir[] reservoirs = new SubReservoir[0];
  private HashMap<String, SubReservoir> reservoirMap = new HashMap<>();

  public SweepableReservoir acquireReservoir(String id, int capacity)
  {
    SubReservoir r = reservoirMap.get(id);
    if (r == null) {
      reservoirMap.put(id, r = new SubReservoir(capacity));
      resizeReservoirs(reservoirs.length + 1, r);
    }

    return r;
  }

  public SweepableReservoir releaseReservoir(String id)
  {
    SubReservoir r = reservoirMap.remove(id);
    if (r != null) {
      resizeReservoirs(reservoirs.length - 1, r);
    }

    return r;
  }

  private void resizeReservoirs(int newSize, SubReservoir exclude)
  {
    SubReservoir[] newReservoirs = new SubReservoir[newSize];
    int j = 0;
    for (SubReservoir reservoir : reservoirs) {
      if (reservoir != exclude) {
        newReservoirs[j++] = reservoir;
      }
    }
    if (exclude != null) {
      newReservoirs[newSize - 1] = exclude;
    }
    reservoirs = newReservoirs;
  }

  protected abstract Queue getQueue();

  class SubReservoir extends CircularBuffer<Object> implements SweepableReservoir
  {
    int count;
    private Sink<Object> sink;

    SubReservoir(int capacity)
    {
      super(capacity);
    }

    @Override
    public int size(final boolean dataTupleAware)
    {
      int size = super.size();
      if (dataTupleAware) {
        size -= countTuples(getFrozenIterator());
      }
      return size;
    }

    private int countTuples(Iterator<Object> iterator)
    {
      int tupleCount = 0;
      while (iterator.hasNext()) {
        if (iterator.next() instanceof Tuple) {
          tupleCount++;
        }
      }
      return tupleCount;
    }

    @Override
    public Sink<Object> setSink(Sink<Object> sink)
    {
      this.sink = sink;
      return sink;
    }

    @Override
    public Tuple sweep()
    {
      int size = size();
      if (size > 0) {
        return processTuples(size);
      }

      return processQueue();
    }

    private Tuple processTuples(int size)
    {
      for (int i = 0; i < size; i++) {
        if (peekUnsafe() instanceof Tuple) {
          count += i;
          return (Tuple) peekUnsafe();
        }
        sink.put(pollUnsafe());
      }
      count += size;
      return null;
    }

    private Tuple processQueue()
    {
      Queue queue = getQueue();
      synchronized (queue) {
        if (queue.isEmpty()) {
          return null;
        }

        int minCapacity = findMinRemainingCapacity();
        return distributeFromQueue(queue, minCapacity);
      }
    }

    private int findMinRemainingCapacity()
    {
      int min = Integer.MAX_VALUE;
      for (SubReservoir reservoir : reservoirs) {
        if (reservoir.remainingCapacity() < min) {
          min = reservoir.remainingCapacity();
        }
      }
      return min;
    }

    private Tuple distributeFromQueue(Queue queue, int minCapacity)
    {
      while (minCapacity-- > 0) {
        Object o = queue.poll();
        if (o == null) {
          break;
        }
        for (SubReservoir reservoir : reservoirs) {
          reservoir.add(o);
        }
      }
      return null;
    }

    @Override
    public int getCount(boolean reset)
    {
      int currentCount = count;
      if (reset) {
        count = 0;
      }
      return currentCount;
    }

  }

  private static final Logger logger = LoggerFactory.getLogger(MuxReservoir.class);
}