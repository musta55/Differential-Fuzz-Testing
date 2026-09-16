@Override
public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (!isDataFlavorSupported(flavor)) {
        throw new UnsupportedFlavorException(flavor);
    }
    if (data != null) {
        ObjectInput ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            JMeterTreeNode[] nodes = (JMeterTreeNode[]) ois.readObject();
            return nodes;
        } catch (ClassNotFoundException cnfe) {
            throw new IOException("Failed to read object stream.", cnfe);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception e) {
                    // NOOP
                }
            }
        }
    }
    return null;
}