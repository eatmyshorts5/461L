package blogsite;

import java.util.Date;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity

public class BlogPost implements Comparable<BlogPost>{
	@Id Long id;
	User user;
	String content;
	Date date;
	String title;
	
	private BlogPost(){}
	public BlogPost(User user, String content)
	{
		this.user = user;
		this.content = content;
		date = new Date();
	}
	
	public BlogPost(User user, String title, String content)
	{
		this(user,content);
		this.title = title;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public Long getId()
	{
		return id;
	}
	
	@Override
	
	public int compareTo(BlogPost other)
	{
		if(date.after(other.date))
		{
			return 1;
		}
		else if(date.before(other.date))
		{
			return -1;
		}
		return 0;
	}

}
