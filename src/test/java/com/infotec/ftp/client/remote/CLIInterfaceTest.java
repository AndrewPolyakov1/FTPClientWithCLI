package com.infotec.ftp.client.remote;

import com.infotec.ftp.client.CLIInterface;
import com.infotec.ftp.client.utils.Utils;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CLIInterfaceTest {
    private final String localPath = "src\\test\\resources\\students.json";
    private final String LOGIN = "user";
    private final String PASSWORD = "password";
    private final String HOST = "localhost";
    private final String REMOTE_PATH = "students.json";
    private final int PORT = 21;
    private final InputStream originalSystemIn = System.in;
    private final String fileContent = "{\n" +
            "    \"students\": [\n" +
            "        {\n" +
            "            \"id\": 1,\n" +
            "            \"name\": \"Student1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 2,\n" +
            "            \"name\": \"Student2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 3,\n" +
            "            \"name\": \"Student3\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 4,\n" +
            "            \"name\": \"Student4\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 5,\n" +
            "            \"name\": \"Student5\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 6,\n" +
            "            \"name\": \"Student6\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 7,\n" +
            "            \"name\": \"Student7\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 9,\n" +
            "            \"name\": \"Andrew\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 10,\n" +
            "            \"name\": \";sadflKJG;\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 11,\n" +
            "            \"name\": \"FSGDA\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 12,\n" +
            "            \"name\": \"asd\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    private FakeFtpServer server;

    @BeforeMethod
    public void setup() {
        // Mock FTP Server
        File file = new File(localPath);

        server = new FakeFtpServer();
        server.setServerControlPort(PORT);

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/", fileContent));
        server.setFileSystem(fileSystem);
        server.addUserAccount(new UserAccount(LOGIN, PASSWORD,"/"));
        server.addUserAccount(new UserAccount("user", "p", Utils.toSystemFilePath("src\\test\\resources")));
        server.start();
    }

    @AfterMethod
    public void teardown() {
        server.stop();
    }

    @Test
    public void testServerIsAvailable() throws IOException {
        URL url = new URL(String.format("ftp://%s:%s@%s:%d/%s", LOGIN, PASSWORD, HOST, PORT, REMOTE_PATH));
        URLConnection connection = url.openConnection();
        connection.connect();
    }

    @Test
    public void testRun() throws IOException {
        // Mock System.in
        String simulatedInput = "1\n1\n5";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);
        //

        CLIInterface cli = new CLIInterface(
                LOGIN,
                PASSWORD,
                HOST,
                PORT,
                "students.json"
        );
        cli.run();
    }
}