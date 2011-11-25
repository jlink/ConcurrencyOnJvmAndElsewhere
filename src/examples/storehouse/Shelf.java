package examples.storehouse;

import java.util.*;

public class Shelf implements Comparable<Shelf> {

	private final int capacity;
	private List<Product> products = new ArrayList<Product>();

	public Shelf(int capacity) {
		this.capacity = capacity;
	}

	public synchronized int getCapacity() {
		return capacity;
	}

	public synchronized List<Product> getProducts() {
		return products;
	}

	public synchronized boolean isEmpty() {
		return products.isEmpty();
	}

	public synchronized boolean isFull() {
		return products.size() == capacity;
	}

	public synchronized void putIn(Product product) {
		if (isFull())
			throw new StorageException("shelf is full.");
		products.add(product);
	}

	public synchronized boolean takeOut(Product aBook) {
		return products.remove(aBook);
	}

	@Override
	public int compareTo(Shelf other) {
		return System.identityHashCode(this) - System.identityHashCode(other);
	}
}
