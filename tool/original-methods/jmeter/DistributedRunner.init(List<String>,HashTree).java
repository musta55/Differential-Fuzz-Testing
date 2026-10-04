public void init(List<String> addresses, HashTree tree) {
    // converting list into mutable version
    List<String> addrs = new ArrayList<>(addresses);
    for (int tryNo = 0; tryNo < retriesNumber; tryNo++) {
        if (tryNo > 0) {
            println("Following remote engines will retry configuring: " + addrs + ", pausing before retry for " + retriesDelay + "ms");
            try {
                Thread.sleep(retriesDelay);
            } catch (InterruptedException e) {
                // NOSONAR
                throw new IllegalStateException("Interrupted while initializing remote engines:" + addrs, e);
            }
        }
        int idx = 0;
        while (idx < addrs.size()) {
            String address = addrs.get(idx);
            println("Configuring remote engine: " + address);
            JMeterEngine engine = getClientEngine(address.trim(), tree);
            if (engine != null) {
                engines.put(address, engine);
                addrs.remove(address);
            } else {
                println("Failed to configure " + address);
                idx++;
            }
        }
        if (addrs.isEmpty()) {
            break;
        }
    }
    if (!addrs.isEmpty()) {
        String msg = "Following remote engines could not be configured:" + addrs;
        if (!continueOnFail || engines.isEmpty()) {
            stop();
            // NOSONAR
            throw new RuntimeException(msg);
        } else {
            println(msg);
            println("Continuing without failed engines...");
        }
    }
}