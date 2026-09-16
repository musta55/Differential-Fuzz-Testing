public FolderLiteral(boolean virtual) {
    if (virtual) {
        this.name = null;
    } else {
        this.name = "";
    }
    final String customDefaultFolderNameBuilderClassName = JsfBaseConfig.ViewConfigCustomization.CUSTOM_DEFAULT_FOLDER_NAME_BUILDER;
    if (customDefaultFolderNameBuilderClassName != null) {
        this.folderNameBuilder = ClassUtils.tryToLoadClassForName(customDefaultFolderNameBuilderClassName);
    } else {
        this.folderNameBuilder = DefaultFolderNameBuilder.class;
    }
}