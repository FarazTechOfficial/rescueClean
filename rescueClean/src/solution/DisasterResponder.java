package solution;

import sim.Message;

import java.util.concurrent.*;

public abstract class DisasterResponder {

    protected final Thread commsThread;
    protected String configFile;

    protected final ExecutorService executor;
    protected BlockingQueue<Message> inMessageQueue;
    protected BlockingQueue<Message> outMessageQueue;

    public DisasterResponder() {

        inMessageQueue = new LinkedBlockingQueue<>();
        executor = Executors.newSingleThreadExecutor();

        commsThread = new Thread(this::commsLoop, "DisasterResponder-DispatchThread");

        commsThread.setUncaughtExceptionHandler((t, e) ->
                System.err.println("Exception in " + t.getName() + ": " + e.getMessage()));
    }

    protected abstract void handle(Message m);
    protected abstract void setup();

    public final void start(String configFile) {
        this.configFile = configFile;

        commsThread.start();

        System.out.println("RESPONDER SETUP START");
        setup();
        System.out.println("RESPONDER SETUP DONE");
    }

    private void commsLoop() {

        while (true) {
            try {
                Message m = inMessageQueue.take();

                if (m == Message.SHUTDOWN) {
                    shutdown();
                    break;
                }

                handle(m);

            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("RESPONDER TERMINATED");
    }

    public void setOutMessageQueue(BlockingQueue<Message> queue) {
        this.outMessageQueue = queue;
    }

    public BlockingQueue<Message> getInMessageQueue() {
        return inMessageQueue;
    }

    public void shutdown() {

        executor.shutdown();

        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("RESPONDER EXECUTOR SHUTDOWN");
    }
}