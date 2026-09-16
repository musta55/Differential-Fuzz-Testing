@Setup
public void setup() {
    collector = new ResultCollector();
    collector.setName("Sample colelctor");
    collector.setComment("Comment");
    collector.setRunningVersion(true);
}