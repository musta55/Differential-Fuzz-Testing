// H5 format write/read steps:
// 1. Create/Open a File (H5Fcreate)
// 2. Create/open a Dataspace
// 3. Create/Open a Dataset
// 4. Write/Read
// 5. Close File
public static H5RootObject H5Fopen(H5ByteReader reader) {
    H5RootObject rootObject = new H5RootObject();
    try {
        // Find out if the file is a HDF5 file
        int maxSignatureLength = 2048;
        boolean validSignature = false;
        long offset;
        for (offset = 0; offset < maxSignatureLength; offset = nextOffset(offset)) {
            validSignature = H5Superblock.verifySignature(reader, offset);
            if (validSignature) {
                break;
            }
        }
        if (!validSignature) {
            throw new H5RuntimeException("No valid HDF5 signature found");
        }
        rootObject.setByteReader(reader);
        final H5Superblock superblock = new H5Superblock(reader, offset);
        rootObject.setSuperblock(superblock);
    } catch (Exception exception) {
        throw new H5RuntimeException("Can't open fine " + exception);
    }
    return rootObject;
}