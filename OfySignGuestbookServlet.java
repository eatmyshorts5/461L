//http://first-skein-105821.appspot.com/ofyblogpost.jsp
package blogsite;
import java.util.logging.Logger;
 



import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

 

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.io.IOException;
import java.util.Date;

 






import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Set;

 

public class OfySignGuestbookServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(OfySignGuestbookServlet.class.getName());
	static
	{
		ObjectifyService.register(BlogPost.class);
		ObjectifyService.register(Subscriber.class);
	}

    public void doPost(HttpServletRequest req, HttpServletResponse resp)

                throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if(req.getParameter("sub") != null)
        {
        	Subscriber s = new Subscriber(user);
        	ofy().save().entity(s).now();
        	resp.sendRedirect("/ofyguestbook.jsp?subscribed=true");
        }
        else if(req.getParameter("unsub") != null)
        {
        	List<Subscriber> subs = ofy().load().type(Subscriber.class).list();
        	for(Subscriber s:subs)
        	{
        		if(s.getEmail().equals(user.getEmail()))
        		{
        			ofy().delete().entity(s).now();
        			break;
        		}
        		
        	}
        	
        	resp.sendRedirect("/ofyguestbook.jsp?subscribed=false");
        }
        else if(req.getParameter("post") != null)
        {
        	String title = req.getParameter("title");
        	String blogpostName = req.getParameter("guestbookName");
        	String content = req.getParameter("content");
		    BlogPost g = new BlogPost(user, title, content);
		    ofy().save().entity(g).now();
		    resp.sendRedirect("/ofyguestbook.jsp?blogpostName=" + blogpostName);
        }
        
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	//UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();
        //String blogpostName = req.getParameter("blogpostName");
    	resp.sendRedirect("/whatever");
    }

}
