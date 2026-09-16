/**
 * Remove publisher from clients
 * @param publisher {@link Publisher}
 */
public static void removeClient(Publisher publisher) {
    CLIENTS.remove(publisher);
}