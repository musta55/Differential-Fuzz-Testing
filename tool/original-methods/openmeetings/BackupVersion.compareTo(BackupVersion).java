@Override
public int compareTo(BackupVersion o) {
    if (o == null) {
        return 1;
    }
    if (equals(o)) {
        return 0;
    }
    if (major > o.major) {
        return 1;
    }
    if (minor > o.minor) {
        return 1;
    }
    if (micro > o.micro) {
        return 1;
    }
    return -1;
}