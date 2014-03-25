package in.ac.iitm.uncle.shoffer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;

public class AddTaskActivity extends Activity {

	Button addButton;
	Button cancelButton;	
	EditText task;
	Spinner location;
	ArrayAdapter<Location> adapter;
	boolean edit=false;
	List<Location> locations;
	DatabaseHelper db;
	Location l1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        addButton = (Button) findViewById(R.id.okButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        task = (EditText) findViewById(R.id.task);
        db = new DatabaseHelper(this);
		l1 = new Location();
		l1.setLocation("Manage Locations...");
		l1.setCount(0);
		l1.setLatitude("0");
		l1.setLongitude("0");
        locations = db.getAllLocations();
        locations.add(l1);
        
        
        adapter=new ArrayAdapter<Location>(this,android.R.layout.simple_spinner_item,locations){
        	@Override
        	public View getView(int position,View convertView,ViewGroup	parent) {
        		TextView view = (TextView) super.getView(position, convertView, parent);
        		view.setText(locations.get(position).getLocation());
        		return view;
        	}
        	
        	@Override
        	public View getDropDownView(int position,View convertView,ViewGroup parent) {
        		CheckedTextView view = (CheckedTextView) super.getDropDownView(position, convertView, parent);
        		view.setText(locations.get(position).getLocation());
				return view;
        	}
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location = (Spinner) findViewById(R.id.locationSpinner);
        location.setAdapter(adapter);
        Bundle ext = getIntent().getExtras();
        if(ext!=null)
        {
        	edit=true;
        	int pos=0;
        	for(Location l:locations)
        	{
        		if(l.getLocation().equals(ext.get("location")))
        			break;
        		pos++;
        	}
        	location.setSelection(pos);
        }
        
        location.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2==(adapter.getCount()-1)){
					Intent i = new Intent("in.ac.iitm.uncle.shoffer.LOCATIONMANAGER");
					locations.clear();
					startActivityForResult(i,1);
					adapter.notifyDataSetChanged();
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent data = new Intent();
				setResult(RESULT_CANCELED,data);
				finish();
			}
		});
        
        addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent data = new Intent();
				Bundle results  = new Bundle();
				String s = task.getText().toString().trim();
				if(s.length()<=0)
				{
					Toast.makeText(arg0.getContext(),"Please enter a task",Toast.LENGTH_SHORT).show();
				}
				else if(location.getSelectedItem()==null)
				{
					Toast.makeText(arg0.getContext(),"Please select a location",Toast.LENGTH_SHORT).show();
				}
				else
				{
					results.putString("task",s);
					String loc = ((Location)location.getSelectedItem()).getLocation();
					results.putString("location",loc);
					data.putExtras(results);
					setResult(RESULT_OK,data);
					finish();
				}
			}
		});
    }

    public void onActivityResult(int acode,int bcode,Intent data) {
		locations.addAll(db.getAllLocations());
		locations.add(l1);
		adapter.notifyDataSetChanged();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_task, menu);
        return true;
    }
}
