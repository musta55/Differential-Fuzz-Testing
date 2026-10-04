package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * PhysicalNode(WriteOnlyClient client) is a buffer-server subscriber. The generic tiers built it
 * with a null client, and PhysicalNode.hashCode() is client.hashCode(), so merely adding the node
 * to a Set threw NullPointerException inside the engine (LeastBusy/RandomOne.distribute).
 *
 * <p>This builds it around a real, unconnected WriteOnlyClient from the netlet library.
 * send() on such a client needs no socket: it queues the slice and returns true until the send
 * queue is full, then false. The queue capacity and how many slices are already queued are
 * fuzzed, so PhysicalNode.send() reaches both its "sent" path and its "blocked" path.
 */
final class PhysicalNodeRecipe implements Recipe
{
  private static final String CLIENT = "com.datatorrent.netlet.WriteOnlyClient";

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    // Same loader as the node, so the client is an instance of the parameter type on this side.
    Class<?> clientType = Class.forName(CLIENT, true, type.getClassLoader());

    int capacity = data.consumeInt(1, 8);   // send-queue slots (rounded up to a power of two)
    Object client = clientType.getConstructor(int.class, int.class).newInstance(1024, capacity);

    int alreadyQueued = data.consumeInt(0, 8);
    Method send = clientType.getMethod("send", byte[].class, int.class, int.class);
    byte[] slice = {0};
    for (int i = 0; i < alreadyQueued; i++) {
      send.invoke(client, slice, 0, slice.length);
    }

    Constructor<?> ctor = type.getConstructor(clientType);
    return ctor.newInstance(client);
  }
}
