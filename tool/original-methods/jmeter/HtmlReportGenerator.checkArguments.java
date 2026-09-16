/**
 * test that all arguments are correct and send a message to the user if not
 *
 * @return whether or not the files are correct
 */
@VisibleForTesting
List<String> checkArguments() {
    List<String> errors = new ArrayList<>();
    String csvError = checkFile(new File(csvFilePath));
    if (csvError != null) {
        errors.add(JMeterUtils.getResString("generate_report_ui.csv_file") + csvError);
    }
    String userPropertiesError = checkFile(new File(userPropertiesFilePath));
    if (userPropertiesError != null) {
        errors.add(JMeterUtils.getResString("generate_report_ui.user_properties_file") + userPropertiesError);
    }
    String outputError = checkDirectory(new File(outputDirectoryPath));
    if (outputError != null) {
        errors.add(JMeterUtils.getResString("generate_report_ui.output_directory") + outputError);
    }
    return errors;
}