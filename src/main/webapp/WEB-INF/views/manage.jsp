 <%@ include file="include.jsp" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manage Aws Cloud Page</title>
<style type="text/css">
div.manage
{
background-color: #C6D3AB;
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
}

div.dataTables_filter
{
background-color: #D3D6FF;
height:30px;
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

</style>
<%@ include file="header.jsp" %>
</head>
<body >
 <div id="manage" class="manage" style="margin-top: 0px; margin-bottom: 0px;">
      <br> <br> <br> <br> <br> <br> <br> 
      
      <%--  ${instanceList} --%>
       <c:set var="serverlist" value="${instanceList}"/>
       <table id="dashboardtable" class="display" cellspacing="0" width="100%">
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
       </table>
       <br> <br> <br> <br> 
       <label class="status-label"> </label>
 </div>
</body>
<%@ include file="footer.jsp" %>

</html>
<script type="text/javascript">
var managetable=null;
var serverDetails=null;
var appUrl=window.location.protocol + "//" + window.location.host+'<%=request.getContextPath()%>';
$(document).ready(function() {
	refeshAll();
	
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
	$.ajax({
		url: appUrl+"/manage/refreshDashboard",
		type: "GET",
		cache : false,
		success: function(data){
			serverDetails=data;
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
										return date.toLocaleString();
									}
									},
								{ mData: 'owner'},
								{ mData: null,
									 "fnRender": function(oObj ) {
										 return '<div class="actions" align="center">'+ 
										 '<img id="'+oObj.iDataRow +'" class="loading-status" src="/cloudmanage/resources/images/loading.gif" />'+
										 '<span class="label label-info label-sm"> <lable class="refresh" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-refresh"></i> Refresh </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="viewdetails" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'"> <i class="icon icon-info-sign"></i> View details </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="start" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Start </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="stop" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Stop </lable></span>'+
										 '<span class="label label-info label-sm"> <lable class="terminate" id="'+oObj.iDataRow +'" name="'+oObj.aData.instanceId+'">  Terminate </lable></span></div>';
										    }
								}

		                      ]
		    } );
			var addDiv=$('.displayAll');
			$('<input type="button" class="refeshAll" value="RefreshAll" />').appendTo(addDiv);
			$('.dataTables_filter').attr('style', 'margin-top: 0px; margin-bottom: 0px;');
			$('.refeshAll').attr('style', 'margin-left: 35px; margin-right: 980px;');
			$('.displayAll').attr('style', 'background-color: #D3D6FF;');
			$('.actions span').attr('style','margin-left: 15px; margin-right: 0px;');
			$('.actions span lable').attr('style','cursor: pointer;');
			$('.actions').attr('style','margin-left: 75px; margin-right: 5px;');
			$('table.display tbody td:nth-child(2)').addClass('status-label');
			$('.actions img').attr('style','margin-left: 5px; margin-right: 15px; visibility: hidden;');
			//$('.displayAll').attr('style', 'background-color: #D3D6FF;');
		//	$('.loading-status').hide();
			$("#manage").unblock();
		}
	});
}
</script>