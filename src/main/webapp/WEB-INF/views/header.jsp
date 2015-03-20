<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style type="text/css">
.big { font-size: 110%; font-weight: bold; }
</style>
<table id="header" width="100%" cellspacing="0" cellpadding="0" border="0">
		<tbody>
				<tr>
					<td id="top-panel" colspan="2">
					<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
					<tr>
					<td colspan="2" style="font-weight:bold; font-size: 2em;">
										<a id="portal-home-link" href="#">
											<img id="portal-home-icon" width="139" height="34" src="${pageContext.request.contextPath}/resources/images/title.png" alt="title">
										</a>	
					</td>
					<td id="login-field">	
							<c:choose>
							<%-- <c:when test="${username eq null}">
									<span>
										&nbsp;
										<span style="white-space:nowrap">
											<a class="model-link inside" href="${pageContext.request.contextPath}/howtouse">
											<b>How to Use</b>
											</a>
												        |
											<a href="${pageContext.request.contextPath}/" style="color:inherit" class="model-link inside">
												<b>log in</b>
											</a>
										</span>
										
									</span>
							</c:when> --%>
							<c:when test="${username eq null}">
									<span>
										&nbsp;
										<span style="white-space:nowrap">
											<a href="${pageContext.request.contextPath}/" style="color:inherit" class="model-link inside">
												<b>log in</b>
											</a>
										</span>
										
									</span>
							</c:when>
							<c:when test="${username ne null}">
							<span>
								&nbsp;
								<span style="white-space:nowrap">
									<a class="model-link inside" href="#">
									<b>${username}</b>
									</a>
										        |
									<a href="${pageContext.request.contextPath}/logout">
										<b>log out</b>
									</a>
								</span>
							</span>
							</c:when>
							</c:choose>
							
						
					</td>
				</tr>
				</tbody>
				</table>
				</td>
				</tr>
		<tr id="top-nav">
</tr>
		</tbody>
	</table>
	<script type="text/javascript">
	
	</script>