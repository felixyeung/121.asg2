package felix.shoppinglist;

import java.util.ArrayList;
import java.util.Date;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ShoppinglistActivity extends Activity {
	
	private ArrayList<ShoppingList> shoppingLists;
	//We don't declare a list of items here, that's the job of the next activity.
	private ListView shoppingListsView;
	private EditText newListNameInput;
	private Button newListSubmit;
	
	//ShoppingListAdapter is like an array adapter tailored for ShoppingList Objects
	private ShoppingListAdapter saa;
	//We don't declare an Adaptor for each grocery item because that's for the next activity to do.

	ShoppinglistDBAdapter dba;
	Cursor c;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*Create database here*/
        setContentView(R.layout.main);
        
        shoppingListsView = (ListView)findViewById(R.id.lists_display);
        newListNameInput = (EditText)findViewById(R.id.list_name_input);
        
        shoppingLists = new ArrayList<ShoppingList>();
        
        //create our adapter to display our list of shopping lists
        saa = new ShoppingListAdapter(this, android.R.layout.simple_list_item_1, shoppingLists);
        
        shoppingListsView.setAdapter(saa);
        
        
        //Code to handle adding of a new shopping list.
        //DB interactions are done in here.
        newListSubmit = (Button)findViewById(R.id.create_list);

        newListSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//create a ShoppingList object from input
            	ShoppingList list = new ShoppingList(newListNameInput.getText().toString());
            	
            	//insert into db
            	dba.insertList(list);
            	
            	//house keeping
                updateArray();
                newListNameInput.setText("");
                saa.notifyDataSetChanged();
            }
        });
        
        //Code to allow clickable list items
        shoppingListsView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		//capture list from position in listview
        		ShoppingList list = shoppingLists.get(position);
        		
        		//extracted Id, we can toast!
        		Toast.makeText(getApplicationContext(), "Clicked " + list.getName() + "!", Toast.LENGTH_SHORT).show();
        	}
        });

        //Code to handle long press
        registerForContextMenu(shoppingListsView);
        
        //restoreUIState();
        
        dba = new ShoppinglistDBAdapter(this);

        // Open or create the database
        dba.open();

        populateShoppingLists();
        
    }
    
    private void populateShoppingLists() {
    	c = dba.getAllLists();

    	//deprecated? let's keep using it 8^(
    	startManagingCursor(c);
    	
    	//rebuild our array which is hooked into our layout
    	updateArray();
    }
    
    private void updateArray() {
    	c.requery();
    	
    	shoppingLists.clear();
    	
    	if(c.moveToFirst())
    		do {
    			//read using cursor
    			long id = c.getLong(c.getColumnIndex("_id"));
    			String name = c.getString(c.getColumnIndex("name"));
    			long created = c.getLong(c.getColumnIndex("created"));
    			
    			//build a new object from DB data.
    			ShoppingList list = new ShoppingList(id, name, new Date(created));

    			//append to hooked arraylist
    			shoppingLists.add(0, list);
    		} while (c.moveToNext());
    	
    	saa.notifyDataSetChanged();
    }

    
    //Long press handler
    @Override
    public void onCreateContextMenu(ContextMenu menu, 
                                    View v, 
                                    ContextMenu.ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);

      menu.setHeaderTitle("Shopping list selected.");
      menu.add(0, Menu.FIRST, Menu.NONE, "Delete");
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {  
    	super.onContextItemSelected(item);
    	switch (item.getItemId()) {
    		//case DELETE
    		case (Menu.FIRST): {
    			//prepare a menu item object
    			AdapterView.AdapterContextMenuInfo menuInfo;
    			menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    			
    			//capture the positional index of our shoppping list in our list view;
    			int index = menuInfo.position;
    			
    			//resolve database id from position
    			ShoppingList list = shoppingLists.get(index);
    			long list_id = list.getId();
    			
    			//get rid!
    			deleteList(list_id);
    			return true;
    		}
    	}
    	return false;
    }
    //if we click on a long press option, what happens?\
    
    //remove a list
    private void deleteList(long id) {
    	dba.deleteList(id);
    	updateArray();
    }
}