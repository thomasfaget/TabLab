package tablab.PartitionPlayer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple implementation of PartitionPlayer and AbstractPartitionPlayer.
 * This class use a ReentrantLock for the mutex and the method 'sleep' of 'Thread' to sleep on the current thread.
 */
public class SimplePartitionPlayer extends AbstractPartitionPlayer {

    private final Lock lock = new ReentrantLock(true);

    @Override
    void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    @Override
    void lockPause() throws InterruptedException {
        synchronized (lock) {
            lock.wait();
        }
    }

    @Override
    void notifyPause() {
        synchronized (lock) {
            lock.notify();
        }
    }
}
