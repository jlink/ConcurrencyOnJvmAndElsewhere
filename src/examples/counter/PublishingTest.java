package examples.counter;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.Test;

import pte.ParallelTestCase;

public class PublishingTest extends ParallelTestCase {

	@Test
	public void accessUnsychronizedCounterFromManyThreads() throws Exception {
		final Counter counter = new NaiveCounter();
		counter.inc();
		assertEquals(1L, counter.value());
		Runnable accessCounterTask = new Runnable() {
			@Override
			public void run() {
				assertEquals(1L, counter.value());
			}
		};
		//Could fail but usually doesn't 
		runInParallelThreads(100, accessCounterTask);
	}

	@Test
	public void accessUnsychronizedCounterManyTimes() throws Exception {
		final BlockingQueue<Counter> counterHandOver = new ArrayBlockingQueue<Counter>(1);
		Runnable createCounterTask = new Runnable() {
			@Override
			public void run() {
				Counter counter = new NaiveCounter();
				counter.inc();
				try {
					counterHandOver.put(counter);
				} catch (InterruptedException ignore) {
					Thread.currentThread().interrupt();
				}
			}
		};
		Runnable accessCounterTask = new Runnable() {
			@Override
			public void run() {
				Counter counter;
				try {
					counter = counterHandOver.take();
					assertEquals(1L, counter.value());
				} catch (InterruptedException ignore) {
					Thread.currentThread().interrupt();
				}
			}
		};
		//Could fail but usually doesn't 
		runManyTimesInParallel(1000, null, null, createCounterTask, accessCounterTask);
	}
}
