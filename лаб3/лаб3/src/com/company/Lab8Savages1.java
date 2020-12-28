package com.company;

import java.util.concurrent.locks.ReentrantLock;

class Resource {
    final int BASE_RESOURCE = 5;
    int n;
    ReentrantLock lock;

    Resource() {
        lock = new ReentrantLock();
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}

public class Lab8Savages1 {
    //количество дикарей

    final static int SAVAGE = 12;

    public static void main(String[] args) throws InterruptedException {
        Resource resource = new Resource();
        CookThread cookThread = new CookThread(resource);
        Thread[] savageThread = new Thread[SAVAGE];
        cookThread.start();

        for (int i = 0; i < SAVAGE; i++) {
            SavageThread thread = new SavageThread(resource);
            thread.start();
            savageThread[i] = thread;
        }

        for (Thread thread : savageThread) {
            thread.join();
        }

        cookThread.killThread();
        cookThread.join();

    }
}


class SavageThread extends Thread {
    Resource resource;

    public SavageThread(Resource resource) {
        this.resource = resource;
    }

    public void eat() {
        resource.lock.lock();
        resource.setN(resource.getN() - 1);
        resource.lock.unlock();
    }

    public void run() {
        for (; ; ) {
            if (resource.getN() > 0) {
                eat();
                System.out.println("Взял ресурс");
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class CookThread extends Thread {
    Resource resource;
    volatile boolean alive = true;

    public CookThread(Resource resource) {
        this.resource = resource;
        this.resource.setN(this.resource.BASE_RESOURCE);
    }

    public void run() {
        while (alive) {
            if (this.resource.getN() == 0) {
                this.resource.setN(this.resource.BASE_RESOURCE);
                System.out.println("Обновил ресурс");
            }
        }
    }

    public void killThread() {
        this.alive = false;
    }
}
