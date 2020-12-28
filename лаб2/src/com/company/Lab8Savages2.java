package test5;

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

class Lab8Savages2 {
    //количество дикарей
    final static int SAVAGE = 12;

    public static void main(String[] args) throws InterruptedException {
        Resource resource = new Resource();
        CookThread cookThread = new CookThread(resource);
        cookThread.start();
        SavageThread[] savageThreads = new SavageThread[SAVAGE];
        for (int i = 0; i < SAVAGE; i++) {
            SavageThread thread = new SavageThread(resource);
            thread.start();
            savageThreads[i] = thread;
        }

        while (true) {
            boolean statusEat = true;
            for (SavageThread thread : savageThreads) {
                if (!thread.getStatusEat()) {
                    statusEat = false;
                    break;
                }
            }
            if (statusEat) {
                System.out.println("Перезапуск очереди");
                for (SavageThread thread : savageThreads) {
                    thread.setStatusEat(false);
                }
            }
        }

    }
}


//statusEat = false Не ел еще
//statusEat = true поел

class SavageThread extends Thread {
    Resource resource;
    private volatile boolean statusEat = false;

    public SavageThread(Resource resource) {
        this.resource = resource;
    }

    public boolean getStatusEat() {
        return this.statusEat;
    }

    public void setStatusEat(boolean statusEat) {
        this.statusEat = statusEat;
    }

    public void eat() {
        resource.lock.lock();
        resource.setN(resource.getN() - 1);
        resource.lock.unlock();
        setStatusEat(true);
    }

    public void run() {
        for (; ; ) {
            if (!getStatusEat()) {
                if (resource.getN() > 0) {
                    eat();
                    System.out.println("Взял ресурс");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
