/**
 * Read text from file, eventually replace variables, then convert it.
 * Cached content depends if variabilisation is active or not.
 *
 * @param filename
 *            name of the file to get the value from
 * @param encoding
 *            encoding of the file
 * @param hasVariable
 *            flag, whether variables inside the value should be replaced
 * @param cache
 *            Cache in which the raw values will be stored/read from
 * @return the constructed object
 */
T getValueFromFile(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache);