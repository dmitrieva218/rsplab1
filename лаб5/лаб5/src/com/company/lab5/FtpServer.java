package com.company.lab5;
import java.rmi.*;
import java.util.ArrayList;

public interface FtpServer extends Remote {
    FileList  getAllFileList() throws RemoteException;
    FileList getFindFileList(String findQuery) throws RemoteException;
    byte[] downloadFileById(int idx)throws RemoteException;
}
