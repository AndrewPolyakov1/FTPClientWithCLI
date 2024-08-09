package com.infotec.ftp.client.remote;

import com.infotec.ftp.client.utils.errors.RemoteException;

import java.io.IOException;

public interface IFTPClient {
    void downloadFile(String path) throws IOException, RemoteException;
    String downloadFileAsString() throws RemoteException;
    void uploadFile(String path) throws IOException, RemoteException;
}
