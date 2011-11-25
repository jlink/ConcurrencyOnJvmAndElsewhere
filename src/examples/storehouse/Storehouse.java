package examples.storehouse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Storehouse {

	private final Map<String, Shelf> shelves = new ConcurrentHashMap<String, Shelf>();

	public Shelf newShelf(String name, int capacity) {
		Shelf newShelf = new Shelf(capacity);
		shelves.put(name, newShelf);
		return newShelf;
	}

	public Shelf getShelf(String name) {
		return shelves.get(name);
	}

	public boolean move(Product product, String from, String to) {
		Shelf source = shelves.get(from);
		Shelf target = shelves.get(to);
		Object[] locks = new Object[] { source, target };
		Arrays.sort(locks);
		synchronized (locks[0]) {
			synchronized (locks[1]) {
				return doMove(product, source, target);
			}
		}
	}

	private boolean doMove(Product product, Shelf source, Shelf target) {
		if (!source.takeOut(product)) {
			return false;
		}
		try {
			target.putIn(product);
			return true;
		} catch (StorageException se) {
			source.putIn(product);
			return false;
		}
	}

}
