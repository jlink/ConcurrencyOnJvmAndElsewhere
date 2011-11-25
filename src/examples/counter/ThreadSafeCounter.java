package examples.counter;

import java.util.concurrent.atomic.AtomicLong;

public class ThreadSafeCounter implements Counter {
	private AtomicLong count = new AtomicLong(0);

	public long value() {
		return count.longValue();
	}

	public void inc() {
		count.incrementAndGet();
	}
}
