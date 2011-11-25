package examples.storehouse.immutable;

import static org.junit.Assert.*;

import org.junit.Test;

class StorehouseTest {

	@Test
	public void functionality() throws Exception {
		def store = new Storehouse()
		assert store.newShelf('A', 5) instanceof Shelf
		assert store['A'] instanceof Shelf
		assert store['A'].capacity == 5

		def book = new Product(type: "Book")
		def pen = new Product(type: "Pen")
		def lamp = new Product(type: "Lamp")

		def shelfA = store['A'].putIn(book)
		shelfA = shelfA.putIn(pen)
		shelfA = shelfA.putIn(lamp)
		store.update('A': shelfA)

		store.newShelf('B', 2)
		assert store.move(book, 'A', 'B')

		assert store['A'].products == [pen, lamp]
		assert store['B'].products == [book]

		assert store.move(pen, 'A', 'B')
		assert !store.move(lamp, 'A', 'B')
		assert store['A'].products == [lamp]
		assert store['B'].products == [book, pen]
	}

	@Test
	public void concurrentMove() throws Exception {
		(1..100).each { n->
			final threads = []
			//println "${n}th trial"
			final concStorehouse = new Storehouse()
			final b = new Product(type: "Book")
			final p = new Product(type: "Pen")
			final l = new Product(type: "Lamp")
			final shelfa = concStorehouse.newShelf('a', 3)
			final shelfb = concStorehouse.newShelf('b', 2)
			final products = [b, p, l]
			products.each {
				shelfa = shelfa.putIn(it)
			}
			concStorehouse.update(a: shelfa)
			products.each { product ->
				threads << Thread.start {
					concStorehouse.move(product, 'a', 'b')
				}
			}
			threads.each {it.join()}
			assert concStorehouse['a'].occupied == 1
			assert concStorehouse['b'].occupied == 2
		}
	}

	@Test
	public void deadlocks() throws Exception {
		(1..100).each { n->
			def threads = []
			//println "${n}th trial"
			final concStorehouse = new Storehouse()
			final b = new Product(type: "Book")
			final p = new Product(type: "Pen")
			concStorehouse.newShelf('a', 2)
			concStorehouse.update(a: concStorehouse['a'].putIn(b))
			concStorehouse.newShelf('b', 2)
			concStorehouse.update(b: concStorehouse['b'].putIn(p))
			threads << Thread.start {
				concStorehouse.move(b, 'a', 'b')
				concStorehouse.move(p, 'a', 'b')
			}
			threads << Thread.start {
				concStorehouse.move(b, 'b', 'a')
				concStorehouse.move(p, 'b', 'a')
			}
			threads.each {
				it.join(2000)
				if (it.isAlive())
					throw new RuntimeException("Deadlock in thread $it")
			}
			assert (concStorehouse['a'].products + concStorehouse['b'].products) as Set == [b, p]as Set
		}
	}
}
