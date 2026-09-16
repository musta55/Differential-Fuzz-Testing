public static void main(String[] args) throws Exception {
    // not using any other libs like commons-cli to save dependencies
    if (args.length < 5) {
        printHelp();
        System.exit(-1);
    }
    if (!"encode".equals(args[0])) {
        printHelp();
        System.exit(-1);
    }
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
    DefaultCipherService defaultCipherService = new DefaultCipherService();
    if (masterPwd != null && masterSalt != null) {
        String masterSaltHash = defaultCipherService.setMasterHash(masterPwd, masterSalt, overwrite);
        System.out.println("A new master password got set. Hash key is " + masterSaltHash);
    } else if (plaintext != null && masterSalt != null) {
        String encrypted = defaultCipherService.encrypt(plaintext, masterSalt);
        System.out.println("Encrypted value: " + encrypted);
    } else {
        printHelp();
        System.exit(-1);
    }
}