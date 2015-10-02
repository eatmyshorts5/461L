package blogsite;
import java.util.logging.Logger;







import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.mail.BounceNotification;
import com.google.appengine.api.mail.BounceNotificationParser;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

 

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.io.IOException;
import java.util.Date;

 






import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Set;

 

public class BounceHandlerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(BounceHandlerServlet.class.getName());
	static
	{
		ObjectifyService.register(Subscriber.class);
	}

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	try
    	{
    		BounceNotification bounce = BounceNotificationParser.parse(req);
    		String email = bounce.getOriginal().getFrom();
    		List<Subscriber> subs = ofy().load().type(Subscriber.class).list();
        	for(Subscriber s:subs)
        	{
        		if(s.getEmail().equals(email))
        		{
        			ofy().delete().entity(s).now();
        			break;
        		}
        		
        	}
    	}
    	catch(MessagingException e)
    	{
    		
    	}	
    	//resp.sendRedirect("/ofyguestbook.jsp?subscribed=false");
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	//UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();
        //String blogpostName = req.getParameter("blogpostName");
    	//resp.sendRedirect("/whatever");
    }

}