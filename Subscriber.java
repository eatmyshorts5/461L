package blogsite;

import java.util.Date;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity

public class Subscriber implements Comparable<Subscriber>{
	@Id Long id;
	User user;
	Date lastDate; //date of last seen post
	Date subDate;
	
	private Subscriber(){}
	
	public Subscriber(User user)
	{
		this.user = user;
		lastDate = new Date();
		subDate = new Date();
	}
	
	public String getEmail()
	{
		return user.getEmail();
	}
	
	public void setDate(Date date)
	{
		lastDate = date;
	}
	
	public Long getId()
	{
		return id;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public Date getLastDate()
	{
		return lastDate;
	}
	
	@Override
	
	public int compareTo(Subscriber other)
	{
		if(subDate.after(other.subDate))
		{
			return 1;
		}
		else if(subDate.before(other.subDate))
		{
			return -1;
		}
		return 0;
	}
}
