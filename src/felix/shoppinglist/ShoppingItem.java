package felix.shoppinglist;

public class ShoppingItem{
	
	long id;
	String name;
	
	public ShoppingItem(String item_name_input) {
		name = item_name_input;
	}

	/*We can only get an id of an item once it's been inserted in to DB*/
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
