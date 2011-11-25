package examples.counter;

import java.util.concurrent.locks.*;

public class LockedCounter implements Counter {
	private long count = 0;
	private Lock lock = new ReentrantLock();

	public long value() {
		lock.lock();
		long value = count;
		lock.unlock();
		return value;
	}

	public void inc() {
		lock.lock();
		count++;
		lock.unlock();
	}
}
