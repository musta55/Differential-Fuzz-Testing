/**
 * Remove client from clients
 * @param client {@link Closeable}
 */
public static void removeClient(Closeable client) {
    CLIENTS.remove(client);
}