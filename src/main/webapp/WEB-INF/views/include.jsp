<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/portal.png">
<link href="${pageContext.request.contextPath}/resources/css/jquery.dataTables.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/prettify.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery-ui.css" />
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/timeout-dialog.css" /> --%>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.8.14.custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery.jalert.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery.dataTables.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/prettify.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery.multiselect.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/json2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery.tmpl.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery-idleTimeout.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery.mtz.monthpicker.js"></script>

<style type="text/css">
body {
padding-top: 0em;
}
.selectlabel {
color: #1C94C4;
font-weight: bold;
display: block;
width: 150px;
float: center;
}
.center{
  align: center;
}
.fullfont{font:20px Helvetica, arial, sans-serif;font-weight: bold; display: block; width: 400px; padding: 0px 0px 0px 550px; color: rgb(255, 0, 0); height: 100px;}
</style>
<script type="text/javascript">
function initSessionTimeout()
{
	$(document).idleTimeout({
		inactivity: 1500000,
		noconfirm: 30000,
		sessionAlive: false,
		click_reset:true,
		alive_url:'/resources//images/portal.png',
		redirect_url:'/resources/logout',
		logout_url:false
		}); 
}

</script>