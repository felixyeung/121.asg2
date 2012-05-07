package felix.shoppinglist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShoppingList {

	long id;
	String name;
	Date created;
	
	public ShoppingList(String list_name_input) {
		this(list_name_input, new Date(java.lang.System.currentTimeMillis()));
	}
	
	//constructor
	public ShoppingList(String list_name_input, Date created_input) {
		this(-1, list_name_input, new Date(java.lang.System.currentTimeMillis()));
	}
	
	//constructor
	public ShoppingList(long id_input, String list_name_input, Date created_input) {
		id = id_input;
		name = list_name_input;
		created = created_input;
	}
	
	/*We can only get an id of a list once it's been inserted in to DB*/
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Date getCreated() {
		return created;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		String dateString = sdf.format(created);
		return id + ": " + name + " (" + dateString +  ")";
	}
}
