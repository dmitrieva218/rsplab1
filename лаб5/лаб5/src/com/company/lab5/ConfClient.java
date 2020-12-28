package com.company.lab5;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

public class ConfClient {
    public DefaultTableModel tableModel;
    String[] headers = {"id", "Название", "Дата Создания", "Автор"};
    ArrayList<Vector<String>> tableData = new ArrayList<Vector<String>>();
    static JFrame frame;
    static JPanel panel;
    JLabel findFileLabel;
    JTextField findFileInput;
    JButton find;
    JButton download;
    JTable table;
    JFileChooser fileChooser;
    int selectTableRowIdValue = 0;
    FileList allFileList;

    public ConfClient() {
        try {
            FtpServer server = (FtpServer) Naming.lookup(
                    "rmi://localhost/ConfServer");
            allFileList = server.getAllFileList();

            for (FileAtr fileAtr : allFileList.getFileAtrArrayList()) {
                Vector<String> tmp = new Vector<>();
                tmp.add(Integer.toString(fileAtr.getId()));
                tmp.add(fileAtr.getName());
                tmp.add(fileAtr.getCreatedAt());
                tmp.add(fileAtr.getAuthor());
                tableData.add(tmp);
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
        frame = new JFrame("Управление файлами");
        panel = new JPanel();
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(headers);

        for (Vector<String> stringVector : tableData) {
            tableModel.addRow(stringVector);
        }

        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getSelectionModel().addListSelectionListener(new TableListenerSelect());
        panel.setLayout(new GridLayout(4, 4));
        frame.setBounds(100, 100, 800, 400);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        findFileLabel = new JLabel("Найти файл");
        findFileInput = new JTextField(1);
        find = new JButton("Найти");
        download = new JButton("Скачать");

        panel.add(findFileLabel);
        panel.add(findFileInput);
        panel.add(find);
        panel.add(table);
        panel.add(new JScrollPane(table));
        panel.add(download);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        find.addActionListener(new ButtonListenerFind());
        download.addActionListener(new ButtonListenerDownload());
    }

    class TableListenerSelect implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent event) {
            try {
                if (table.getModel().getColumnCount() > 0) {
                    selectTableRowIdValue = Integer.parseInt((String) table.getModel().getValueAt(table.getSelectedRow(), 0));
                }
            } catch (Exception e) {

            }
        }
    }

    class ButtonListenerFind implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            try {
                table.clearSelection();
                tableModel.setRowCount(0);
                FtpServer server = (FtpServer) Naming.lookup(
                        "rmi://localhost/ConfServer");
                FileList allFileList = server.getFindFileList(findFileInput.getText());
                tableModel.setRowCount(0);
                tableData.clear();
                for (FileAtr fileAtr : allFileList.getFileAtrArrayList()) {
                    Vector<String> tmp = new Vector<>();
                    tmp.add(Integer.toString(fileAtr.getId()));
                    tmp.add(fileAtr.getName());
                    tmp.add(fileAtr.getCreatedAt());
                    tmp.add(fileAtr.getAuthor());
                    tableData.add(tmp);
                }

                for (Vector<String> stringVector : tableData) {
                    tableModel.addRow(stringVector);
                }
                table.repaint();
            } catch (RemoteException | NotBoundException | MalformedURLException e) {
                e.printStackTrace();
            }
            try {


            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Ошибка");
            }
        }
    }

    class ButtonListenerDownload implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            byte[] byteFile = null;
            FtpServer server = null;
            try {
                server = (FtpServer) Naming.lookup(
                        "rmi://localhost/ConfServer");
                byteFile = server.downloadFileById(selectTableRowIdValue);
            } catch (NotBoundException notBoundException) {
                notBoundException.printStackTrace();
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            FileAtr findFile = null;
            for (FileAtr file : allFileList.getFileAtrArrayList()) {
                if (file.getId() == selectTableRowIdValue) {
                    findFile = file;
                    break;
                }
            }
            if (findFile == null) {
                JOptionPane.showMessageDialog(frame,
                        "Файл ненайден отмена");
                return;
            }
            String[] filePathSplit = findFile.getPath().split("\\\\");
            String fileNameAndType = filePathSplit[filePathSplit.length - 1];
            String fileType = filePathSplit[filePathSplit.length - 1].split("[.]")[1];
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Сохранение файла");
            fileChooser.setFileFilter(new FileNameExtensionFilter(fileType, fileType));
            File file = new File("C:/" + fileNameAndType);
            fileChooser.setSelectedFile(file);
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(fileChooser.getSelectedFile());
                    for (byte xx : byteFile) {
                        fout.write(xx);
                    }
                    fout.close();
                    JOptionPane.showMessageDialog(frame,
                            "Файл успешно загружен");
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (result == JFileChooser.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(frame,
                        "Отмена выбора места сохранения");
            } else if (result == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(frame,
                        "Системная ошибка");
            }
        }
    }

    public static void main(String args[]) throws NamingException {

        Context context = new InitialContext();
        Enumeration<NameClassPair> e = context.list("rmi://localhost/");
        new ConfClient();
    }
}

