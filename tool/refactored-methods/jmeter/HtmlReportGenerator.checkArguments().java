/**
 * Test that all arguments are correct and send a message to the user if not
 *
 * @return whether or not the files are correct
 */
@VisibleForTesting
List<String> checkArguments() {
    List<String> errors = new ArrayList<>();
    String csvError = checkFileExistsAndReadable(new File(csvFilePath));
    if (csvError != null) {
        errors.add(JMeterUtils.getResString("generate_report_ui.csv_file") + csvError);
    }
    String userPropertiesError = checkFileExistsAndReadable(new File(userPropertiesFilePath));
    if (userPropertiesError != null) {
        errors.add(JMeterUtils.getResString("generate_report_ui.user_properties_file") + userPropertiesError);
    }
    String outputError = checkDirectoryExistsWritableAndEmpty(new File(outputDirectoryPath));
    if (outputError != null) {
        errors.add(JMeterUtils.getResString("generate_report_ui.output_directory") + outputError);
    }
    return errors;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Check if a file exists and is readable
 *
 * @param file the file to check
 * @return the error message or null if the file is ok
 */
private static String checkFileExistsAndReadable(File file) {
    if (file.exists() && file.canRead() && file.isFile()) {
        return null;
    } else {
        return MessageFormat.format(JMeterUtils.getResString(NO_FILE), file);
    }
}

/**
 * Check if a directory exists, is writable, and is empty
 *
 * @param directory the directory to check
 * @return the error message or null if the directory is ok
 */
private static String checkDirectoryExistsWritableAndEmpty(File directory) {
    if (directory.exists()) {
        return checkIfDirectoryIsEmpty(directory);
    } else {
        return checkIfParentDirectoryWritableAndCreate(directory);
    }
}

/**
 * Check if a directory is empty
 *
 * @param directory the directory to check
 * @return the error message or null if the directory is empty
 */
private static String checkIfDirectoryIsEmpty(File directory) {
    String[] files = directory.list();
    if (files != null && files.length > 0) {
        return MessageFormat.format(JMeterUtils.getResString(NOT_EMPTY_DIRECTORY), directory);
    } else {
        return null;
    }
}

/**
 * Check if the parent directory is writable and create the directory
 *
 * @param directory the directory to check and create
 * @return the error message or null if the directory is created successfully
 */
private static String checkIfParentDirectoryWritableAndCreate(File directory) {
    File parentDirectory = directory.getParentFile();
    if (parentDirectory != null && parentDirectory.exists() && parentDirectory.canWrite()) {
        if (directory.mkdir()) {
            return null;
        } else {
            return MessageFormat.format(JMeterUtils.getResString(CANNOT_CREATE_DIRECTORY), directory);
        }
    } else {
        return MessageFormat.format(JMeterUtils.getResString(CANNOT_CREATE_DIRECTORY), directory);
    }
}

