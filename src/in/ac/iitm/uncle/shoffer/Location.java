package in.ac.iitm.uncle.shoffer;

public class Location {
	int _id;
	String _location;
	String _latitude;
	String _longitude;
	int _count;
	int _alerted;
	
	public Location(){}
	
	public Location(int _id,String _location,String _latitude,String _longitude,int _count,boolean _alerted)
	{
		this._id=_id;
		this._location=_location;
		this._latitude=_latitude;
		this._longitude=_longitude;
		this._count=_count;
		this._alerted=(_alerted)?1:0;
	}
	
	public int getID(){return this._id;}
	public void setID(int id){this._id=id;}
	
	public String getLocation(){return this._location;}
	public void setLocation(String location){this._location=location;}
	
	public String getLatitude(){return this._latitude;}
	public void setLatitude(String latitude){this._latitude=latitude;}
	
	public String getLongitude(){return this._longitude;}
	public void setLongitude(String longitude){this._longitude=longitude;}
	
	public int getCount(){return this._count;}
	public void setCount(int count){this._count=count;}
	
	public boolean isAlerted() {return (this._alerted==1);}
	public void setAlerted(boolean _alerted) {this._alerted=(_alerted)?1:0;}
}
