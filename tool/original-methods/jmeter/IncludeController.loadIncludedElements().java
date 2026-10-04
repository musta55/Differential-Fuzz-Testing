/**
 * load the included elements using SaveService
 *
 * @return tree with loaded elements
 */
protected HashTree loadIncludedElements() {
    // only try to load the JMX test plan if there is one
    final String includePath = getIncludePath();
    HashTree tree = null;
    if (includePath != null && includePath.length() > 0) {
        String fileName = PREFIX + includePath;
        try {
            File file = new File(fileName.trim());
            final String absolutePath = file.getAbsolutePath();
            log.info("loadIncludedElements -- try to load included module: {}", absolutePath);
            if (!file.exists() && !file.isAbsolute()) {
                log.info("loadIncludedElements -failed for: {}", absolutePath);
                file = new File(FileServer.getFileServer().getBaseDir(), includePath);
                if (log.isInfoEnabled()) {
                    log.info("loadIncludedElements -Attempting to read it from: {}", file.getAbsolutePath());
                }
                if (!file.canRead() || !file.isFile()) {
                    log.error("Include Controller '{}' can't load '{}' - see log for details", this.getName(), fileName);
                    throw new IOException("loadIncludedElements -failed for: " + absolutePath + " and " + file.getAbsolutePath());
                }
            }
            tree = SaveService.loadTree(file);
            // filter the tree for a TestFragment.
            tree = getProperBranch(tree);
            removeDisabledItems(tree);
            return tree;
        } catch (// Allow for missing optional jars
        NoClassDefFoundError ex) {
            String msg = "Including file \"" + fileName + "\" failed for Include Controller \"" + this.getName() + "\", missing jar file";
            log.warn(msg, ex);
            JMeterUtils.reportErrorToUser(msg + " - see log for details");
        } catch (FileNotFoundException ex) {
            String msg = "File \"" + fileName + "\" not found for Include Controller \"" + this.getName() + "\"";
            JMeterUtils.reportErrorToUser(msg + " - see log for details");
            log.warn(msg, ex);
        } catch (Exception ex) {
            String msg = "Including file \"" + fileName + "\" failed for Include Controller \"" + this.getName() + "\", unexpected error";
            JMeterUtils.reportErrorToUser(msg + " - see log for details");
            log.warn(msg, ex);
        }
    }
    return tree;
}