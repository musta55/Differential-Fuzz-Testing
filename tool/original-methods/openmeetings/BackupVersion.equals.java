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
    BackupVersion other = (BackupVersion) obj;
    if (major != other.major) {
        return false;
    }
    if (micro != other.micro) {
        return false;
    }
    return minor == other.minor;
}