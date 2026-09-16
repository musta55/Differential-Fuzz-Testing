@Override
public IResource getResource() {
    return new ProfileImageResource();
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isRelativeUri(String uri) {
    boolean relative = true;
    try {
        relative = !URI.create(uri).isAbsolute();
    } catch (Exception e) {
        //no-op
    }
    return relative;
}

private static String generateUrl(RequestCycle rc, Long userId, String uri) {
    File img = OmFileHelper.getUserProfilePicture(userId, uri);
    return rc.urlFor(new ProfileImageResourceReference(), new PageParameters().add("id", userId).add("anticache", img.lastModified())).toString();
}

