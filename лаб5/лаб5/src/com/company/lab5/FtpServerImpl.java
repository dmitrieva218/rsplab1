package com.company.lab5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.security.Permission;
import java.sql.*;


public class FtpServerImpl extends UnicastRemoteObject
        implements FtpServer {
    /* Определяется конструктор по умолчанию */
    public FtpServerImpl() throws RemoteException {
        super();
    }

    @Override
    public FileList getAllFileList() throws RemoteException {
        FileList fileList = new FileList();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            // Получение соединения с БД
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/Lab05DB1;create=true;user=root;password=toor");
            Statement st1 = con.createStatement();
            ResultSet resultSet = st1.executeQuery("SELECT * FROM FILE_INFO");
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String date = resultSet.getString(3);
                String author = resultSet.getString(4);
                String path = resultSet.getString(5);
                FileAtr fileAtr = new FileAtr(id, name, date, author, path);
                fileList.addFile(fileAtr);
            }
            for (FileAtr fileAtr : fileList.getFileAtrArrayList()) {
                System.out.println(fileAtr.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage(), e);
        }
        return fileList;
    }

    @Override
    public FileList getFindFileList(String findQuery) throws RemoteException {
        FileList fileList = new FileList();

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            // Получение соединения с БД
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/Lab05DB1;create=true;user=root;password=toor");
            Statement st1 = con.createStatement();
            String query = "SELECT * FROM FILE_INFO WHERE name LIKE '%" + findQuery + "%'";
            ResultSet resultSet = st1.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String date = resultSet.getString(3);
                String author = resultSet.getString(4);
                String path = resultSet.getString(5);
                FileAtr fileAtr = new FileAtr(id, name, date, author, path);
                fileList.addFile(fileAtr);
            }
            for (FileAtr fileAtr : fileList.getFileAtrArrayList()) {
                System.out.println(fileAtr.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage(), e);
        }

        return fileList;
    }

    @Override
    public byte[] downloadFileById(int idx) throws RemoteException {
        try {
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/Lab05DB1;create=true;user=root;password=toor");
            Statement st1 = con.createStatement();
            String query = "SELECT * FROM FILE_INFO WHERE id=" + idx;
            ResultSet resultSet = st1.executeQuery(query);
            String path = "";
            while (resultSet.next()) {
                path = resultSet.getString(5);
            }
            File file = new File(path);
            byte[] b = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(b);
                for (int i = 0; i < b.length; i++) {
                    System.out.print((char) b[i]);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found.");
                e.printStackTrace();
            }
            return b;
        } catch (Exception e) {

        }
        return null;
    }

    /* Метод main() */
    public static void main(String args[]) {
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager() {
                    public void checkConnect(String host, int port,
                                             Object context) {
                    }

                    public void checkConnect(String host, int port) {
                    }

                    public void checkPermission(Permission perm) {
                    }
                });
            }
            FtpServerImpl instance = new FtpServerImpl();
            Naming.rebind("ConfServer", new FtpServerImpl());
            System.out.println("Сервис зарегистрирован");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
