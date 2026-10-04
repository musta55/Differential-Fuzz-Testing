public void setTransferData(JMeterTreeNode[] nodes) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = null;
    try {
        oos = new ObjectOutputStream(bos);
        oos.writeObject(nodes);
        data = bos.toByteArray();
    } finally {
        if (oos != null) {
            try {
                oos.close();
            } catch (Exception e) {
                // NOOP
            }
        }
    }
}