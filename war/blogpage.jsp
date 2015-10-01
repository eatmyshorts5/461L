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
	int count = 0;
    String index = request.getParameter("index");

    if (index == null) {

        count  = 0;

    } else {
    	count = Integer.parseInt(index);
    }

    pageContext.setAttribute("index", index);

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
	
	
	BlogPost blogpost = blogposts.get(count);
	pageContext.setAttribute("blogpost_content", blogpost.getContent());
	pageContext.setAttribute("blog_title",blogpost.getTitle());
	pageContext.setAttribute("blog_date", blogpost.getDate());
	
	if (blogpost.getUser() == null) {

        %>

		<header style="font-size:20px">${fn:escapeXml(blog_title)}
		<h6 style="font-size:10px">by Anonymous. Posted on ${fn:escapeXml(blog_date)}</h6>
		</header>

        <%

    } else {

        pageContext.setAttribute("blogpost_user", blogpost.getUser());

        %>
		<header style="font-size:20px">${fn:escapeXml(blog_title)}
		<h6 style="font-size:10px">by ${fn:escapeXml(blogpost_user.nickname)}. Posted on ${fn:escapeXml(blog_date)}</h6>
		</header>
		
        <%

    }
	
	%>

    <blockquote>${fn:escapeXml(blogpost_content)}</blockquote>	
    
	</div>
	</div>
  </body>

</html>
