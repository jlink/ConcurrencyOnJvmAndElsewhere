package pte;

import static org.junit.Assert.fail;

import java.util.*;
import java.util.concurrent.*;

public class ParallelTestCase {

	private static final long DEFAULT_TIMEOUT_SECS = 5;

	private long nanos = 0;
	private Map<Thread, Throwable> throwables = new ConcurrentHashMap<Thread, Throwable>();

	public void runInParallelThreads(int threads, final Runnable task) throws InterruptedException {
		runInParallelThreads(threads, task, DEFAULT_TIMEOUT_SECS, TimeUnit.SECONDS);
	}

	public void runInParallelThreads(int threads, final Runnable task, long timeout, TimeUnit unit) throws InterruptedException {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(threads);
		for (int i = 1; i <= threads; i++) {
			Thread t = new Thread("parallel[" + i + "]") {
				@Override
				public void run() {
					try {
						startGate.await();
						try {
							task.run();
						} catch (Throwable e) {
							throwables.put(Thread.currentThread(), e);
						} finally {
							endGate.countDown();
						}
					} catch (InterruptedException ignored) {
					}
				}
			};
			t.start();
		}
		long before = System.nanoTime();
		startGate.countDown();
		boolean timedOut = !endGate.await(timeout, unit);
		long after = System.nanoTime();
		nanos = after - before;
		failIfExceptionsOccurred();
		if (timedOut)
			fail("Timed out after " + unit.toSeconds(timeout) + " secs");
	}

	public void runAndMeasure(final Runnable task) {
		long before = System.nanoTime();
		task.run();
		long after = System.nanoTime();
		nanos = after - before;
	}

	public long runManyTimesAndMeasureAverage(int times, Runnable beforeEach, final Runnable task) {
		long nanosSum = 0L;
		task.run(); //Run once for the JIT compilation to happen
		for (int i = 0; i < times; i++) {
			if (beforeEach != null)
				beforeEach.run();
			runAndMeasure(task);
			nanosSum += lastRunNanos();
		}
		return nanosSum / times;
	}

	public long lastRunNanos() {
		return nanos;
	}

	public void runManyTimesInParallel(int times, Runnable doBeforeEach, Runnable doAfterEach, Runnable... tasks) throws InterruptedException {
		final CountDownLatch startGate = new CountDownLatch(1);
		nanos = 0L;
		for (int i = 1; i <= times; i++) {
			if (doBeforeEach != null) {
				doBeforeEach.run();
			}
			ExecutorService executor = Executors.newCachedThreadPool();
			for (final Runnable task : tasks) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							startGate.await();
						} catch (InterruptedException ignored) {
						}
						try {
							task.run();
						} catch (Throwable e) {
							throwables.put(Thread.currentThread(), e);
						}
					}
				});
			}
			long before = System.nanoTime();
			startGate.countDown();
			executor.shutdown();
			boolean timedOut = !executor.awaitTermination(DEFAULT_TIMEOUT_SECS, TimeUnit.SECONDS);
			failIfExceptionsOccurred();
			if (timedOut) {
				fail("Timed out in trial " + i + " after " + DEFAULT_TIMEOUT_SECS + " secs");
			}
			long after = System.nanoTime();
			nanos += (after - before);
			if (doAfterEach != null) {
				doAfterEach.run();
			}
		}
	}

	private void failIfExceptionsOccurred() {
		if (!throwables.isEmpty())
			fail(throwables.size() + " exceptions occurred: " + throwables);
	}

}
