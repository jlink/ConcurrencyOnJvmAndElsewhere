package examples.counter;

import org.junit.*;
import static org.junit.Assert.*;

import pte.ParallelTestCase;

public class CounterPerformanceTest extends ParallelTestCase {

	@Test @Ignore
	public void comparison() throws Exception {
		long nanosSynchronizedTest = measureCounterPerformanceInNanos(new SynchronizedCounter());
		System.out.println("SynchronizedCounter: " + nanosSynchronizedTest / 1000000 + " ms");

		long nanosLockedTest = measureCounterPerformanceInNanos(new LockedCounter());
		System.out.println("LockedCounter: " + nanosLockedTest / 1000000 + " ms");

		long nanosThreadSafeTest = measureCounterPerformanceInNanos(new ThreadSafeCounter());
		System.out.println("ThreadSafeCounter: " + nanosThreadSafeTest / 1000000 + " ms");

		//assertTrue(nanosLockedTest < nanosSynchronizedTest);
		//assertTrue(nanosThreadSafeTest * 5 < nanosSynchronizedTest);
	}

	private long measureCounterPerformanceInNanos(final Counter threadSafeCounter) throws Exception {
		final Runnable threadSafeTest = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 5000; i++) {
					threadSafeCounter.inc();
					assertTrue(threadSafeCounter.value() > 0L);
				}
			}
		};
		runManyTimesInParallel(5, null, null, threadSafeTest, threadSafeTest);
		assertEquals(50000L, threadSafeCounter.value());
        return lastRunNanos();
	}
}
