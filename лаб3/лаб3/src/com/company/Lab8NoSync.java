package test4;

class Main {
    public static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        long f = System.currentTimeMillis();
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        for (int i = 0; i < 100000; i++) {
            Thread[] threadWait = new Thread[n + m];
            for (int j = 0; j < n; j++) {
                ThreadOne thread = new ThreadOne();
                thread.start();
                threadWait[j] = thread;
            }
            for (int v = 0; v < m; v++) {
                ThreadTwo threadTwo = new ThreadTwo();
                Thread actualThread = new Thread(threadTwo);
                actualThread.start();
                threadWait[n + v] = actualThread;
            }

            for (Thread thread : threadWait) {
                thread.join();
            }
        }
        System.out.println("Актуальные данные счетчика " + counter + " Время исполнения " + (double) (System.currentTimeMillis() - f));
    }
}

class ThreadOne extends Thread {
    public void run() {
        int actualCounter = Main.counter;
        actualCounter++;
        Main.counter = actualCounter;
    }
}

class ThreadTwo implements Runnable {
    public void run() {
        int actualCounter = Main.counter;
        actualCounter--;
        Main.counter = actualCounter;
    }
}