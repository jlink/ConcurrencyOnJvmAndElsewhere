package examples.storehouse.immutable

import groovy.transform.Immutable

@Immutable
class Product {
	String type
	String toString() {
		"a $type"
	}
}
