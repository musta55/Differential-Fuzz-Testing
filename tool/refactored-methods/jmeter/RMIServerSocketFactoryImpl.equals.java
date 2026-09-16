@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    RMIServerSocketFactoryImpl other = (RMIServerSocketFactoryImpl) obj;
    return Objects.equals(localAddress, other.localAddress);
}