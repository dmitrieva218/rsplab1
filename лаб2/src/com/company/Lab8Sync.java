package test2;
class SharedObject {
    int x = 0;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }
}


class Main {
    public static void main(String[] args) throws InterruptedException {
        long f = System.currentTimeMillis();
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        SharedObject sharedObject = new SharedObject();

        for (int i = 0; i < 100000; i++) {
            Thread[] threadWait = new Thread[n+m];
            for (int j = 0; j < n; j++) {
                ThreadOne thread = new ThreadOne(sharedObject);
                thread.start();
                threadWait[j] = thread;
            }
            for (int v = 0; v < m; v++) {
                 ThreadTwo threadTwo = new  ThreadTwo(sharedObject);
                Thread actualThread = new Thread(threadTwo);
                actualThread.start();
                threadWait[n+v] = actualThread;
            }

            for (Thread thread:threadWait){
                thread.join();
            }
        }
        System.out.println("Актуальные данные счетчика " + sharedObject.getX() + " Время исполнения " + (double) (System.currentTimeMillis() - f));
    }
}

class ThreadOne extends Thread {
    SharedObject shared;
    public ThreadOne(SharedObject shared) {
        this.shared = shared;
    }

    public void run() {
        synchronized (shared){
            int x = shared.getX();
            x++;
            shared.setX(x);
        }
    }
}

class ThreadTwo implements Runnable {
    SharedObject shared;
    public ThreadTwo(SharedObject shared) {
        this.shared = shared;
    }

    public void run() {
        synchronized (shared){
            int x = shared.getX();
            x--;
            shared.setX(x);
        }

    }
}