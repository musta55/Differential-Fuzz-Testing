public static void H5Dwrite(H5RootObject rootObject, double[][] data) {
    for (int i = 0; i < rootObject.getRow(); i++) {
        H5Dwrite(rootObject, data[i]);
    }
}