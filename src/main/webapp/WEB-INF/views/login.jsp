<%@ include file="include.jsp" %>
<html>
<head>
<title>CloudManage</title>
</head>
<body >
	<%@ include file="header.jsp" %>
	
		<table id="main-table" width="100%" border="0" height="70%" style="background-repeat: no-repeat; background-position: bottom left;">
				<tbody>
					<tr>
						<td id="side-panel" width="20%">
							<div id="navigation" style="min-height: 323px; height: auto !important; height: 323px;"></div>
						</td>
						<td id="main-panel" width="80%" height="100%">
							<a name="skip2content"></a>
						<div style="margin: 2em;">
					<form:form id="signin" action="j_spring_security_check" method="post" autocomplete="on">
					<table>
						<tbody>
							<tr>
								<td>User:</td>
								<td>
									<input id="j_username" type="text" name="j_username">
								</td>
							</tr>
							<tr>
								<td>Password:</td>
								<td>
									<input id="j_password" type="password" name="j_password">
								</td>
							</tr>
							<tr>
								<td align="right">
									<input id="remember_me" type="checkbox" name="remember_me">
								</td>
								<td>
									<label for="remember_me">Remember me on this computer</label>
								</td>
							</tr>
						</tbody>
					</table>
					<input type="hidden" value="/portal" name="from">
					<span id="yui-gen1"  name="Submit">
					<span>
					<button id="yui-gen1-button" type="submit" tabindex="0">Log in</button>
					</span>
					</span>
					
					<div>
					<input type="hidden" value="init" name="json">
					</div>
					</form:form>
					</td>
				</tr>
				</tbody>
		</table>
	<%@ include file="footer.jsp" %>
</body>
</html>
<script type="text/javascript">
					$("#j_username").focus();
					$('.footer #totalhit').hide();
</script>