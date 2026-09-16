protected BasePage() {
    if (isInitComplete()) {
        if (!isInstalled() && !(this instanceof InstallWizardPage)) {
            throw new RestartResponseException(InstallWizardPage.class);
        }
    } else if (!(this instanceof NotInitedPage)) {
        throw new RestartResponseException(NotInitedPage.class);
    }
    options.put("fragmentIdentifierSuffix", "");
    options.put("keyValueDelimiter", "/");
}