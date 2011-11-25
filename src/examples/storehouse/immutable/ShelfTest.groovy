package examples.storehouse.immutable;

import static org.junit.Assert.*;

import org.junit.Test;

class ShelfTest {

	@Test
	void everythingInOneThread() throws Exception {
		// Testing Shelf functionality

		def shelf = new Shelf(5, [], 1)
		assert shelf.capacity == 5
		assert shelf.isEmpty()
		assert !shelf.isFull()
		assert shelf.occupied == 0
		assert shelf.products == []
		assert shelf.version == 1

		def p1 = new Product(type: "Buch1")
		shelf = shelf.putIn(p1)
		assert !shelf.isEmpty()
		assert shelf.occupied == 1
		assert shelf.products == [p1]
		assert shelf.incVersion().version == 2

		shelf = shelf.putIn(new Product(type: "Buch2"))
		shelf = shelf.putIn(new Product(type: "Buch3"))
		shelf = shelf.putIn(new Product(type: "Buch4"))
		shelf = shelf.putIn(new Product(type: "Buch5"))
		assert shelf.occupied == 5
		assert shelf.isFull()

		try {
			shelf.putIn(new Product(type: "Buch"))
			assert false, "StorageException expected"
		} catch (StorageException expected) {
			assert expected.message.contains("is full")
		}

		shelf = shelf.takeOut(p1)
		assert shelf.occupied == 4
		assert !shelf.products.contains(p1)

		assert shelf.takeOut(p1) == shelf
	}

	@Test
	public void concurrent() throws Exception {
		final threads = []
		final storehouse = new Storehouse()
		storehouse.newShelf('a', 100)

		100.times {
			threads << Thread.start {
				while(true) {
					def s = storehouse['a'].putIn(new Product('p'))
					if (storehouse.update('a': s))
						break
				}
			}
		}
		threads.each {it.join()}
		assert storehouse['a'].occupied == 100
	}
}
