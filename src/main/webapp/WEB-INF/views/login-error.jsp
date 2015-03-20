<%@ include file="include.jsp" %>
<html>
<head>
<title>Hcentive CloudManage</title>
</head>
<body >
	<%@ include file="header.jsp" %>
	
	<table id="main-table" width="100%" border="0" height="70%"  background-repeat: no-repeat; background-position: bottom left;">
		<tbody>
			<tr>
				<td id="side-panel" width="20%">
					<div id="navigation" style="min-height: 323px; height: auto !important; height: 323px;"></div>
				</td>
				<td id="main-panel" width="80%" height="100%">
				<a name="skip2content"></a>
				<div style="margin: 2em; text-align:center; color:red; font-weight:bold">
					Invalid Login Information. Please login again.
				<br>
				<a href="${pageContext.request.contextPath}">Try Again</a>
				</div>
				</td>
			</tr>
		</tbody>
</table>
	<%@ include file="footer.jsp" %>
</body>
</html>