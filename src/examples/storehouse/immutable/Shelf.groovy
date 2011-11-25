package examples.storehouse.immutable
import groovy.transform.Immutable

@Immutable
class Shelf {
	int capacity
	List products
	long version

	Shelf putIn(Product product) {
		if (isFull())
			throw new StorageException("shelf is full.")
		return cloneWith(products: new ArrayList(products) << product)
	}

	private Shelf cloneWith(changes) {
		def newProps = props
		changes.each {key, value -> newProps[key] = value}
		new Shelf(newProps)
	}

	def getProps() {
		[products: products, capacity: capacity, version: version]
	}

	Shelf incVersion() {
		return cloneWith(version: version + 1)
	}

	Shelf takeOut(Product product) {
		if (!products.contains(product))
			return this
		return cloneWith(products: new ArrayList(products).minus(product))
	}

	int getOccupied() {
		return products.size()
	}

	boolean isEmpty() {
		return products.isEmpty()
	}

	boolean isFull() {
		return products.size() == capacity
	}
}

