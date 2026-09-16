@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null) {
        return false;
    }
    if (getClass() != obj.getClass()) {
        return false;
    }
    RMIServerSocketFactoryImpl other = (RMIServerSocketFactoryImpl) obj;
    if (localAddress == null) {
        if (other.localAddress != null) {
            return false;
        }
    } else if (!localAddress.equals(other.localAddress)) {
        return false;
    }
    return true;
}