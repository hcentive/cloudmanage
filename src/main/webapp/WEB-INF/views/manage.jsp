 <%@ include file="include.jsp" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/osx.css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manage Aws Cloud Page</title>
<style type="text/css">
div.manage 
{
background-color: #C6D3AB;
z-index: -1;
position: absolute;
top:0px;
bottom:0px;
} 
div.noroels
{
z-index: -1;
position: relative;
top:0px;
bottom:0px;

}
.refeshAll {
	-moz-box-shadow:inset 0px 0px 2px 0px #cae3fc;
	-webkit-box-shadow:inset 0px 0px 2px 0px #cae3fc;
	box-shadow:inset 0px 0px 2px 0px #cae3fc;
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #79bbff), color-stop(1, #4197ee) );
	background:-moz-linear-gradient( center top, #79bbff 5%, #4197ee 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#79bbff', endColorstr='#4197ee');
	background-color:#79bbff;
	-webkit-border-top-left-radius:0px;
	-moz-border-radius-topleft:0px;
	border-top-left-radius:0px;
	-webkit-border-top-right-radius:0px;
	-moz-border-radius-topright:0px;
	border-top-right-radius:0px;
	-webkit-border-bottom-right-radius:0px;
	-moz-border-radius-bottomright:0px;
	border-bottom-right-radius:0px;
	-webkit-border-bottom-left-radius:0px;
	-moz-border-radius-bottomleft:0px;
	border-bottom-left-radius:0px;
	text-indent:2px;
	border:1px solid #469df5;
	display:inline-block;
	color:#ffffff;
	font-family:Arial;
	font-size:14px;
	font-weight:bold;
	font-style:normal;
	height:30px;
	line-height:30px;
	width:84px;
	text-decoration:none;
	text-align:left;
	text-shadow:2px 2px 0px #287ace;
}
.refeshAll:hover {
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #4197ee), color-stop(1, #79bbff) );
	background:-moz-linear-gradient( center top, #4197ee 5%, #79bbff 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#4197ee', endColorstr='#79bbff');
	background-color:#4197ee;
}.refeshAll:active {
	position:relative;
	top:1px;
}
.displayAll{
    display:inline;
    *display:inline; /* for IE7*/
    *zoom:1;/* for IE7*/
    width:80%;
    float:left;
    }
.refesh {
	-moz-box-shadow:inset 0px 0px 2px 0px #cae3fc;
	-webkit-box-shadow:inset 0px 0px 2px 0px #cae3fc;
	box-shadow:inset 0px 0px 2px 0px #cae3fc;
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #79bbff), color-stop(1, #4197ee) );
	background:-moz-linear-gradient( center top, #79bbff 5%, #4197ee 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#79bbff', endColorstr='#4197ee');
	background-color:#79bbff;
	-webkit-border-top-left-radius:0px;
	-moz-border-radius-topleft:0px;
	border-top-left-radius:0px;
	-webkit-border-top-right-radius:0px;
	-moz-border-radius-topright:0px;
	border-top-right-radius:0px;
	-webkit-border-bottom-right-radius:0px;
	-moz-border-radius-bottomright:0px;
	border-bottom-right-radius:0px;
	-webkit-border-bottom-left-radius:0px;
	-moz-border-radius-bottomleft:0px;
	border-bottom-left-radius:0px;
	text-indent:0px;
	border:1px solid #469df5;
	display:inline-block;
	color:#ffffff;
	font-family:Arial;
	font-size:11px;
	font-weight:bold;
	font-style:normal;
	height:24px;
	line-height:20px;
	width:50px;
	text-decoration:none;
	text-align:center;
	text-shadow:2px 2px 0px #287ace;
}
.refesh:hover {
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #4197ee), color-stop(1, #79bbff) );
	background:-moz-linear-gradient( center top, #4197ee 5%, #79bbff 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#4197ee', endColorstr='#79bbff');
	background-color:#4197ee;
}.refesh:active {
	position:relative;
	top:1px;
}
.startstop {
	-moz-box-shadow:inset 0px 0px 2px 0px #cae3fc;
	-webkit-box-shadow:inset 0px 0px 2px 0px #cae3fc;
	box-shadow:inset 0px 0px 2px 0px #cae3fc;
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #79bbff), color-stop(1, #4197ee) );
	background:-moz-linear-gradient( center top, #79bbff 5%, #4197ee 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#79bbff', endColorstr='#4197ee');
	background-color:#79bbff;
	-webkit-border-top-left-radius:0px;
	-moz-border-radius-topleft:0px;
	border-top-left-radius:0px;
	-webkit-border-top-right-radius:0px;
	-moz-border-radius-topright:0px;
	border-top-right-radius:0px;
	-webkit-border-bottom-right-radius:0px;
	-moz-border-radius-bottomright:0px;
	border-bottom-right-radius:0px;
	-webkit-border-bottom-left-radius:0px;
	-moz-border-radius-bottomleft:0px;
	border-bottom-left-radius:0px;
	text-indent:0px;
	border:1px solid #469df5;
	display:inline-block;
	color:#ffffff;
	font-family:Arial;
	font-size:11px;
	font-weight:bold;
	font-style:normal;
	height:24px;
	line-height:20px;
	width:36px;
	text-decoration:none;
	text-align:left;
	text-shadow:2px 2px 0px #287ace;
}
.startstop:hover {
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #4197ee), color-stop(1, #79bbff) );
	background:-moz-linear-gradient( center top, #4197ee 5%, #79bbff 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#4197ee', endColorstr='#79bbff');
	background-color:#4197ee;
}.startstop:active {
	position:relative;
	top:1px;
}
.terminat {
	-moz-box-shadow:inset 0px 0px 2px 0px #cae3fc;
	-webkit-box-shadow:inset 0px 0px 2px 0px #cae3fc;
	box-shadow:inset 0px 0px 2px 0px #cae3fc;
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #79bbff), color-stop(1, #4197ee) );
	background:-moz-linear-gradient( center top, #79bbff 5%, #4197ee 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#79bbff', endColorstr='#4197ee');
	background-color:#79bbff;
	-webkit-border-top-left-radius:0px;
	-moz-border-radius-topleft:0px;
	border-top-left-radius:0px;
	-webkit-border-top-right-radius:0px;
	-moz-border-radius-topright:0px;
	border-top-right-radius:0px;
	-webkit-border-bottom-right-radius:0px;
	-moz-border-radius-bottomright:0px;
	border-bottom-right-radius:0px;
	-webkit-border-bottom-left-radius:0px;
	-moz-border-radius-bottomleft:0px;
	border-bottom-left-radius:0px;
	text-indent:0px;
	border:1px solid #469df5;
	display:inline-block;
	color:#ffffff;
	font-family:Arial;
	font-size:11px;
	font-weight:bold;
	font-style:normal;
	height:24px;
	line-height:20px;
	width:71px;
	text-decoration:none;
	text-align:center;
	text-shadow:2px 2px 0px #287ace;
}
.terminat:hover {
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #4197ee), color-stop(1, #79bbff) );
	background:-moz-linear-gradient( center top, #4197ee 5%, #79bbff 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#4197ee', endColorstr='#79bbff');
	background-color:#4197ee;
}.terminat:active {
	position:relative;
	top:1px;
}
.dataTables_wrapper div
{
  float: left;
  clear: none;
  white-space:nowrap;
  
}

div.dataTables_filter
{
background-color: #D3D6FF;
height:30px;
display:inline;
float:left;
width:20%
}
div.paging_two_button
{
background-color: #D3D6FF;
display:inline;
float:left;
width:20%
}
div.dataTables_info
{
 display:inline;
    *display:inline; /* for IE7*/
    *zoom:1;/* for IE7*/
    width:80%;
    float:left;
background-color: #D3D6FF;
width:80%
}
table.display thead th
{
background-color: #EAEBFF;
height:30px;
align:center;
}
table.display thead th:hover
{
background-color:#C6D3AB;
}
table.display tbody tr:hover
{
background-color:#C6D3AB;
}
table.display tbody tr {
  height: 40px; 
}
table.display tbody tr td{
  text-align:center;
  input-align:center;
  
}
.status-label{
  font-size: 11px;
  text-transform: uppercase;
  color: #526977;
}
.lable {
font-size: .75em;
display: inline-block;
}
label {
    cursor: pointer;
}
.status-icon.running {
  background: #0eb977;
}
.refresh-one {
  float: right;
  margin-top: 0px;
  padding-top: 0px;
  line-height: 34px;
  color: white;
}
.status-icon {
  display: inline-block;
  width: 34px;
  height: 34px;
  float: left;
  margin-right: 10px;
  text-align: center;
  -webkit-border-radius: 4px;
  -moz-border-radius: 4px;
  -ms-border-radius: 4px;
  -o-border-radius: 4px;
  border-radius: 4px;
}
.refresh-spinner {
    background-image: url('data:image/gif;base64,R0lGODlhEAAQAPQAAP////////7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAAFUCAgjmRpnqUwFGwhKoRgqq2YFMaRGjWA8AbZiIBbjQQ8AmmFUJEQhQGJhaKOrCksgEla+KIkYvC6SJKQOISoNSYdeIk1ayA8ExTyeR3F749CACH5BAkKAAAALAAAAAAQABAAAAVoICCKR9KMaCoaxeCoqEAkRX3AwMHWxQIIjJSAZWgUEgzBwCBAEQpMwIDwY1FHgwJCtOW2UDWYIDyqNVVkUbYr6CK+o2eUMKgWrqKhj0FrEM8jQQALPFA3MAc8CQSAMA5ZBjgqDQmHIyEAIfkECQoAAAAsAAAAABAAEAAABWAgII4j85Ao2hRIKgrEUBQJLaSHMe8zgQo6Q8sxS7RIhILhBkgumCTZsXkACBC+0cwF2GoLLoFXREDcDlkAojBICRaFLDCOQtQKjmsQSubtDFU/NXcDBHwkaw1cKQ8MiyEAIfkECQoAAAAsAAAAABAAEAAABVIgII5kaZ6AIJQCMRTFQKiDQx4GrBfGa4uCnAEhQuRgPwCBtwK+kCNFgjh6QlFYgGO7baJ2CxIioSDpwqNggWCGDVVGphly3BkOpXDrKfNm/4AhACH5BAkKAAAALAAAAAAQABAAAAVgICCOZGmeqEAMRTEQwskYbV0Yx7kYSIzQhtgoBxCKBDQCIOcoLBimRiFhSABYU5gIgW01pLUBYkRItAYAqrlhYiwKjiWAcDMWY8QjsCf4DewiBzQ2N1AmKlgvgCiMjSQhACH5BAkKAAAALAAAAAAQABAAAAVfICCOZGmeqEgUxUAIpkA0AMKyxkEiSZEIsJqhYAg+boUFSTAkiBiNHks3sg1ILAfBiS10gyqCg0UaFBCkwy3RYKiIYMAC+RAxiQgYsJdAjw5DN2gILzEEZgVcKYuMJiEAOwAAAAAAAAAAAA==') !important;
    background-repeat: no-repeat !important;
    background-position: center center !important;
}

.refresh-spinner:before {
    content: '' !important;
}

.refresh-spinner:after {
    content: '' !important;
}

.refresh-spinner-container .refresh-spinner {
    background-image: url('data:image/gif;base64,R0lGODlhEAAQAPQAAP////////7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAAFUCAgjmRpnqUwFGwhKoRgqq2YFMaRGjWA8AbZiIBbjQQ8AmmFUJEQhQGJhaKOrCksgEla+KIkYvC6SJKQOISoNSYdeIk1ayA8ExTyeR3F749CACH5BAkKAAAALAAAAAAQABAAAAVoICCKR9KMaCoaxeCoqEAkRX3AwMHWxQIIjJSAZWgUEgzBwCBAEQpMwIDwY1FHgwJCtOW2UDWYIDyqNVVkUbYr6CK+o2eUMKgWrqKhj0FrEM8jQQALPFA3MAc8CQSAMA5ZBjgqDQmHIyEAIfkECQoAAAAsAAAAABAAEAAABWAgII4j85Ao2hRIKgrEUBQJLaSHMe8zgQo6Q8sxS7RIhILhBkgumCTZsXkACBC+0cwF2GoLLoFXREDcDlkAojBICRaFLDCOQtQKjmsQSubtDFU/NXcDBHwkaw1cKQ8MiyEAIfkECQoAAAAsAAAAABAAEAAABVIgII5kaZ6AIJQCMRTFQKiDQx4GrBfGa4uCnAEhQuRgPwCBtwK+kCNFgjh6QlFYgGO7baJ2CxIioSDpwqNggWCGDVVGphly3BkOpXDrKfNm/4AhACH5BAkKAAAALAAAAAAQABAAAAVgICCOZGmeqEAMRTEQwskYbV0Yx7kYSIzQhtgoBxCKBDQCIOcoLBimRiFhSABYU5gIgW01pLUBYkRItAYAqrlhYiwKjiWAcDMWY8QjsCf4DewiBzQ2N1AmKlgvgCiMjSQhACH5BAkKAAAALAAAAAAQABAAAAVfICCOZGmeqEgUxUAIpkA0AMKyxkEiSZEIsJqhYAg+boUFSTAkiBiNHks3sg1ILAfBiS10gyqCg0UaFBCkwy3RYKiIYMAC+RAxiQgYsJdAjw5DN2gILzEEZgVcKYuMJiEAOwAAAAAAAAAAAA==') !important;
    background-repeat: no-repeat !important;
    background-position: center center !important;
}
.detail-list {
  float: left;
  width: 50%;
  margin: 15px 0;
  padding: 0;
  list-style-type: none;
}
.detail-list .item-label {
  float: left;
  display: inline-block;
  color: #0e77b9;
  width: 40%;
  padding-right: 5%;
  word-break: break-all;
}

.item-label {
  font-size: 14px;
}

.detail-list .item-value {
  float: left;
  display: inline-block;
  width: 60%;
  word-break: break-all;
  color: #666;
}
.detail-list li {
  padding: 7.5px;
  padding-left: 15px;
}

.detail-list li span
{
  height:48px;
}

/* li {
  display: list-item;
  text-align: -webkit-match-parent;
} */

</style>
<%@ include file="header.jsp" %>
</head>
<body >
<%-- Authorities: <sec:authentication property="principal.authorities"/><br /> --%>
<%-- <sec:authentication property="principal.username" var="user" /> --%>
<sec:authentication property="authorities" var="rights" />
 <%-- <sec:authorize ifAnyGranted="admin">
   <input type="hidden" id="role" value="admin">
   <input type="hidden" id="user" value="${user}">
</sec:authorize>  --%>

<c:set var="haveRoles" value="${haveRoles}"/>
<c:set var="serverlist" value="${instanceList}"/>
<%@ include file="_manageTableTemplate.jsp" %>
 <div id="noroels" class="noroels"  style="background-color: #C6D3AB;text-align:center; color:red; font-weight:bold;margin-bottom: 0px; position: relative; zoom: 1;">
     <br> <br> <br> <br> <br> <br> <br> 
      Sorry, You are not authorized to access Aws Server's, contact to techop's team  
     <br> <br> <br> <br> <br> <br> <br><br> <br> <br> <br> <br> <br> <br>
 
 </div>
 <div id="manage" class="manage" style="margin-top:px; margin-bottom: 0px;width: 100%" >     
      <br> <br> <br> <br> <br> <br> <br> <br> <br> 
    
      <%--  ${instanceList} --%>
       
    <!--    <table id="dashboardtable" class="display" cellspacing="0" width="100%">
       <thead>
           <tr>
           	  <th> Name </th>
           	  <th> Status  </th>
           	  <th> System Create Time </th>
           	  <th> Owner </th>
           	  <th> Actions </th>
           </tr>
       </thead>
       <tbody>
       </tbody>
       </table> -->
       <br> <br> <br> <br> 
      <!--  <label class="status-label"> </label> -->
       <!-- modal content -->
		<!-- <div id="osx-modal-content">
			<div id="osx-modal-title">Instance Details</div>
			<div class="close"><a href="#" class="simplemodal-close">x</a></div>
			<div id="osx-modal-data">
				<div class="details">
                	<ul class="detail-list left">
                	</ul>
                	<ul class="detail-list right">
                	</ul>
            	</div>				
			</div>
		</div> -->
 </div>
 	<div id="osx-modal-content">
			<div id="osx-modal-title">Instance Details</div>
			<div class="close"><a href="#" class="simplemodal-close">x</a></div>
			<div id="osx-modal-data">
				<div class="details">
                	<ul class="detail-list left">
                	</ul>
                	<ul class="detail-list right">
                	</ul>
            	</div>				
			</div>
		</div>
 <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery.simplemodal.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/osx.js"></script>
</body>
<%@ include file="footer.jsp" %>

</html>
<script type="text/javascript">
var managetable=null;
var serverDetails=null;
var appUrl=window.location.protocol + "//" + window.location.host+'<%=request.getContextPath()%>';
var rolesArr = '${rights}'.substring(1,'${rights}'.length-1).split(',');
/* (function ($) {
    $.extend(jQuery.tmpl.tag, {
        "for": {
            _default: {$2: "var i=1;i<=1;i++"},
            open: 'for ($2){',
            close: '};'
        }
    });
})(jQuery); */
$(document).ready(function() {
	$( document ).ajaxError(function( event, request, settings ) {
		  console.log(request);
		  console.log(settings);
		});
	if('${haveRoles}' === 'false')
		{
		$('#manage').hide();
		$('#noroels').show();
		$('#logo').attr('style','font-weight:bold; font-size: 2em;margin-right: 60%;');
		return;
		}
	else
		{
		$('#noroels').hide();
		}
	refeshAll();
	
	$.fn.dataTableExt.sErrMode = 'none';
	$('.refeshAll').live('click', function () {
		if(managetable != null)
		  {
	  	if(managetable.fnSettings().fnRecordsDisplay() > 0)
	  		{
	  		managetable.fnDestroy();
	  		}
		  }
		refeshAll();
	});
	$('.refresh').live('click', function () {
		if(managetable != null)
		  {
	  	if(managetable.fnSettings().fnRecordsDisplay() > 0)
	  		{
	  		var serverDetail=null;
	  		var id=parseInt(this.id);
	  		$(".actions")[id].children[1].children[0].children[0].setAttribute("class","icon-spinner icon-spin");
	  		//managetable.fnUpdate( 'WIG_DR', 0,0);
	  		 $.ajax({
				url: appUrl+"/manage/refreshServerInstance",
				type: "POST",
				data: {"instanceId":serverDetails[this.id].instanceId},
				cache : false,
				success: function(data)
		    	{
					serverDetail=serverDetails[id];
					if(serverDetail.name !== data.name)
						{
					      managetable.fnUpdate(data.name, id, 0);
					      serverDetails[id].name=data.name;
						}
					if(serverDetail.state !== data.state)
						{
						  managetable.fnUpdate(data.state, id, 1);
					      serverDetails[id].state=data.state;
						}
					if(serverDetail.owner !== data.owner)
					{
					  managetable.fnUpdate(data.owner, id, 3);
				      serverDetails[id].owner=data.owner;
					}	    
				$(".actions")[id].children[1].children[0].children[0].setAttribute("class","icon icon-refresh");
	  			}
		  } );  
	  		}
		  }
} );
	$('.start').live('click', function () {
		if(managetable != null)
		  {
	  	if(managetable.fnSettings().fnRecordsDisplay() > 0)
	  		{
	  		var serverDetail=null;
	  		var id=parseInt(this.id);
	  		managetable.fnUpdate("pending", id, 1);
	  	//	$('.loading-status')[id].show();
	  		//$("img").filter("#"+id).show();
	  		$("img").filter("#"+id)[0].setAttribute("style","margin-left: 5px; margin-right: 15px;");
	  		//managetable.fnUpdate( 'WIG_DR', 0,0);
	  		 $.ajax({
				url: appUrl+"/manage/startServerInstance",
				type: "POST",
				data: {"instanceId":serverDetails[this.id].instanceId},
				cache : false,
				success: function(data)
		    	{
					serverDetail=serverDetails[id];
					
					if(serverDetail.state !== data)
						{
						  managetable.fnUpdate(data, id, 1);
					      serverDetails[id].state=data;
						}    
					//$("img").filter("#"+id).hide();
					$("img").filter("#"+id)[0].setAttribute("style","margin-left: 5px; margin-right: 15px; visibility: hidden;");
	  			}
		  } );  
	  		}
		  }
} );
	$('.stop').live('click', function () {
		if(managetable != null)
		  {
	  	if(managetable.fnSettings().fnRecordsDisplay() > 0)
	  		{
	  		var serverDetail=null;
	  		var id=parseInt(this.id);
	  		managetable.fnUpdate("stopping", id, 1);
	  		//$("img").filter("#"+id).show();
	  		$("img").filter("#"+id)[0].setAttribute("style","margin-left: 5px; margin-right: 15px;");
	  		//managetable.fnUpdate( 'WIG_DR', 0,0);
	  		 $.ajax({
				url: appUrl+"/manage/stopServerInstance",
				type: "POST",
				data: {"instanceId":serverDetails[this.id].instanceId},
				cache : false,
				success: function(data)
		    	{
					serverDetail=serverDetails[id];
					
					if(serverDetail.state !== data)
						{
						  managetable.fnUpdate(data, id, 1);
					      serverDetails[id].state=data;
						}   
					//$("img").filter("#"+id).hide();
					$("img").filter("#"+id)[0].setAttribute("style","margin-left: 5px; margin-right: 15px; visibility: hidden;");
	  			}
		  } );  
	  		}
		  }
} );
	$('.terminate').live('click', function () {
		if(managetable != null)
		  {
	  	if(managetable.fnSettings().fnRecordsDisplay() > 0)
	  		{
	  		//managetable.fnUpdate("shuting-down", id, 1);
	  		var serverDetail=null;
	  		var id=parseInt(this.id);
	  		managetable.fnUpdate("shuting-down", id, 1);
	  		//$("img").filter("#"+id).show();
	  		$("img").filter("#"+id)[0].setAttribute("style","margin-left: 5px; margin-right: 15px;");
	  		//managetable.fnUpdate( 'WIG_DR', 0,0);
	  		 $.ajax({
				url: appUrl+"/manage/terminateServerInstance",
				type: "POST",
				data: {"instanceId":serverDetails[this.id].instanceId},
				cache : false,
				success: function(data)
		    	{
					serverDetail=serverDetails[id];
					
					if(serverDetail.state !== data)
						{
						  managetable.fnUpdate(data, id, 1);
					      serverDetails[id].state=data;
						}  
					$("img").filter("#"+id)[0].setAttribute("style","margin-left: 5px; margin-right: 15px; visibility: hidden;");
	  			}
		  } );  
	  		}
		  }
} );
} );

function Manage_Data(maxSecurityGroups)
{
    this.maxSecurityGroups=maxSecurityGroups;
}
function refeshAll(){
	$("#manage").block({ css: { 
        border: 'none', 
        padding: '15px', 
        backgroundColor: '#D3D6FF', 
        '-webkit-border-radius': '10px', 
        '-moz-border-radius': '10px', 
        opacity: .2, 
        color: '#fff' 
    },
    message:'<h1><img src="/cloudmanage/resources/images/712.GIF" /> </h1>'
	});
	$('#dashboardtable').bind('page', function () {
		$('.actions span').attr('style','margin-left: 15px; margin-right: 0px;');
		$('.actions span lable').attr('style','cursor: pointer;');
		$('.actions').attr('style','margin-left: 75px; margin-right: 5px;');
		$('table.display tbody td:nth-child(2)').addClass('status-label');
		$('.actions img').attr('style','margin-left: 5px; margin-right: 15px; visibility: hidden;');
	});
	$.ajax({
		url: appUrl+"/manage/refreshDashboard",
		type: "GET",
		cache : false,
		success: function(data){
			serverDetails=data;
			var maxSecurityGroups=0;
			$.each(data, function(index,element){
				$.each(element, function(k,v){
				        if(k === "maxSecurityGroups")
					 {
					   if(v>maxSecurityGroups)
					     {
						   maxSecurityGroups=v;
					     }
					 }
				});
			});
			maxSecurityGroups=1;
			$("#manage").empty();
			$("#manage_theadtemplate").tmpl(new Manage_Data(maxSecurityGroups)).appendTo("#manage");
			//$("#phixServerTable").hide();
			console.log("maximum security groups are"+maxSecurityGroups);
			if(maxSecurityGroups == 1)
			{
			    managetable=$('#dashboardtable').dataTable( {
				"sDom": '<"displayAll">rft<"bottom"><"clear">ip',
				"aaData":data,
				"bAutoWidth": false,
				"bSort": false,
				"fnDrawCallback": function( oSettings ) {
					$('.actions span').attr('style','margin-left: 15px; margin-right: 0px;');
					$('.actions span lable').attr('style','cursor: pointer;');
					$('.actions').attr('style','margin-left: 75px; margin-right: 5px;');
					$('table.display tbody td:nth-child(2)').addClass('status-label');
					$('.actions img').attr('style','margin-left: 5px; margin-right: 15px; visibility: hidden;');
				    },
				//"sPaginationType": "full_numbers",
				"iDisplayLength":10,
				//"bPaginate": false,
		        "aoColumns": [
								{ mData: 'name'},
								/* { mData: null,
									"fnRender": function(oObj)
									{
										//return '<label class="status-label">'+oObj.aData.state+' </label>';
										return oObj.aData.state;
									}
								}, */
								{ mData: 'state'},
								{ mData: null,
									"fnRender": function(oObj)
									{
										var date = new Date(oObj.aData.createtime);
										return date.toUTCString();
									}
									},
								{ mData: 'owner'},
								{ mData: 'instanceId'},
								{ mData: 'architecture'},
								{ mData: 'imageId'},
								{ mData: 'instanceType'},
								{ mData: 'keyName'},
								{ mData: 'privateIpAddress'},
								{ mData: 'subnetId'},
								{ mData: 'vpcId'},
								{ mData: 'costCenter'},
								{ mData: 'securityGroups.0.groupId'},
								{ mData: 'securityGroups.0.groupName'},
								{ mData: null,
									 "fnRender": function(oObj ) {
										 var ret=null;
										 
										 if($.inArray('start',rolesArr) !== -1)
											 {
											 if('${user}' === "manoj.tailor" || '${user}' === "praveen.tiwari" || '${user}' === "pardeep.chahal")
												 {
										  ret= '<div class="actions" align="center">'+ 
										 '<img id="'+oObj.iDataRow +'" class="loading-status" src="/cloudmanage/resources/images/loading.gif" />'+
										 '<span class="label label-info label-sm"> <lable class="refresh" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-refresh"></i> Refresh </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="viewdetails osx" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-info-sign"></i> View details </lable></span>'+ 
										 '<span class="label label-info label-sm"> <lable class="start" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Start </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="stop" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Stop </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="terminate" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Terminate </lable></span></div>';
												 }
											 else
												 {
												 ret= '<div class="actions" align="center">'+ 
												 '<img id="'+oObj.iDataRow +'" class="loading-status" src="/cloudmanage/resources/images/loading.gif" />'+
												 '<span class="label label-info label-sm"> <lable class="refresh" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-refresh"></i> Refresh </lable></span>'+
												 '<span class="label label-info label-sm"> <lable class="viewdetails osx" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-info-sign"></i> View details </lable></span>'+ 
												 '<span class="label label-info label-sm"> <lable class="start" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Start </lable></span>'+
												 '<span class="label label-info label-sm"> <lable class="stop" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Stop </lable></span></div>';
												 }
												}
										 
										 else
											 {
											 console.log("array nahi h");
											 console.log(jQuery.type('${rights}'));
											 console.log(rolesArr);
											 ret= '<div class="actions" align="center">'+ 
											 '<img id="'+oObj.iDataRow +'" class="loading-status" src="/cloudmanage/resources/images/loading.gif" />'+
											 '<span class="label label-info label-sm"> <lable class="refresh" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-refresh"></i> Refresh </lable></span>'+
											 '<span class="label label-info label-sm"> <lable class="viewdetails osx" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-info-sign"></i> View details </lable></span></div>'; 
											 }
										 return ret;
									 }
								}

		                      ]
		    } );
			  /*   $('#dashboardtable').bind('page', function () {
					
				}); */
			}
			else if(maxSecurityGroups == 2)
				{
				managetable=$('#dashboardtable').on( 'error.dt', function ( e, settings, techNote, message ) {
			        console.log( 'An error has been reported by DataTables: ', message );
			    } ).dataTable( {
					"sDom": '<"displayAll">rft<"bottom"><"clear">',
					"aaData":data,
					"bAutoWidth": false,
					"bSort": false,
					"bPaginate": false,
			        "aoColumns": [
									{ mData: 'name'},
									/* { mData: null,
										"fnRender": function(oObj)
										{
											//return '<label class="status-label">'+oObj.aData.state+' </label>';
											return oObj.aData.state;
										}
									}, */
									{ mData: 'state'},
									{ mData: null,
										"fnRender": function(oObj)
										{
											var date = new Date(oObj.aData.createtime);
											return date.toUTCString();
										}
										},
									{ mData: 'owner'},
									{ mData: 'instanceId'},
									{ mData: 'architecture'},
									{ mData: 'imageId'},
									{ mData: 'instanceType'},
									{ mData: 'keyName'},
									{ mData: 'privateIpAddress'},
									{ mData: 'subnetId'},
									{ mData: 'vpcId'},
									{ mData: 'costCenter'},
									{ mData: 'securityGroups.0.groupId'},
									{ mData: 'securityGroups.0.groupName'},
									{ mData: 'securityGroups.1.groupId'},
									{ mData: 'securityGroups.1.groupName'},
									{ mData: null,
										 "fnRender": function(oObj ) {
											 return '<div class="actions" align="center">'+ 
											 '<img id="'+oObj.iDataRow +'" class="loading-status" src="/cloudmanage/resources/images/loading.gif" />'+
											 '<span class="label label-info label-sm"> <lable class="refresh" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-refresh"></i> Refresh </lable></span>'+
											 '<span class="label label-info label-sm"> <lable class="viewdetails osx" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-info-sign"></i> View details </lable></span>'+ 
											 '<span class="label label-info label-sm"> <lable class="start" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Start </lable></span>'+
											 '<span class="label label-info label-sm"> <lable class="stop" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Stop </lable></span>'+
											 '<span class="label label-info label-sm"> <lable class="terminate" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Terminate </lable></span></div>';
											    }
									}

			                      ]
			    } );
				}
			else if(maxSecurityGroups == 3)
			{
			managetable=$('#dashboardtable').dataTable( {
				"sDom": '<"displayAll">rft<"bottom"><"clear">',
				"aaData":data,
				"bAutoWidth": false,
				"bSort": false,
				"bPaginate": false,
		        "aoColumns": [
								{ mData: 'name'},
								/* { mData: null,
									"fnRender": function(oObj)
									{
										//return '<label class="status-label">'+oObj.aData.state+' </label>';
										return oObj.aData.state;
									}
								}, */
								{ mData: 'state'},
								{ mData: null,
									"fnRender": function(oObj)
									{
										var date = new Date(oObj.aData.createtime);
										return date.toUTCString();
									}
									},
								{ mData: 'owner'},
								{ mData: 'instanceId'},
								{ mData: 'architecture'},
								{ mData: 'imageId'},
								{ mData: 'instanceType'},
								{ mData: 'keyName'},
								{ mData: 'privateIpAddress'},
								{ mData: 'subnetId'},
								{ mData: 'vpcId'},
								{ mData: 'costCenter'},
								{ mData: 'securityGroups.0.groupId'},
								{ mData: 'securityGroups.0.groupName'},
								{ mData: 'securityGroups.1.groupId'},
								{ mData: 'securityGroups.1.groupName'},
								{ mData: 'securityGroups.2.groupId'},
								{ mData: 'securityGroups.2.groupName'},
								{ mData: null,
									 "fnRender": function(oObj ) {
										 return '<div class="actions" align="center">'+ 
										 '<img id="'+oObj.iDataRow +'" class="loading-status" src="/cloudmanage/resources/images/loading.gif" />'+
										 '<span class="label label-info label-sm"> <lable class="refresh" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-refresh"></i> Refresh </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="viewdetails osx" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-info-sign"></i> View details </lable></span>'+ 
										 '<span class="label label-info label-sm"> <lable class="start" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Start </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="stop" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Stop </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="terminate" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Terminate </lable></span></div>';
										    }
								}

		                      ]
		    } );
			}
			    var hideColumnArr;
			    var lastColumn =12;
			    hideColumnArr=[4,5,6,7,8,9,10,11,12];
			    var k;
			    for (k = 9; k <= 8+maxSecurityGroups*2 ; k++) { 
			    	hideColumnArr[k]=lastColumn+1;
			    	lastColumn++;
			    }
			    
			      jQuery.each(hideColumnArr, function(index, iCol) {											
					var bVis = managetable.fnSettings().aoColumns[iCol].bVisible;
					managetable.fnSetColumnVis( iCol, bVis ? false : true );
				});    
			var addDiv=$('.displayAll');
			$('<input type="button" class="refeshAll" value="IngestAll" />').appendTo(addDiv);
			$('.dataTables_filter').attr('style', 'margin-top: 0px; margin-bottom: 0px;');
			$('.refeshAll').attr('style', 'margin-left: 35px; ');
			
			$('.displayAll').attr('style', 'background-color: #D3D6FF;');
			$('.actions span').attr('style','margin-left: 15px; margin-right: 0px;');
			$('.actions span lable').attr('style','cursor: pointer;');
			$('.actions').attr('style','margin-left: 75px; margin-right: 5px;');
			$('table.display tbody td:nth-child(2)').addClass('status-label');
			$('.actions img').attr('style','margin-left: 5px; margin-right: 15px; visibility: hidden;');
			//$('.displayAll').attr('style', 'background-color: #D3D6FF;');
		//	$('.loading-status').hide();
			$("#manage").unblock();
		},
		error: function (xhr, error, thrown) {
			console.log(error);
		}
	});
}
</script>