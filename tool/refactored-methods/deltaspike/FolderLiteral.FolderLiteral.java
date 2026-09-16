public FolderLiteral(boolean virtual) {
    this.name = virtual ? null : "";
    final String customDefaultFolderNameBuilderClassName = JsfBaseConfig.ViewConfigCustomization.CUSTOM_DEFAULT_FOLDER_NAME_BUILDER;
    this.folderNameBuilder = ClassUtils.tryToLoadClassForName(customDefaultFolderNameBuilderClassName) != null ? ClassUtils.tryToLoadClassForName(customDefaultFolderNameBuilderClassName) : DefaultFolderNameBuilder.class;
}