/**
 * clear all the InitialContext objects.
 */
public static void close() {
    for (Context ctx : MAP.values()) {
        try {
            ctx.close();
        } catch (NamingException e) {
            log.error(e.getMessage());
        }
    }
    MAP.clear();
    log.info("InitialContextFactory.close() called and Context instances cleaned up");
}