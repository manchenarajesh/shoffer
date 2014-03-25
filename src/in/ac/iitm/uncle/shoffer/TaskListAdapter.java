package in.ac.iitm.uncle.shoffer;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskListAdapter extends BaseAdapter {

	private Context context;
	private List<Task> tasks;
	DatabaseHelper db;
	
	public TaskListAdapter(Context context,List<Task> tasks) {
		this.context=context;
		this.tasks=tasks;
		db = new DatabaseHelper(context);
	}
	
	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int arg0) {
		return tasks.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater li = ((Activity)context).getLayoutInflater();
		v = li.inflate(R.layout.list_element,parent,false);
		Task t = tasks.get(position);
    	final CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
    	cb.setText(t.getTask());
		cb.setTag(position);
    	cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				int pos = (Integer) cb.getTag();
				Task t = tasks.get(pos);
				if(arg1)
					t.setDone(1);
				else
					t.setDone(0);
				notifyDataSetChanged();
			}
    		
    	});
		if(t.isDone()==1)
			cb.setChecked(true);
		else
			cb.setChecked(false);
		return v;
	}
}
