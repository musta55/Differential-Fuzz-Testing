@Override
public void initFilter(FilterContainer container, Configuration conf) {
    logger.debug("Conf {}", conf);
    Map<String, String> params = new HashMap<>();
    Collection<String> proxies = new ArrayList<>();
    if (ConfigUtils.isRMHAEnabled(conf)) {
        // HA is enabled get all
        for (String rmId : ConfigUtils.getRMHAIds(conf)) {
            proxies.add(getResolvedRMWebAppURLWithoutScheme(conf, rmId));
        }
        logger.info("HA proxy addresses {}", proxies);
    }
    if (proxies.isEmpty()) {
        proxies.add(getProxyHostAndPort(conf));
        logger.info("Proxy addresses {}", proxies);
    }
    StringBuilder proxyBr = new StringBuilder();
    for (String proxy : proxies) {
        if (proxyBr.length() != 0) {
            proxyBr.append(StramWSFilter.PROXY_DELIMITER);
        }
        String[] parts = proxy.split(":");
        proxyBr.append(parts[0]);
    }
    params.put(StramWSFilter.PROXY_HOST, proxyBr.toString());
    container.addFilter(FILTER_NAME, FILTER_CLASS, params);
}