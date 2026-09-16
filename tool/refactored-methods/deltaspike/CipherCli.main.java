public static void main(String[] args) throws Exception {
    if (args.length < 5) {
        printHelp();
        System.exit(-1);
    }
    if (!"encode".equals(args[0])) {
        printHelp();
        System.exit(-1);
    }
    CommandLineArguments arguments = parseCommandLineArguments(args);
    DefaultCipherService defaultCipherService = new DefaultCipherService();
    if (arguments.masterPwd != null && arguments.masterSalt != null) {
        String masterSaltHash = defaultCipherService.setMasterHash(arguments.masterPwd, arguments.masterSalt, arguments.overwrite);
        System.out.println("A new master password got set. Hash key is " + masterSaltHash);
    } else if (arguments.plaintext != null && arguments.masterSalt != null) {
        String encrypted = defaultCipherService.encrypt(arguments.plaintext, arguments.masterSalt);
        System.out.println("Encrypted value: " + encrypted);
    } else {
        printHelp();
        System.exit(-1);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static CommandLineArguments parseCommandLineArguments(String[] args) {
    String masterPwd = null;
    String plaintext = null;
    String masterSalt = null;
    boolean overwrite = false;
    for (int i = 1; i < args.length; i++) {
        String arg = args[i];
        if ("-masterPassword".equals(arg) && i < args.length - 1) {
            masterPwd = args[++i];
        } else if ("-masterSalt".equals(arg) && i < args.length - 1) {
            masterSalt = args[++i];
        } else if ("-plaintext".equals(arg) && i < args.length - 1) {
            plaintext = args[++i];
        } else if ("-overwrite".equals(arg)) {
            overwrite = true;
        }
    }
    return new CommandLineArguments(masterPwd, plaintext, masterSalt, overwrite);
}

