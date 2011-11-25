package examples.counter;

public class SynchronizedCounter implements Counter {
	private long count = 0;

	public synchronized long value() {
		return count;
	}

	public synchronized void inc() {
		count++;
	}
}
