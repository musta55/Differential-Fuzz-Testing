@Override
public boolean containsStrict(IColIndex a, IColIndex b) {
    if (a != null && b != null && a.size() + b.size() == size()) {
        IIterate ia = a.iterator();
        while (ia.hasNext()) {
            if (!(findIndex(ia.next()) >= 0))
                return false;
        }
        IIterate ib = b.iterator();
        while (ib.hasNext()) {
            if (!(findIndex(ib.next()) >= 0))
                return false;
        }
        return true;
    }
    return false;
}