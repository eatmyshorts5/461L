<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="com.google.appengine.api.users.User" %>

<%@ page import="com.google.appengine.api.users.UserService" %>

<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import = "java.util.Collections"%>
<%@ page import = "java.util.Date"%>
<%@ page import = "com.googlecode.objectify.*"%>
<%@ page import = "blogsite.BlogPost" %>
<%@ page import = "blogsite.Subscriber" %>


<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

 

<html>

  <head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>

 

  <body>


<div class="background">
<div class="transbox">
<br><br>
<%

    UserService userService = UserServiceFactory.getUserService();

    User user = userService.getCurrentUser();

    if (user != null) {

      pageContext.setAttribute("user", user);

%>

<p class="nitpick" style="float:right">Signed in as ${fn:escapeXml(user.nickname)}

<br>Click <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">here</a> to sign out.</p>

<%

    } else {

%>

<p class="nitpick" style="float:right">Hello guest!<br>Click <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">here</a> to sign in.</p>

<%

    }

%>
</div>
<img class ="resize" style="float:left" src="/kappa.png" />
<p class="headerr" style="float:left">Blog Kappa</p>

<div class="transbox1">
<%

	ObjectifyService.register(BlogPost.class);
	List<BlogPost> blogposts = ObjectifyService.ofy().load().type(BlogPost.class).list();
	Collections.sort(blogposts);
	Collections.reverse(blogposts);
    // Run an ancestor query to ensure we see the most up-to-date

    // view of the BlogPosts belonging to the selected Guestbook.
    ObjectifyService.register(Subscriber.class);
	List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
	Subscriber subbedUser = null;
    boolean subscribed = false;
	
    if(user != null)
    {
    	for(Subscriber sub:subscribers)
    	{
    		if(sub.getEmail().equals(user.getEmail()))
    		{
    			sub.setDate(new Date());
    			subbedUser = sub;
    			subscribed = true;
    			break;
    		}
    	}
    }
	
    
	%>

    <p><br>Welcome<%if(user != null) 
         	{%>, ${fn:escapeXml(user.nickname)}<%}%>!<%if (blogposts.isEmpty()) { 

         %> Be the first to post!<%}%>
         <%if(user != null){%> Click <a href="/blog?subscribed=<%=subscribed%>">here</a> to <%if(subscribed){%>unsubscribe from<%}else{%>subscribe to<%}%> the e-mail digest (update sent every day at 5:00pm Central Time).<%} %></p> 
    
        <%
        int count = 0;
        for (int i = 0; i < blogposts.size(); i++) {%>
        	<div class="break">
        	<%
        	if(blogposts.size() == 0)
        	{
        		break;
        	}
			if (count == 5)
				break;
        	count += 1;
			BlogPost blogpost = blogposts.get(i);
            pageContext.setAttribute("blogpost_content", blogpost.getContent());
			pageContext.setAttribute("blog_title",blogpost.getTitle());
			pageContext.setAttribute("blog_date", blogpost.getDate());
            if (blogpost.getUser() == null) {

                %>
                <header style="font-size:30px"><br><br><br><br><br><a href="/blogpage.jsp">${fn: escapeXml(blog_title)}</a>
				<h6 style="font-size:10px">by Anonymous. Posted on ${fn:escapeXml(blog_date)}</h6>
				</header>

                <%

            } else {

                pageContext.setAttribute("blogpost_user", blogpost.getUser());

                %>
				<header style="font-size:30px"><a href="/blogpage.jsp">${fn: escapeXml(blog_title)}</a>
				<h6 style="font-size:10px">by ${fn:escapeXml(blogpost_user.nickname)}. Posted on ${fn:escapeXml(blog_date)}</h6>
				</header>
			
                <%
			
            	}

            %>

            <blockquote>${fn:escapeXml(blogpost_content)}</blockquote>	
            </div>	
            <%

        }

%>

 
<%
	if(user!= null){%>
    <form action="/blog" method="post">
	  <div><textarea name="title" rows="1" cols="50" placeholder ="Title"></textarea></div>
      <div><textarea name="content" rows="3" cols="60" placeholder ="Say stoofz here." required></textarea></div>

      <div><input type="submit" value="Post BlogPost" /></div>
		<input type="hidden" name="postButton" value="post"/>
    </form>
	<%} %>
	<form action="/listblog.jsp" method="get">


      <div><input type="submit" value="View All" /></div>
		
      

    </form>
    </div>
  </div>  
    
 

  </body>

</html>
