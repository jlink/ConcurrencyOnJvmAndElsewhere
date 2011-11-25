package examples.counter;

public class NaiveCounter implements Counter {
	private long count = 0;

	public long value() {
		return count;
	}

	public void inc() {
		count++;
	}

	public static void main(String[] args) {
		Counter counter = new NaiveCounter();
		// in Thread 1:
		counter.inc();
		// in Thread 2:
		counter.inc();
		assert counter.value() == 2;
	}
}
