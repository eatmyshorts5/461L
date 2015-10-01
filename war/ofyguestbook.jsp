<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="com.google.appengine.api.users.User" %>

<%@ page import="com.google.appengine.api.users.UserService" %>

<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import = "java.util.Collections"%>
<%@ page import = "com.googlecode.objectify.*"%>
<%@ page import = "blogsite.BlogPost" %>


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

    String guestbookName = request.getParameter("guestbookName");

    if (guestbookName == null) {

        guestbookName = "default";

    }

    pageContext.setAttribute("guestbookName", guestbookName);

    UserService userService = UserServiceFactory.getUserService();

    User user = userService.getCurrentUser();

    if (user != null) {

      pageContext.setAttribute("user", user);

%>

<p align="right">Signed in as ${fn:escapeXml(user.nickname)}

<br>Click here to <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out.</a></p>

<%

    } else {

%>

<p align="right"><a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to post.</p>

<%

    }

%>
</div>
<img class ="resize" style="float:left" src="/kappa.png" /> 

<div class="transbox1">
<%

	ObjectifyService.register(BlogPost.class);
	List<BlogPost> blogposts = ObjectifyService.ofy().load().type(BlogPost.class).list();
	Collections.sort(blogposts);
	Collections.reverse(blogposts);

    // Run an ancestor query to ensure we see the most up-to-date

    // view of the BlogPosts belonging to the selected Guestbook.


    
    if (blogposts.isEmpty()) {

        %>

        <p><br>Guestbook '${fn:escapeXml(guestbookName)}' has no messages.<br><br></p>

        <%

    } else {

        %>

        <p><br>Messages in Guestbook '${fn:escapeXml(guestbookName)}'.</p>

        <%

        for (int i = 0; i < blogposts.size(); i++) {%>
        	<div class="break">
        	<%
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

    }

%>

 
<%
	if(user!= null){%>
    <form action="/blog" method="post">
	  <div><textarea name="title" rows="1" cols="50" placeholder ="Title"></textarea></div>
      <div><textarea name="content" rows="3" cols="60" placeholder ="Say stoofz here." required></textarea></div>

      <div><input type="submit" value="Post BlogPost" /></div>

      <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>

    </form>
	<%} %>
	<form action="/listblog.jsp" method="get">


      <div><input type="submit" value="View All" /></div>
		
      

    </form>
    </div>
  </div>  
    
 

  </body>

</html>
