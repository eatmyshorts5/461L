//http://first-skein-105821.appspot.com/ofyguestbook.jsp
package guestbook;
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
		ObjectifyService.register(Greeting.class);
	}

    public void doPost(HttpServletRequest req, HttpServletResponse resp)

                throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String title = req.getParameter("title");
        String guestbookName = req.getParameter("guestbookName");
        String content = req.getParameter("content");
        Greeting g = new Greeting(user, title, content);
        ofy().save().entity(g).now();
        resp.sendRedirect("/ofyguestbook.jsp?guestbookName=" + guestbookName);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	//UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();
        //String guestbookName = req.getParameter("guestbookName");
        //String guestbookEntry = req.getParameter("guestbookEntry");
        //if(guestbookId!=null)
        //ofy().delete().type(Greeting.class).id(guestbookEntry).now();
    }

}