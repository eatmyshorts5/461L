//http://first-skein-105821.appspot.com/ofyblogpost.jsp
package blogsite;
import java.util.logging.Logger;
 





import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

 

import com.google.appengine.api.utils.SystemProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

 






import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
        
        if(user != null)
        {
        	if(req.getParameter("postButton") != null)
            {
            	String title = req.getParameter("title");
            	//String blogpostName = req.getParameter("guestbookName");
            	String content = req.getParameter("content");
    		    BlogPost g = new BlogPost(user, title, content);
    		    ofy().save().entity(g).now();
    		    resp.sendRedirect("/ofyguestbook.jsp");
            }
            else if(req.getParameter("delete") != null)
            {
            	//log.info("delete");
            	try
            	{
            		
            		log.info("size is " + ofy().load().type(BlogPost.class).list().size() + " and blog id is " + req.getParameter("blog_id"));
            		int key = Integer.parseInt(req.getParameter("blog_id"));
            		ofy().delete().key(Key.create(BlogPost.class, key)).now();
            		log.info("size is " + ofy().load().type(BlogPost.class).list().size());
            	}
            	catch (Exception e)
            	{
            		e.printStackTrace();
            		//log.info("parsing error");
            		//idk
            	}
            	resp.sendRedirect("/ofyguestbook.jsp");
            }

        }
                
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	//doPost(req,resp);
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if(user != null)
        {
        	if(req.getParameter("subscribed") != null)
            {
            	if(req.getParameter("subscribed").equals("true"))
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
            	else
            	{
            		List<Subscriber> subs = ofy().load().type(Subscriber.class).list();
                	for(Subscriber sub:subs)
                	{
                		if(sub.getEmail().equals(user.getEmail()))
                		{
                			resp.sendRedirect("/ofyguestbook.jsp?alreadysubscribed=true");
                		}
                	}
                	Subscriber s = new Subscriber(user);
                	ofy().save().entity(s).now();
                	resp.sendRedirect("/ofyguestbook.jsp?subscribed=true");
            	}
            }
        }
        else
        {
        	resp.sendRedirect("/ofyguestbook.jsp?subscribed=false");
        }
    	
    }

}
