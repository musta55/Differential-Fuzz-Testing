public void setTransferData(JMeterTreeNode[] nodes) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
        oos.writeObject(nodes);
        data = bos.toByteArray();
    }
}