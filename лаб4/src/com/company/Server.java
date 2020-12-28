package com.company;

import com.company.entity.DateMessage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private ServerSocket serverSocket;
    private List<DateMessage> listOfMessage = Collections.synchronizedList(new ArrayList<>());

    public Server(int port) {
        new MulticastServerUDP("233.0.0.1", 1502, listOfMessage);

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Запуск сервера... ");

            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new ClientTCPHandler(serverSocket.accept(), listOfMessage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientTCPHandler implements Runnable {
        private ReentrantLock lock = new ReentrantLock();

        private Socket socket;
        private List<DateMessage> listOfMessage;

        private String message;
        private boolean running;
        private BufferedReader in;

        public ClientTCPHandler(Socket socket, List<DateMessage> listOfMessage) {
            this.socket = socket;
            this.listOfMessage = listOfMessage;
        }

        @Override
        public void run() {
            running = true;
            System.out.println("Установлено соединение с клиентом: " + socket);
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (running) {
                    try {
                        message = in.readLine();
                        if ("exit".equalsIgnoreCase(message)) {
                            System.out.println("Соединение закрыто с клиентом: " + socket);
                            running = false;
                            continue;
                        }
                        System.out.println(message);
                        lock.lock();
                        listOfMessage.add(new DateMessage(Calendar.getInstance().getTime(), message));
                        lock.unlock();
                    } catch (IOException e) {
                        throw new Exception("Нет сообщения!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String arg[]) {
        new Server(5555);
    }
}

class MulticastServerUDP extends Thread {
    private ReentrantLock lock = new ReentrantLock();

    private MulticastSocket socket;
    private InetAddress group;
    private byte[] buffer;

    private String address;
    private int port;

    private boolean running;

    private List<DateMessage> listOfMessage;

    public MulticastServerUDP(String address, int port, List<DateMessage> listOfMessage) {
        this.address = address;
        this.port = port;
        this.listOfMessage = listOfMessage;

        start();
    }

    @Override
    public void run() {
        try {
            socket = new MulticastSocket(port);
            group = InetAddress.getByName(address);

            socket.joinGroup(group);

            running = true;
            lock.lock();
            while (running) {
                if (listOfMessage.size() > 0) {
                    for (DateMessage dm : listOfMessage) {
                        buffer = dm.toString().getBytes();
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.send(packet);
                        System.out.println("Отправлена очередная порция сообщений!");
                    }
                    Thread.sleep(10_000);
                    listOfMessage.clear();
                }
            }
            lock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.leaveGroup(group);
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}