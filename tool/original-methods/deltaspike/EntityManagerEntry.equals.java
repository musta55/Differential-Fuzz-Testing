@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
        return false;
    }
    EntityManagerEntry that = (EntityManagerEntry) o;
    if (!qualifier.equals(that.qualifier)) {
        return false;
    }
    return true;
}