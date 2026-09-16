@Override
public void startConsuming() {
    if (sampleIndexer != null) {
        sampleIndexer.reset();
    }
    initProducedMetadata();
    super.startProducing();
}