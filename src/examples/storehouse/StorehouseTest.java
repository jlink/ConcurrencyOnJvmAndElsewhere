package examples.storehouse;

import static org.junit.Assert.*;

import org.junit.*;

public class StorehouseTest {

	private Storehouse storehouse;

	@Before
	public void init() {
		storehouse = new Storehouse();
	}

	@Test
	public void createStorehouse() {
		Shelf aShelf = storehouse.newShelf("a", 3);
		assertSame(aShelf, storehouse.getShelf("a"));
	}

	@Test
	public void movingProductsSuccessfully() throws Exception {
		Shelf a = storehouse.newShelf("a", 2);
		Shelf b = storehouse.newShelf("b", 1);
		Product aBook = new Product("book");
		Product aLaptop = new Product("laptop");
		a.putIn(aBook);
		a.putIn(aLaptop);
		assertTrue(storehouse.move(aBook, "a", "b"));
		assertFalse(a.getProducts().contains(aBook));
		assertTrue(b.getProducts().contains(aBook));
	}

	@Test
	public void noProductToMove() throws Exception {
		storehouse.newShelf("a", 1);
		storehouse.newShelf("b", 1);
		assertFalse(storehouse.move(new Product("book"), "a", "b"));
	}

	@Test
	public void cannotMoveToFullShelf() throws Exception {
		Shelf a = storehouse.newShelf("a", 1);
		storehouse.newShelf("b", 0);
		Product aBook = new Product("book");
		a.putIn(aBook);
		assertFalse(storehouse.move(aBook, "a", "b"));
		assertTrue(a.getProducts().contains(aBook));
	}
}
