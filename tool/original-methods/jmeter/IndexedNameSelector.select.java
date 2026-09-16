/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.csv.processor.SampleSelector#select(org.apache
     * .jmeter.report.csv.core.Sample)
     */
@Override
public Double select(Sample sample) {
    String name = sample.getName();
    int index = names.indexOf(name);
    if (index < 0) {
        names.add(name);
        index = names.size() - 1;
    }
    return (double) index;
}