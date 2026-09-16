@Override
public void modifyTestElement(TestElement sampler) {
    super.configureTestElement(sampler);
    SystemSampler systemSampler = (SystemSampler) sampler;
    systemSampler.setCheckReturnCode(checkReturnCode.isSelected());
    if (checkReturnCode.isSelected()) {
        if (!StringUtils.isEmpty(desiredReturnCode.getText())) {
            systemSampler.setExpectedReturnCode(Integer.parseInt(desiredReturnCode.getText()));
        } else {
            systemSampler.setExpectedReturnCode(SystemSampler.DEFAULT_RETURN_CODE);
        }
    } else {
        systemSampler.setExpectedReturnCode(SystemSampler.DEFAULT_RETURN_CODE);
    }
    systemSampler.setCommand(command.getFilename());
    systemSampler.setArguments((Arguments) argsPanel.createTestElement());
    systemSampler.setEnvironmentVariables((Arguments) envPanel.createTestElement());
    systemSampler.setDirectory(directory.getFilename());
    systemSampler.setStdin(stdin.getFilename());
    systemSampler.setStdout(stdout.getFilename());
    systemSampler.setStderr(stderr.getFilename());
    if (!StringUtils.isEmpty(timeout.getText())) {
        try {
            systemSampler.setTimout(Long.parseLong(timeout.getText()));
        } catch (NumberFormatException e) {
            log.error("Error parsing timeout field value:" + timeout.getText(), e);
        }
    }
}