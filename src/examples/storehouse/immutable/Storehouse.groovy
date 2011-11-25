package examples.storehouse.immutable

import java.util.concurrent.ConcurrentHashMap

class Storehouse {

	final shelves = [:] as ConcurrentHashMap
	Shelf newShelf(String name, int size) {
		def newShelf = new Shelf(size, [], 0)
		shelves[name] = newShelf
		return newShelf
	}
	Shelf getAt(String name) {
		return shelves[name]
	}
	synchronized boolean update(Map shelvesToUpdate) {
		if (shelvesToUpdate.any {name, shelf ->
			shelves[name].version != shelf.version
		})
			return false
		shelvesToUpdate.each {name, shelf ->
			shelves[name] = shelf.incVersion()
		}
		return true
	}
	boolean move(Product product, String from, String to) {
		while(true) {
			Shelf shelfTo = shelves[to]
			Shelf shelfFrom = shelves[from]
			if (shelfTo.isFull())
				return false
			def newShelfFrom = shelfFrom.takeOut(product)
			if (shelfFrom == newShelfFrom)
				return false
			shelfTo = shelfTo.putIn(product)
			def m = [(from): newShelfFrom, (to): shelfTo]
			if (update(m))
				return true
		}
	}
}
