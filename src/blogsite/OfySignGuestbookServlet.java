//http://first-skein-105821.appspot.com/ofyblogpost.jsp
package blogsite;
import java.util.logging.Logger;
 
import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

 

import com.googlecode.objectify.ObjectifyService;
import java.io.IOException;
import java.util.Date;

 





import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Set;

 

public class OfySignGuestbookServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(OfySignGuestbookServlet.class.getName());
	static
	{
		ObjectifyService.register(BlogPost.class);
	}

    public void doPost(HttpServletRequest req, HttpServletResponse resp)

                throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String title = req.getParameter("title");
        String blogpostName = req.getParameter("guestbookName");
        String content = req.getParameter("content");
        BlogPost g = new BlogPost(user, title, content);
        ofy().save().entity(g).now();
        resp.sendRedirect("/ofyguestbook.jsp?blogpostName=" + blogpostName);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	//UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();
        //String blogpostName = req.getParameter("blogpostName");
        //String blogpostEntry = req.getParameter("blogpostEntry");
        //if(blogpostId!=null)
        //ofy().delete().type(Greeting.class).id(blogpostEntry).now();
    	resp.sendRedirect("/listblog.jsp");
    }

}
