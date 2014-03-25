package in.ac.iitm.uncle.shoffer;

import java.util.List;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

public class ProximityBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Uri soundUri;
		MediaPlayer mp;
     
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mp = MediaPlayer.create(context,soundUri);
        mp.setVolume(1,1);
        mp.setLooping(false);
		DatabaseHelper db = new DatabaseHelper(context);
		final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
		List<Location> locations = db.getAllLocations();
		android.location.Location l2= new android.location.Location("reverseGeocoded");
		
		l2.setLatitude(locationInfo.lastLat);
		l2.setLongitude(locationInfo.lastLong);
	
		for(Location l:locations)
        {
			android.location.Location l1= new android.location.Location("reverseGeocoded");
			
			l1.setLatitude(Double.parseDouble(l.getLatitude()));
			l1.setLongitude(Double.parseDouble(l.getLongitude()));
			
			
			if(l2.distanceTo(l1)<=50 && l.getCount()>0 && !l.isAlerted()){
				NotificationCompat.Builder mBuilder =
				        new NotificationCompat.Builder(context)
				        .setSmallIcon(R.drawable.ic_launcher)
				        .setContentTitle("Shoffer")
				        .setContentText("Pending Tasks Nearby at "+l.getLocation());
				Intent resultIntent = new Intent(context,TaskListActivity.class);
				resultIntent.putExtra("location",l.getLocation());
				
				// The stack builder object will contain an artificial back stack for the
				// started Activity.
				// This ensures that navigating backward from the Activity leads out of
				// your application to the Home screen.
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
				// Adds the back stack for the Intent (but not the Intent itself)
				stackBuilder.addParentStack(TaskListActivity.class);
				// Adds the Intent that starts the Activity to the top of the stack
				stackBuilder.addNextIntent(resultIntent);
				PendingIntent resultPendingIntent =
				        stackBuilder.getPendingIntent(
				            0,
				            PendingIntent.FLAG_UPDATE_CURRENT
				        );
				mBuilder.setContentIntent(resultPendingIntent);
				NotificationManager mNotificationManager =
				    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(l.getID(), mBuilder.build());
		        if(soundUri!=null)
		        	mp.start();
				Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(1000);
				l.setAlerted(true);
				db.updateLocation(l);
			}
			else
				l.setAlerted(false);
				db.updateLocation(l);
		}
	}

}
