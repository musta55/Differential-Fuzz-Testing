/**
 * Get a description for this filter.
 *
 * @return a description for this filter
 */
@Override
public String getDescription() {
    return "JMeter Files (" + String.join(", ", exts) + ")";
}