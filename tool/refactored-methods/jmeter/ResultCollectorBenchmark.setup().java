@Setup
public void setup() {
    collector = new ResultCollector();
    collector.setName("Sample collector");
    collector.setComment("Comment");
    collector.setRunningVersion(true);
}