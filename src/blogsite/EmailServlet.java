package blogsite;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import com.google.apphosting.api.ApiProxy;
import com.googlecode.objectify.ObjectifyService;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SuppressWarnings("serial")
public class EmailServlet extends HttpServlet{
	static
	{
		ObjectifyService.register(Subscriber.class);
		ObjectifyService.register(BlogPost.class);
	}
	private static final Logger _logger = Logger.getLogger(EmailServlet.class.getName());
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		_logger.info("Cron Job begin " + SystemProperty.applicationId.get());
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		try
		{
			//UserService userService = UserServiceFactory.getUserService();
	        //User user = userService.getCurrentUser();
			//_logger.info("Cron Job has been executed by user " + user.getEmail());
			
			//Put my logic here
			List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
			//email every user in subscribers
			List<BlogPost> posts = ObjectifyService.ofy().load().type(BlogPost.class).list();
			Collections.sort(posts);
			Collections.reverse(posts);
			_logger.info("my id is " + SystemProperty.applicationId.get());
			
			Date oneDayB4 = new Date(System.currentTimeMillis() - 24*3600*1000);//cal.getTime();
			for(Subscriber s:subscribers)
			{	
				String msgBody = "";
				
				for(BlogPost post:posts)
				{
					
					if(post.getDate().after(oneDayB4))
					{
						String blogpost = "<b>" + post.getTitle() + "</b><br>by " + post.getUser().getNickname() + ". Posted on " + post.getDate() + "<br>" + post.getContent() + "<br><br>";
						msgBody += blogpost;
					}
					else
					{
						break;
					}
				}
				_logger.info(msgBody);
				s.setDate(new Date());
				_logger.info(s.getLastDate().toString());
				if(msgBody.length() != 0)
				{
					msgBody = "Posts in the last 24 hours:<br><br>" + msgBody;
					_logger.info("Sending e-mail to " );
					Message msg = new MimeMessage(session);//SystemProperty.applicationId.get()
			    	msg.setFrom(new InternetAddress("anything@" + SystemProperty.applicationId.get() + ".appspotmail.com", "Blog Kappa"));
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s.getEmail(), s.getUser().getNickname()));
					msg.setSubject("Blog update");
					msg.setContent(msgBody, "text/html");
					//msg.setText(msgBody);
					Transport.send(msg);
				}
				
				//System.out.println("Email sent");
			}
		}
		catch(Exception ex)
		{
			_logger.info("Error");
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doGet(req, resp);
	}
}
