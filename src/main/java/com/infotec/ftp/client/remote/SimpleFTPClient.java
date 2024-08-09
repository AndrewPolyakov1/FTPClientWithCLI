package com.infotec.ftp.client.remote;

import com.infotec.ftp.client.utils.Utils;
import com.infotec.ftp.client.utils.errors.RemoteException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class SimpleFTPClient implements IFTPClient {
    private final URL url;
    private URLConnection connection;

    public SimpleFTPClient(String login, String password, String host, int port, String remotePath) throws MalformedURLException {
        String ftpLink = String.format("ftp://%s:%s@%s:%d/%s", login, password, host, port, remotePath);
        url = new URL(ftpLink);
    }

    @Override
    public void downloadFile(String localPath) throws RemoteException {
        try {
            connection = url.openConnection();
            Utils.checkFileOrCreate(localPath);
        } catch (IOException e) {
            System.err.println("Error while opening connection: " + e.getMessage());
            // TODO: handle exception
            return;
        }
        // Get input stream from the connection
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(localPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // Read bytes from the input stream and write them to the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("File downloaded successfully.");
        } catch (IOException e) {
            throw new RemoteException("Error while uploading file: " + e.getMessage());
        }
    }

    @Override
    public String downloadFileAsString() throws RemoteException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            System.err.println("Error while opening connection: " + e.getMessage());
            return null; // or throw an exception
        }

        // Get input stream from the connection
        try (InputStream inputStream = connection.getInputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read bytes from the input stream and write them to the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("File downloaded successfully.");
        } catch (IOException e) {
            throw new RemoteException("Error while uploading file: " + e.getMessage());
        }

        return outputStream.toString();
    }

    @Override
    public void uploadFile(String localFile) throws IOException, RemoteException {
        connection = url.openConnection();
        connection.setDoOutput(true);  // Allows output to the connection

        try (OutputStream outputStream = connection.getOutputStream();
             FileInputStream inputStream = new FileInputStream(localFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File uploaded successfully.");
        } catch (IOException e) {
            throw new RemoteException("Error while uploading file: " + e.getMessage());
        }
    }

}
