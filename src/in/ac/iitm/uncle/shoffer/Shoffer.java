package in.ac.iitm.uncle.shoffer;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import android.app.Application;
import android.content.IntentFilter;

public class Shoffer extends Application {
	public void onCreate(){
		super.onCreate();
		
		LocationLibrary.initialiseLibrary(getBaseContext(),3 * 60 * 1000, 5 * 60 * 1000, "in.ac.iitm.uncle.shoffer");
	    IntentFilter lftIntentFilter = new IntentFilter(LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction());
	    registerReceiver(new ProximityBroadcast(),lftIntentFilter);
	  
	}
}
