@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null) {
        return false;
    }
    if (!(obj instanceof WsClient)) {
        return false;
    }
    WsClient other = (WsClient) obj;
    if (uid == null) {
        if (other.uid != null) {
            return false;
        }
    } else if (!uid.equals(other.uid)) {
        return false;
    }
    return true;
}