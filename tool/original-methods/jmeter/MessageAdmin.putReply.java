/**
 * Try to associate a reply to a previously stored request. If a matching
 * request is found, the owner of the request will be notified with the
 * registered {@link CountDownLatch}
 *
 * @param id
 *            id of the request
 * @param reply
 *            object with the reply
 */
public void putReply(String id, Message reply) {
    PlaceHolder holder = table.get(id);
    log.debug("RPL_ID [{}] for holder {}", id, holder);
    if (holder != null) {
        holder.setReply(reply);
        CountDownLatch latch = holder.getLatch();
        if (log.isDebugEnabled()) {
            log.debug("{} releasing latch : {}", Thread.currentThread().getName(), latch);
        }
        latch.countDown();
        if (log.isDebugEnabled()) {
            log.debug("{} released latch : {}", Thread.currentThread().getName(), latch);
        }
    } else {
        if (log.isDebugEnabled()) {
            log.debug("Failed to match reply: {}", reply);
        }
    }
}