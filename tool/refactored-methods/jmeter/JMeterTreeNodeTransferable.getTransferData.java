@Override
public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (!isDataFlavorSupported(flavor)) {
        throw new UnsupportedFlavorException(flavor);
    }
    if (data != null) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (JMeterTreeNode[]) ois.readObject();
        } catch (ClassNotFoundException cnfe) {
            throw new IOException("Failed to read object stream.", cnfe);
        }
    }
    return null;
}