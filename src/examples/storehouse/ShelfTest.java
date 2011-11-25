package examples.storehouse;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import pte.ParallelTestCase;

public class ShelfTest extends ParallelTestCase {
	@Test
	public void createShelf() {
		Shelf shelf = new Shelf(3);
		assertEquals(3, shelf.getCapacity());
		assertTrue(shelf.getProducts().isEmpty());
		assertTrue(shelf.isEmpty());
		assertFalse(shelf.isFull());
	}

	@Test
	public void puttingProductsIn() {
		Shelf shelf = new Shelf(3);
		shelf.putIn(new Product("book"));
		shelf.putIn(new Product("chair"));
		assertEquals(2, shelf.getProducts().size());
	}

	@Test(expected = StorageException.class)
	public void putTooManyProductsIn() {
		Shelf shelf = new Shelf(1);
		shelf.putIn(new Product("book"));
		assertTrue(shelf.isFull());
		shelf.putIn(new Product("chair"));
	}

	@Test
	public void takingProductsOut() {
		Shelf shelf = new Shelf(3);
		Product aBook = new Product("book");
		shelf.putIn(aBook);
		shelf.putIn(new Product("chair"));
		assertTrue(shelf.takeOut(aBook));
		assertEquals(1, shelf.getProducts().size());
		assertFalse(shelf.takeOut(aBook));
		assertEquals(1, shelf.getProducts().size());
	}

	@Test
	public void useShelfConcurrently() throws Exception {
		final AtomicReference<Shelf> shelf = new AtomicReference<Shelf>();
		final Runnable doBeforeEach = new Runnable() {
			@Override
			public void run() {
				shelf.set(new Shelf(4));
			}
		};
		final Runnable firstTask = new Runnable() {
			@Override
			public void run() {
				shelf.get().putIn(new Product("book"));
				shelf.get().putIn(new Product("car"));
			}
		};
		final Runnable secondTask = new Runnable() {
			@Override
			public void run() {
				shelf.get().putIn(new Product("laptop"));
				shelf.get().putIn(new Product("ipod"));
			}
		};
		Runnable doAfterEach = new Runnable() {
			@Override
			public void run() {
				assertEquals(4, shelf.get().getProducts().size());
			}
		};
		runManyTimesInParallel(5000, doBeforeEach, doAfterEach, firstTask, secondTask);
	}
}
