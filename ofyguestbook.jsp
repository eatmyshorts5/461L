<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="com.google.appengine.api.users.User" %>

<%@ page import="com.google.appengine.api.users.UserService" %>

<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import = "java.util.Collections"%>
<%@ page import = "com.googlecode.objectify.*"%>
<%@ page import = "guestbook.Greeting" %>


<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

 

<html>

  <head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>

 

  <body>

 

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

<p>Signed in as ${fn:escapeXml(user.nickname)}

<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out.</a>)</p>

<%

    } else {

%>

<p><a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to post.</p>

<%

    }

%>

 

<%

	ObjectifyService.register(Greeting.class);
	List<Greeting> greetings = ObjectifyService.ofy().load().type(Greeting.class).list();
	Collections.sort(greetings);
	Collections.reverse(greetings);

    // Run an ancestor query to ensure we see the most up-to-date

    // view of the Greetings belonging to the selected Guestbook.


    if (greetings.isEmpty()) {

        %>

        <p>Guestbook '${fn:escapeXml(guestbookName)}' has no messages.</p>

        <%

    } else {

        %>

        <p>Messages in Guestbook '${fn:escapeXml(guestbookName)}'.</p>

        <%

        for (int i = 0; i < 5; i++) {
			Greeting greeting = greetings.get(i);
            pageContext.setAttribute("greeting_content", greeting.getContent());
			pageContext.setAttribute("blog_title",greeting.getTitle());
			pageContext.setAttribute("blog_date", greeting.getDate());
            if (greeting.getUser() == null) {

                %>

                <p>An anonymous person wrote:</p>

                <%

            } else {

                pageContext.setAttribute("greeting_user", greeting.getUser());

                %>
				<header style="font-size:30px">${fn:escapeXml(blog_title)}
				<h6 style="font-size:10px">by ${fn:escapeXml(greeting_user.nickname)}</h6>
				</header>
				
                <%

            }

            %>

            <blockquote>${fn:escapeXml(greeting_content)}</blockquote>
			<footer style="font-size:12px">posted ${fn:escapeXml(blog_date)}</footer>	
            <%

        }

    }

%>

 
<%
	if(user!= null){%>
    <form action="/ofysign" method="post">
	  <div><textarea name="title" rows="1" cols="50" placeholder ="Title"></textarea></div>
      <div><textarea name="content" rows="3" cols="60" placeholder ="Say stoofz here." required></textarea></div>

      <div><input type="submit" value="Post Greeting" /></div>

      <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>

    </form>
	<%} %>
	<form action="/ofysign" method="get">


      <div><input type="submit" value="View All" /></div>
		
      

    </form>
 

  </body>

</html>