package in.ac.iitm.uncle.shoffer;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LocationListAdapter extends BaseAdapter {

	private Context context;
	private List<Location> locations;
	
	public LocationListAdapter(Context context,List<Location> locations) {
		this.context=context;
		this.locations=locations;
	}
	
	@Override
	public int getCount() {
		return locations.size();
	}

	@Override
	public Object getItem(int arg0) {
		return locations.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		LayoutInflater li = ((Activity)context).getLayoutInflater();
		v = li.inflate(R.layout.locations_list_element,parent,false);
		Location l = locations.get(position);
    	TextView t = (TextView) v.findViewById(R.id.location);
    	t.setText(l.getLocation());
		return v;
	}
}
