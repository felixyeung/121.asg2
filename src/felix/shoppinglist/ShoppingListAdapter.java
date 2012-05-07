package felix.shoppinglist;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList>{

	public ShoppingListAdapter(Context context, int resource,
			List<ShoppingList> objects) {
		super(context, resource, objects);
	}
	
}
