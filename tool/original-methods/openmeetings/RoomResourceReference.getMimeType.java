@Override
protected String getMimeType(FileItem r) {
    String mime;
    switch(r.getType()) {
        case WML_FILE:
            mime = "application/json";
            break;
        case IMAGE:
            mime = PNG_MIME_TYPE;
            break;
        case PRESENTATION:
            mime = PNG_MIME_TYPE;
            break;
        case VIDEO:
            mime = MP4_MIME_TYPE;
            break;
        default:
            throw new RuntimeException("Not supported");
    }
    return r.isDeleted() ? PNG_MIME_TYPE : mime;
}