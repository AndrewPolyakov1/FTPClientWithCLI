package com.infotec.ftp.client;

import java.net.MalformedURLException;

public class Main {
    private static final String USAGE = "Usage: java -jar ftp-client.jar <login> <password> <host> <port> <remotePath>";

    private static String LOGIN = null;
    private static String PASSWORD = null;
    private static String HOST = null;
    private static int PORT = 0;
    private static String REMOTE_PATH = null;


    public static void parseArgs(String[] args) {
        try {
            if (args.length != 5) {
                System.err.println(USAGE);
                System.exit(1);
            }
            LOGIN = args[0];
            PASSWORD = args[1];
            HOST = args[2];
            PORT = Integer.parseInt(args[3]);
            REMOTE_PATH = args[4];
        } catch (Exception e) {
            System.err.println("Error while parsing arguments: " + e.getMessage());
            System.err.println(USAGE);
            System.exit(1);
        }

    }

    public static void main(String[] args) throws MalformedURLException {
        parseArgs(args);
        CLIInterface cli = new CLIInterface(
                LOGIN,
                PASSWORD,
                HOST,
                PORT,
                REMOTE_PATH
        );
        cli.run();
    }


}