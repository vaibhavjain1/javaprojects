<!DOCTYPE html>
<%@page import="org.search.webProject.ListFolder"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Lets Download</title>
<style type="text/css">
#navcontainer ul li {
	float: left;
}
.banner{
position: relative;
	font: 100px arial, sans-serif;
    font-weight: bold;
    color:#EE8400;
    text-decoration: none;
    left: 40px;
}
.image {
	position: relative;
	display: inline;
	margin: 8px;
}

h3 {
	position: absolute;
	top: -85px;
	left: 35px;
	color: white;
	font-weight: 600;
	font-size: 26px;
	width: 8em;
	height: 1.1em;
	overflow: hidden;
	text-overflow: ellipsis;
}
</style>
</head>
<body>
	<h3 style="text-align: center">
		<%
			if (request.getAttribute("Servlet.message") != null) {
		%>
		<%=request.getAttribute("Servlet.message")%>
		<%
			}
		%>
	</h3>
	<div><a class="banner" href="<%=request.getContextPath()%>">Download</a></div>
	<FORM action="ListFileOptionServlet" method="post">
		<div id="navcontainer">
			<B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size="25">Download</font></B>
			<ul style="list-style: none;">
				<%=ListFolder.getFolders(request.getServletContext())%>
			</ul>
		</div>
	</FORM>
	<br style="clear: both" />
	<FORM action="UploadServlet" enctype="multipart/form-data" method="post">
		<div id="navcontainer1">
			<B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size="25">Upload</font></B>
			<ul style="list-style: none;">
				<li><a href="ListFileOptionServlet?type=/uploadedfiles"> <img
						src="images/uploadedfiles.png">
				</a></li>
			</ul>
		</div>
		<B><font size="6">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Select File	to upload:&nbsp;&nbsp;</font></B>
		 <input type="file" name="fname" /> <input type="submit" value="upload" />
	</FORM>
</body>
<footer style="text-align:right">Created By : Vaibhav</footer>
</html>