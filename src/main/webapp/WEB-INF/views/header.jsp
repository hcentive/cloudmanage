<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<style type="text/css">
#topBar{
position:fixed;
top:0;
left:0;
width:100%;
height:40px;
background-color:#9BBB59;
}
#topBar div {
  float: left;
  clear: none; 
}
#topBar div a{
  text-decoration: none; 
}
.desc { color:#6b6b6b;}
        .desc a {color:#0092dd;}
        
        .ccdropdown dd, .ccdropdown dt, .ccdropdown ul { margin:0px; padding:0px; }
        .ccdropdown dd { position:relative; }
        .ccdropdown a, .ccccdropdown a:visited { color:#816c5b; text-decoration:none; outline:none;}
        .ccdropdown a:hover { color:#5d4617;}
        .ccdropdown dt a:hover { color:#5d4617; border: 1px solid #d0c9af;}
        .ccdropdown dt a {background:#e4dfcb url(resources/images/arrow.png) no-repeat scroll right center; display:block; padding-right:20px;
                        border:1px solid #d4ca9a; width:150px;}
        .ccdropdown dt a span {cursor:pointer; display:block; padding:5px;}
        .ccdropdown dd ul { background:#e4dfcb none repeat scroll 0 0; border:1px solid #d4ca9a; color:#C5C0B0; display:none;
                          left:0px; padding:5px 0px; position:absolute; top:2px; width:auto; min-width:170px; list-style:none;}
        .ccdropdown span.value { display:none;}
        .ccdropdown dd ul li a { padding:5px; display:block;}
        .ccdropdown dd ul li a:hover { background-color:#d0c9af;}
        
        .ccdropdown img.flag { border:none; vertical-align:middle; margin-left:10px; }
        .flagvisibility { display:none;}
        .dropdown a{background:#9BBB59 url(${pageContext.request.contextPath}/resources/images/arrow.png) no-repeat scroll right center; display:block;
                         width:100px;} 
        
</style>
<%-- <table id="header" width="100%" cellspacing="0" cellpadding="0" border="0">
		<tbody>
				<tr>
					<td id="top-panel" colspan="2">
					<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
					<tr>
					<td  style="font-weight:bold; font-size: 2em;">
										<a id="portal-home-link" href="#">
											<img id="portal-home-icon" width="139" height="34" src="${pageContext.request.contextPath}/resources/images/title.png" alt="title">
										</a>	
					</td>
					<td >
						<div class="navigation" >
						<!--  <ul>
							<li><a href="#">Settings</a>
								<ul>					
									<li><a href="#" class="manageglobalrole">Manage Global Role</a></li>
									<li><a href="#" class="manageglobalrole">Manage Cost-center Role</a></li>
									<li><a href="#" class="manageglobalrole">Assign Global Role</a></li>
									<li><a href="#" class="manageglobalrole">Assign Cost-center Role</a></li>
								</ul>
						   </li>
						 </ul> -->
						
						</div>
					</td>
					<td id="login-field">	
							<c:choose>
							<c:when test="${username eq null}">
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
							</c:when>
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
		
		</tbody>
	</table> --%>
<%-- <sec:authentication property="principal.username" var="user" /> --%>
	<div id="topBar">
	  <div id="logo" style="font-weight:bold; font-size: 2em;margin-right: 50%;" >
	    <a id="portal-home-link" href="#">
		  <img id="portal-home-icon" width="139" height="34" src="${pageContext.request.contextPath}/resources/images/title.png" alt="title">
		</a>
	  </div>
	  <sec:authorize ifAnyGranted="admin">
	  <div class="navigation" id="setting" >
						 <ul>
							<li class="dropdown"><a href="#" ><b><span> Settings</span></b></a>&nbsp;<!-- <strong class="caret"></strong> -->
								<ul>					
									<li><a href="${pageContext.request.contextPath}/roles/showglobalrole" class="manageglobalrole">Manage Global Role</a></li>
									<li><a href="${pageContext.request.contextPath}/roles/showcostcenterrole" class="managecostcenterrole">Manage Cost-center Role</a></li>
									<li><a href="${pageContext.request.contextPath}/roles/assignglobalrole" class="assignglobalrole">Assign Global Role</a></li>
									<li><a href="${pageContext.request.contextPath}/roles/assigncostcenterrole" class="assigncostcenterrole">Assign Cost-center Role</a></li>
								</ul>
						   </li>
						 </ul>
						
	 </div>
	 </sec:authorize>
	<div class="navigation" id="costcenter"  style="margin-left: 20px;margin-right: 100px;">
	<sec:authorize ifAnyGranted="admin">
						 <ul>
							<li class="dropdown"><a href="#" data-toggle="ccdropdown"><b><span>Admin</span></b></a>&nbsp;<!-- <strong class="caret"></strong> -->
								<ul>					
									
								</ul>
						   </li>
						 </ul>
	</sec:authorize>					
	 </div>
	<div id="login" style="margin-top: 11px">
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
					<tr>
					<td id="login-field">	
							<c:choose>
							<%-- <c:when test="${user eq null}">
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
					</tbody></table>
	</div>
	</div>

	<script type="text/javascript">
	$(document).ready(function() {
       // $(".ccccdropdown img.flag").addClass("flagvisibility");

        $(".ccdropdown dt a").live('click',function(e) {
            $(".ccdropdown dd ul").toggle();
        });
                    
        $(".ccdropdown dd ul li a").click(function() {
         /*    var text = $(this).html();
            $(".ccdropdown dt a span").html(text);
            $(".ccdropdown dd ul").hide();
            $("#result").html("Selected value is: " + getSelectedValue("sample")); */
        });
                    
        function getSelectedValue(id) {
            return $("#" + id).find("dt a span.value").html();
        }

        $(document).bind('click', function(e) {
            var $clicked = $(e.target);
            if (! $clicked.parents().hasClass("ccdropdown"))
                $(".ccdropdown dd ul").hide();
        });
        var url='${pageContext.request.requestURI}';
        if(url.toLowerCase().indexOf("login")>0)
        	{
        		$('#logo').attr('style','font-weight:bold; font-size: 2em;margin-right: 70%;');
        	}
        if(url.toLowerCase().indexOf("_role")>0)
    	{
        	$('.dropdown a').css({"width":"60px"});
    	}
        

        /* $("#flagSwitcher").click(function() {
            $(".ccdropdown img.flag").toggleClass("flagvisibility");
        }); */
    });
	</script>