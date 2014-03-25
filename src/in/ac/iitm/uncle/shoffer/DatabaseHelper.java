package in.ac.iitm.uncle.shoffer;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION=4;
	private static final String DATABASE_NAME="Shoffer";
	
	private static final String TABLE_LOCATIONS="locations";
	private static final String TABLE_TASKS="tasks";

		
	private static final String KEY_ID="_id";
	private static final String KEY_LOCATION="_location";
	private static final String KEY_LATITUDE="_latitude";
	private static final String KEY_LONGITUDE="_longitude";
	private static final String KEY_COUNT="_count";
	private static final String KEY_ALERTED="_alerted";
	
	private static final String KEY_TASK="_task";
	private static final String KEY_DONE="_done";
	Context context;
	public DatabaseHelper(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		this.context=context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+TABLE_LOCATIONS+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_LOCATION+" TEXT,"+KEY_LATITUDE+" TEXT," +KEY_LONGITUDE+" TEXT,"+KEY_COUNT+" INTEGER DEFAULT 0 ,"+KEY_ALERTED+"  INTEGER DEFAULT 0);");
		db.execSQL("CREATE TABLE "+TABLE_TASKS+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_TASK+" TEXT,"+KEY_LOCATION+" TEXT,"+KEY_DONE+" INTEGER DEFAULT 0);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE "+TABLE_LOCATIONS+" ADD COLUMN "+KEY_ALERTED+" INTEGER DEFAULT 0;");
		//db.execSQL("DROP TABLE "+TABLE_TASKS+";");
		//onCreate(db);
	}

	public void addLocation(Location location)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LOCATION, location.getLocation());
		values.put(KEY_LATITUDE, location.getLatitude());
		values.put(KEY_LONGITUDE, location.getLongitude());
		values.put(KEY_COUNT,location.getCount());
		values.put(KEY_ALERTED,(location.isAlerted())?1:0);
		db.insertOrThrow(TABLE_LOCATIONS, KEY_LOCATION, values);
		db.close();
	}
	
	public Location getLocation(String locationName)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_LOCATIONS, new String[] {KEY_ID,KEY_LOCATION,KEY_LATITUDE,KEY_LONGITUDE,KEY_COUNT,KEY_ALERTED},KEY_LOCATION+"=?",new String[] {locationName},null,null,null,null);
		
		if(cursor!=null)
			cursor.moveToFirst();
		
		Location l = new Location(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),(cursor.getInt(5)==1));
		db.close();
		return l;
	}
	
 public List<Location> getAllLocations() {
	 List<Location> locationList = new ArrayList<Location>();
	 String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
	 
	 SQLiteDatabase db = this.getWritableDatabase();
	 Cursor cursor = db.rawQuery(selectQuery, null);
	 
	 if (cursor.moveToFirst()) {
		 do {
			 Location location = new Location();
			 location.setID(cursor.getInt(0));
	         location.setLocation(cursor.getString(1));
	         location.setLatitude(cursor.getString(2));
	         location.setLongitude(cursor.getString(3));
	         location.setCount(cursor.getInt(4));
	         location.setAlerted((cursor.getInt(5)==1));
	         locationList.add(location);
		 } while (cursor.moveToNext());
	 }
	 db.close();
	 return locationList;
 	}
 
 public int updateLocation(Location location) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_LOCATION, location.getLocation());
	    values.put(KEY_LATITUDE, location.getLatitude());
	    values.put(KEY_LONGITUDE, location.getLongitude());
	    values.put(KEY_COUNT, location.getCount());
		values.put(KEY_ALERTED,(location.isAlerted())?1:0);
		
	    int id= db.update(TABLE_LOCATIONS, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(location.getID()) });
	    db.close();
	    return id;
	}
 
 public void deleteLocation(Location location) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_LOCATIONS, KEY_ID + " = ?",
	            new String[] { String.valueOf(location.getID()) });
	    db.close();
	}
 
 public void addTask(Task task)	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TASK, task.getTask());
		values.put(KEY_LOCATION, task.getLocation());
		values.put(KEY_DONE, task.isDone());
		try{
			db.insertOrThrow(TABLE_TASKS,KEY_TASK, values);
			Location l = getLocation(task.getLocation());
			l.setCount(l.getCount()+1);
			updateLocation(l);
		} catch(SQLException e){
			
		}
		db.close();
	}
	
 public Task getTask(String task) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASKS, new String[] {KEY_ID,KEY_TASK,KEY_LOCATION,KEY_DONE},KEY_TASK+"=?",new String[] {task},null,null,null,null);
		
		if(cursor!=null)
			cursor.moveToFirst();
		
		Task t = new Task(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3));
		db.close();
		return t;
	}
	
 public List<Task> getAllTasks() {
		 List<Task> tasksList = new ArrayList<Task>();
		 String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
		 
		 SQLiteDatabase db = this.getWritableDatabase();
		 Cursor cursor = db.rawQuery(selectQuery, null);
		 
		 if (cursor.moveToFirst()) {
			 do {
				 Task task = new Task();
				 task.setID(cursor.getInt(0));
				 task.setTask(cursor.getString(1));
		         task.setLocation(cursor.getString(2));
		         task.setDone(cursor.getInt(3));
		         tasksList.add(task);
			 } while (cursor.moveToNext());
		 }
		 db.close();
		 return tasksList;
	}
 
 public List<Task> getTasksByLocation(String location){
	 List<Task> tasksList = new ArrayList<Task>();
	 String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE "+KEY_LOCATION+"=\""+location+"\";";
	 
	 SQLiteDatabase db = this.getWritableDatabase();
	 Cursor cursor = db.rawQuery(selectQuery, null);
	 
	 if(cursor.moveToNext()) {
		 do {
			 Task task = new Task();
			 task.setID(cursor.getInt(0));
			 task.setTask(cursor.getString(1));
	         task.setLocation(cursor.getString(2));
	         task.setDone(cursor.getInt(3));
	         tasksList.add(task);
		 }while(cursor.moveToNext());
	 }
	 db.close();
	 return tasksList;
 }
 
 public int updateTask(Task task) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_TASK, task.getTask());
	    values.put(KEY_LOCATION, task.getLocation());
	    values.put(KEY_DONE,task.isDone());
	    int id= db.update(TABLE_TASKS, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(task.getID()) });
	    db.close();
	    return id;
 }

 public void deleteTask(Task task) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_TASKS, KEY_ID + " = ? ",
	            new String[] { String.valueOf(task.getID()) });
	    db.close();
	    Location loc = getLocation(task.getLocation());
	    loc.setCount(loc.getCount()-1);
	    updateLocation(loc);
	 
	}

}
