package org.apache.deltaspike.core.impl.crypto;

/**
 * Command Line Interface for CipherService
 */
public class CipherCli
{

    private CipherCli()
    {
        // private ct.
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length < 5)
        {
            printHelp();
            System.exit(-1);
        }

        if (!"encode".equals(args[0]))
        {
            printHelp();
            System.exit(-1);
        }

        CommandLineArguments arguments = parseCommandLineArguments(args);

        DefaultCipherService defaultCipherService = new DefaultCipherService();

        if (arguments.masterPwd != null && arguments.masterSalt != null)
        {
            String masterSaltHash = defaultCipherService.setMasterHash(arguments.masterPwd, arguments.masterSalt, arguments.overwrite);
            System.out.println("A new master password got set. Hash key is " + masterSaltHash);
        }
        else if (arguments.plaintext != null && arguments.masterSalt != null)
        {
            String encrypted = defaultCipherService.encrypt(arguments.plaintext, arguments.masterSalt);
            System.out.println("Encrypted value: " + encrypted);
        }
        else
        {
            printHelp();
            System.exit(-1);
        }
    }

    private static CommandLineArguments parseCommandLineArguments(String[] args)
    {
        String masterPwd = null;
        String plaintext = null;
        String masterSalt = null;
        boolean overwrite = false;

        for (int i = 1; i < args.length; i++)
        {
            String arg = args[i];

            if ("-masterPassword".equals(arg) && i < args.length - 1)
            {
                masterPwd = args[++i];
            }
            else if ("-masterSalt".equals(arg) && i < args.length - 1)
            {
                masterSalt = args[++i];
            }
            else if ("-plaintext".equals(arg) && i < args.length - 1)
            {
                plaintext = args[++i];
            }
            else if ("-overwrite".equals(arg))
            {
                overwrite = true;
            }
        }

        return new CommandLineArguments(masterPwd, plaintext, masterSalt, overwrite);
    }

    private static void printHelp()
    {
        StringBuilder usage = new StringBuilder(1024);
        usage.append("To create a master password use:");
        usage.append("\n$> java -jar deltaspike-core-impl.jar encode -masterPassword " +
            "yourMasterPassword -masterSalt someSecretOnlyKnownToYouAndTheApplication");
        usage.append("\n   you can also specify -overwrite to replace an existing masterpassword.");
        usage.append("\n\nFor encrypting a secret with a previously stored masterPassword use:");
        usage.append("\n$> java -jar deltaspike-core-impl.jar encode -plaintext plaintextToEncrypt " +
            "-masterSalt someSecretOnlyKnownToYouAndTheApplication");
        usage.append("\n\nVisit https://deltaspike.apache.org for more information.\n");

        System.out.print(usage.toString());
    }

    private static class CommandLineArguments
    {
        String masterPwd;
        String plaintext;
        String masterSalt;
        boolean overwrite;

        CommandLineArguments(String masterPwd, String plaintext, String masterSalt, boolean overwrite)
        {
            this.masterPwd = masterPwd;
            this.plaintext = plaintext;
            this.masterSalt = masterSalt;
            this.overwrite = overwrite;
        }
    }
}