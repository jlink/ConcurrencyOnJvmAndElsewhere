package examples.storehouse;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import pte.ParallelTestCase;

public class ConcurrentShelfCreationTest extends ParallelTestCase {

	private volatile Storehouse store = new Storehouse();

	@Test
	public void createAccountsInManyThreads() throws Exception {
		final int numberOfShelves = 100;
		final AtomicInteger shelf = new AtomicInteger(0);
		Runnable createShelfTask = new Runnable() {
			@Override
			public void run() {
				String shelfName = "n" + shelf.incrementAndGet();
				Shelf shelf = store.newShelf(shelfName, 1);
				assertNotNull("No shelf " + shelfName, store.getShelf(shelfName));
			}
		};
		runInParallelThreads(numberOfShelves, createShelfTask);
		// assertAllShelvesHaveBeenCreated(numberOfShelves);
	}
}
