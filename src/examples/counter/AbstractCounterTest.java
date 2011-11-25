package examples.counter;

import static org.junit.Assert.assertEquals;

import org.junit.*;
import org.junit.rules.TestName;

import pte.ParallelTestCase;

public abstract class AbstractCounterTest extends ParallelTestCase {

	volatile protected Counter counter;

	@Test
	public void countInSingleThread() throws Exception {
		for (long i = 1; i <= 1000; i++) {
			counter.inc();
			assertEquals(i, counter.value());
		}
	}

	@Test
	public void countInManyThreads() throws Exception {
		int threads = 200;
		Runnable countingTask = new Runnable() {
			@Override
			public void run() {
				for (long i = 1; i <= 1000; i++) {
					counter.inc();
				}
			}
		};
		runInParallelThreads(threads, countingTask);
		assertEquals(threads * 1000, counter.value());
	}

	@Rule
	public TestName testName = new TestName();

	@After
	public void printRuntime() {
		System.out.println(getClass().getSimpleName() + "." + testName.getMethodName() + ": " + lastRunNanos() / 1000000.0 + " secs");
	}

}
