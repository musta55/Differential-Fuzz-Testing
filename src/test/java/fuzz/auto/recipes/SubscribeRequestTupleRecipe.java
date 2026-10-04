package fuzz.auto.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * SubscribeRequestTuple(byte[] array, int offset, int length) parses the buffer in its
 * constructor, so offset and length must fit the array. Built independently from fuzz bytes they
 * almost never do, and the receiver was never built. This builds a well-formed request with the
 * class's own serializer (identical on both sides) from fuzzed field values.
 */
final class SubscribeRequestTupleRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    String version = data.consumeAsciiString(16);
    String id = data.consumeAsciiString(16);
    String downType = data.consumeAsciiString(16);
    String upstreamId = data.consumeAsciiString(16);
    int mask = data.consumeInt();
    List<Integer> partitions = new ArrayList<>();
    int n = data.consumeInt(0, 4);
    for (int i = 0; i < n; i++) {
      partitions.add(data.consumeInt());
    }
    long startingWindowId = data.consumeLong();
    int bufferSize = data.consumeInt();

    byte[] bytes = (byte[]) type.getMethod("getSerializedRequest", String.class, String.class,
        String.class, String.class, int.class, Collection.class, long.class, int.class)
        .invoke(null, version, id, downType, upstreamId, mask, partitions, startingWindowId,
            bufferSize);
    return type.getConstructor(byte[].class, int.class, int.class)
        .newInstance(bytes, 0, bytes.length);
  }
}
