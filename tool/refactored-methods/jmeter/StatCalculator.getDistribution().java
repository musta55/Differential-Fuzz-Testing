/**
 * Returns the distribution of the values in the list.
 *
 * @return map containing either Integer or Long keys; entries are a Number array containing the key and the [Integer] count.
 * TODO - why is the key value also stored in the entry array? See Bug 53825
 */
public Map<Number, Number[]> getDistribution() {
    Map<Number, Number[]> items = new HashMap<>();
    for (Map.Entry<T, MutableLong> entry : valuesMap.entrySet()) {
        items.put(entry.getKey(), new Number[] { entry.getKey(), entry.getValue() });
    }
    return items;
}