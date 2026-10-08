package fuzz.auto.recipes;

import java.net.InetSocketAddress;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Built from a random IP, an InetSocketAddress makes getHostName() do a reverse DNS lookup, which
 * can take seconds; RecoverableRpcProxy.toConnectURI hit Jazzer's per-input time limit that way
 * and the whole run was killed. An unresolved address never touches DNS: getHostName() just
 * returns the stored name.
 */
final class InetSocketAddressRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type)
  {
    return InetSocketAddress.createUnresolved("host" + data.consumeInt(0, 99),
        data.consumeInt(0, 65535));
  }
}
