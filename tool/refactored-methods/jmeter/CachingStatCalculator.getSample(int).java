public Sample getSample(int index) {
    if (index < storedValues.size()) {
        synchronized (storedValues) {
            return storedValues.get(index);
        }
    }
    return null;
}