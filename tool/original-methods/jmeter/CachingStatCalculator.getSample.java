public Sample getSample(int index) {
    synchronized (storedValues) {
        if (index < storedValues.size()) {
            return storedValues.get(index);
        }
    }
    return null;
}