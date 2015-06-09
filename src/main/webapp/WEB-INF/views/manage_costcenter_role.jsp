<%@ include file="include.jsp" %>  
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">

.manageglobal table {
	border: 1px solid;
	border-collapse: collapse;
}
.manageglobal td {
	border: 1px solid;
}
.manageglobal th {
	background: #ffbb00 solid;
}
.manageglobal
{
background-color: #C6D3AB;
}

</style>
<title>Manage Costcenter Role</title>
 <%@ include file="header.jsp" %>  
</head>
<body>
<sec:authentication property="principal.username" var="username" /> 
	<div class="manageglobal">
	   
	  <div style="margin-left: 200px;">
	  <br><br><br><br><br><br>
	  <h1><b>Manage Costcenter Role</b></h1>
	  <br><br><br>
	  	<form:form method="post" action="submitcostcenterrole" modelAttribute="roleForm">
			<table id="manageCostcenterRoles" bgcolor="#fff">
				<tr>
					<th> </th>
					<th>Role</th>
					 <th>start</th>
					<th>stop</th>
					<th>terminate</th>
					<th>qa</th>
					<th>dev</th> 
					<!-- <th colspan="2">cost-center</th> -->
				</tr>
				<c:forEach items="${roleForm.roles}" var="roles" varStatus="status">
					<tr>
						<td align="center" class="start"><a href="#" id="${status.index}" class="remove"><img alt="remove" src="${pageContext.request.contextPath}/resources/images/remove.gif">
							</a>
						</td>
						<td><label for="roles[${status.index}].role" >${roles.role}</label>
						    <input type="hidden" name="roles[${status.index}].role" value="${roles.role}">
						</td>
						<%-- <c:forEach items="${roles.permissions}" var="permission">
							<c:if test="${permission == 'qa'}">
								<td>
									<form:checkbox path="permission" value="qa" />
								</td>
							</c:if>
						</c:forEach> --%>
						<td><form:checkbox path="roles[${status.index}].permissions" value="start"/></td>
						<td><form:checkbox path="roles[${status.index}].permissions" value="stop"/></td>
						<td><form:checkbox path="roles[${status.index}].permissions" value="terminate"/></td>
						<td><form:checkbox path="roles[${status.index}].permissions" value="qa"/></td>
						<td><form:checkbox path="roles[${status.index}].permissions" value="dev"/></td>						
						
						
						<%--   <form:select multiple="true" path="roles.costCenterRoles">
    							<form:options items="${role}" itemValue="role" itemLabel="role"/>
						  </form:select> --%>
						
					</tr>
					<%-- <c:out value="${roles.role }"></c:out> --%>
				</c:forEach>
			</table>	
			<br/>
			<br/>
			   <label>Add Costcenter Role</label> <input type="text" id="addrole" value=""/> 
			    <br/><br>
				<input type="button" value="Add" id="add" onclick="addRow('manageCostcenterRoles')" style="margin-left: 200px"/>
			<br/> <br/>
			<input type="submit" value="Save" style="margin-bottom: 60px" />
	
		</form:form>
	  </div> 
	  
	</div>
	<script type="text/javascript">

	/* $("#add").click(function(e)
	{
		var table=document.getElementById("manageGlobalRole");
		var rowCount=table.rows.length;
		var row=table.insertRow(rowCount);
		var colCount=table.rows[0].cells.length;
	});*/
	
	$(".remove").live('click',function(e)
	{
		try{
			var table=document.getElementById('manageCostcenterRoles');
			var rownumber=parseInt(this.id);
			var row=table.rows[rownumber+1];
			if(rownumber+1 == 1 && row.cells[1].childNodes[2].value =='all')
				{
				alert("Cannot remove all role");
				return;
				}
			
			table.deleteRow(rownumber+1);
		}
		catch(e)
		{
			alert(e);
		};
	});
	function addRow(tableID)
	{
		var table=document.getElementById(tableID);
		var rowCount=table.rows.length;
		var row=table.insertRow(rowCount);
		var colCount=table.rows[1].cells.length;
		for(var i=0;i<colCount;i++)
		{
		var newcell=row.insertCell(i);
		newcell.innerHTML=table.rows[1].cells[i].innerHTML;
		switch(newcell.childNodes[0].tagName)
		{
		case"INPUT":
			
			var num=table.rows.length-2;
			if(newcell.childNodes[0].type == "text")
				{
				newcell.childNodes[0].value="";
					if(newcell.childNodes[0].name.indexOf('lastname')>0)
						{
						
						newcell.childNodes[0].name="contacts["+num.toString()+"].lastname";
						}
					else if(newcell.childNodes[0].name.indexOf('email')>0)
						{
						newcell.childNodes[0].name="contacts["+num.toString()+"].email";
						}
					else if(newcell.childNodes[0].name.indexOf('phone')>0)
					{
					newcell.childNodes[0].name="contacts["+num.toString()+"].phone";
					}
				}
			else if(newcell.childNodes[0].type == "checkbox")
				{
				newcell.childNodes[0].checked=false;
				newcell.childNodes[0].name="roles["+num.toString()+"].permissions";
				}
		break;
		case"LABEL":
			var num=table.rows.length-2;
			newcell.childNodes[0].innerHTML=document.getElementById("addrole").value;
			newcell.childNodes[2].value=document.getElementById("addrole").value;
			newcell.childNodes[2].name="roles["+num.toString()+"].role";
		break;
		case"A":
			var num=table.rows.length-2;
			newcell.childNodes[0].id=""+num;
			//newcell.childNodes[0].onclick="deleteRow('manageGlobalRoles',"+num+")";
		break;
		}
		}
		$("#addrole").val('');
	}
</script>
</body>
<%@ include file="footer.jsp" %>
</html>