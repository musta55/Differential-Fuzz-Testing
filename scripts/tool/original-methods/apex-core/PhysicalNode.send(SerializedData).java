/**
 * @param d
 * @throws InterruptedException
 */
public boolean send(SerializedData d) {
    if (client.send(d.buffer, d.dataOffset, d.length - (d.dataOffset - d.offset))) {
        return true;
    }
    if (blocker == null) {
        blocker = d;
    } else if (blocker != d) {
        throw new IllegalStateException(String.format("Can't send data %s while blocker %s is pending on %s", d, blocker, this));
    }
    return false;
}