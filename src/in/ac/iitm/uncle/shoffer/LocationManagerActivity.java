package in.ac.iitm.uncle.shoffer;

import java.util.List;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LocationManagerActivity extends Activity {

	DatabaseHelper db;
	ListView locationsList;
	Button addButton;
	Button doneButton;
	LocationListAdapter adapter;
	int respCode;
	List<Location> locations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_manager);
        db=new DatabaseHelper(this);
        locationsList = (ListView) findViewById(R.id.locationsView);
        locations = db.getAllLocations();
        adapter = new LocationListAdapter(this,locations);
        addButton = (Button) findViewById(R.id.addButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        locationsList.setItemsCanFocus(true);
        locationsList.setClickable(true);
        locationsList.setSelected(true);
        locationsList.setAdapter(adapter);
        locationsList.setOnItemClickListener(new OnItemClickListener(){

   
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				in.ac.iitm.uncle.shoffer.Location l = locations.get(arg2);
				if(l.getCount()>0)
					
					Toast.makeText(arg1.getContext(), "Cannot delete location as it has associated tasks",Toast.LENGTH_SHORT).show();
				else {
					db.deleteLocation(l);
					locations.clear();
					locations.addAll(db.getAllLocations());
					adapter.notifyDataSetChanged();
				}
			}
        	
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setResult(2);
				finish();
				
			}
		});
        addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent data = new Intent("in.ac.iitm.uncle.shoffer.ADDLOCATION");
				startActivityForResult(data,1);
			}
		});
    }
    
    public void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
    	respCode = resultCode;
    	if(requestCode==1)
    	{
    		if(resultCode==RESULT_OK)
    		{
    			LocationInfo latestInfo = new LocationInfo(this); 
    			String locName = intent.getStringExtra("name");
    			Location l = new Location();
    			l.setLocation(locName);
    			l.setLatitude(String.valueOf(latestInfo.lastLat));
    			l.setLongitude(String.valueOf(latestInfo.lastLong));
    			l.setCount(0);
    			db.addLocation(l);
    			locations.clear();
    			locations.addAll(db.getAllLocations());
    			adapter.notifyDataSetChanged();
					
    		}
    	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_location_manager, menu);
        return true;
    }
}
