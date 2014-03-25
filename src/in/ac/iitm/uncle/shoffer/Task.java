package in.ac.iitm.uncle.shoffer;

public class Task extends Object{
	int _id;
	String _task;
	String _location;
	int _done;
	
	public Task(){}
	
	public Task(int _id,String _task,String _location,int _done) {
		this._id=_id;
		this._task=_task;
		this._location=_location;
		this._done=_done;
	}
	
	public int getID(){return this._id;}
	public void setID(int id){this._id=id;}
	
	public String getTask(){return this._task;}
	public void setTask(String task){this._task=task;}
	
	public String getLocation(){return this._location;}
	public void setLocation(String location){this._location=location;}
	
	public int isDone(){return this._done;}
	public void setDone(int done){this._done=done;}
	
	/*@Override
	public boolean equals(Object o){
		Task t = (Task) o;
		if(t.getID()==_id && t.getTask().equals(_task) && t.getLocation().equals(_location) && t.isDone()==_done)
			return true;
		else
			return false;
	}*/
}
