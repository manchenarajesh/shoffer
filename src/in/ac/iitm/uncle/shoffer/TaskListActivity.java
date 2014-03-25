package in.ac.iitm.uncle.shoffer;

import java.util.List;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class TaskListActivity extends Activity {

	Button addButton;
	Button delButton;
	ListView taskList;
	TaskListAdapter adapter;
	List<Task> tasks;
	DatabaseHelper db;
	String loc;
	LocationManager lm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        LocationLibrary.forceLocationUpdate(this);
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
        	 loc = extras.getString("location");
        }
        addButton = (Button) findViewById(R.id.addButton);
        delButton = (Button) findViewById(R.id.delButton);
        taskList = (ListView) findViewById(R.id.taskList);
        db = new DatabaseHelper(this);
        if(loc==null)
        	tasks = db.getAllTasks();
        else
        	tasks = db.getTasksByLocation(loc);
        adapter = new TaskListAdapter(this,tasks);
        taskList.setAdapter(adapter);
        
        addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent("in.ac.iitm.uncle.shoffer.ADDTASK");
				startActivityForResult(i,1);
			}
		});
        
        delButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				for(int i=0;i<tasks.size();i++) {
					Task t = tasks.get(i);
					if(t.isDone()==1)
						db.deleteTask(t);
				}
				tasks.clear();
				tasks.addAll(db.getAllTasks());
				adapter.notifyDataSetChanged();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task_list, menu);
        return true;
    }
    
    @Override
    public void onActivityResult(int requestCode,int respCode,Intent data){
    	if(requestCode==1) {
    		if(respCode==RESULT_OK) {
    			
    			Bundle b = data.getExtras();
    			Task t = new Task();
    			t.setTask(b.getString("task"));
    			t.setLocation(b.getString("location"));
    			t.setDone(0);
    			t.setID(-1);
    			db.addTask(t);
    			tasks.clear();
    			tasks.addAll(db.getAllTasks());
    			adapter.notifyDataSetChanged();
    		}
    	}
    }
}
