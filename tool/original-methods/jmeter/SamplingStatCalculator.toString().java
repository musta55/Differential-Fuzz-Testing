@Override
public String toString() {
    return "Samples: " + this.getCount() + "  " + "Avg: " + this.getMean() + "  " + "Min: " + this.getMin() + "  " + "Max: " + this.getMax() + "  " + "Error Rate: " + this.getErrorPercentage() + "  " + "Sample Rate: " + this.getRate();
}