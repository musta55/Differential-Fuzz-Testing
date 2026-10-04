public static String getUrl(RequestCycle rc, User u) {
    String uri = u.getPictureUri();
    if (isRelative(uri)) {
        File img = OmFileHelper.getUserProfilePicture(u.getId(), uri);
        uri = rc.urlFor(new ProfileImageResourceReference(), new PageParameters().add("id", u.getId()).add("anticache", img.lastModified())).toString();
    }
    return uri;
}