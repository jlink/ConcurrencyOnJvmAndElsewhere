package examples.storehouse;

public class Product {
	private String type;

	public Product(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "a " + type;
	}
}
