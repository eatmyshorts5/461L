package blogsite;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		try
		{
			UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();
			_logger.info("Cron Job has been executed by user " + user.getEmail());
			
			//Put my logic here
			List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
			//email every user in subscribers
			List<BlogPost> posts = ObjectifyService.ofy().load().type(BlogPost.class).list();
			Collections.sort(posts);
			Collections.reverse(posts);
			_logger.info("before loop");
			for(Subscriber s:subscribers)
			{	
				_logger.info("Sending e-mail to " + s.getEmail());
					Message msg = new MimeMessage(session);
			    	msg.setFrom(new InternetAddress("first-skein-105821@appspotmail.com", "appspot.com"));
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s.getEmail(), s.getUser().getNickname()));
					msg.setSubject("Blog update");
					String msgBody = "yo";
					//for(BlogPost post:posts)
					//{
						//if(post.getDate().after(s.getLastDate()))
						//{
					//		msgBody += post.getTitle() + "\nby " + post.getUser().getNickname() + post.getContent() + "\nposted on " + post.getDate();
					//		s.setDate(new Date());
						//}
						//else
						//{
						//	break;
						//}
					//}
					//msgBody = "";
					msg.setText(msgBody);
					Transport.send(msg);
				
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
