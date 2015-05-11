/*
 * SimpleModal OSX Style Modal Dialog
 * http://simplemodal.com
 *
 * Copyright (c) 2013 Eric Martin - http://ericmmartin.com
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *   
 *   
 *   
 */

jQuery(function ($) {
	var id=null;
	var OSX = {
		container: null,
		init: function () {
			$(".osx").live('click', function (e) {
				e.preventDefault();	
				id=this.id;
				$("#osx-modal-content").modal({
					overlayId: 'osx-overlay',
					containerId: 'osx-container',
					closeHTML: null,
					minHeight: 80,
					opacity: 65, 
					position: ['0',],
					overlayClose: true,
					onOpen: OSX.open,
					onClose: OSX.close
				});
			});
		},
		open: function (d) {
			var self = this;
			self.container = d.container[0];
			d.overlay.fadeIn('slow', function () {
				var i=0;
				$("#osx-modal-content", self.container).show();
				var title = $("#osx-modal-title", self.container);
				title.show();
				$("#osx-container").block({ css: { 
			        border: 'none', 
			        padding: '15px', 
			        backgroundColor: '#D3D6FF', 
			        '-webkit-border-radius': '10px', 
			        '-moz-border-radius': '10px', 
			        opacity: .3,
			        top:  '200px',
			        color: '#fff' 
			    },
			    message:'<h1><img src="/cloudmanage/resources/images/712.GIF" /> </h1>'
				});
				
				$(".blockMsg").attr("style","z-index: 1011; position: absolute; padding: 15px; margin: 0px; width: 30%; top: 330px; left: 430px; text-align: center; color: rgb(255, 255, 255); border: none; cursor: wait; border-radius: 10px; opacity: 0.3; background-color: rgb(211, 214, 255);"); 
				$.ajax({
						url: appUrl+"/manage/viewServerInstanceDetails",
						type: "POST",
						data: {"instanceId":serverDetails[id].instanceId},
						cache : false,
						success: function(data)
				    	{
															
											$.each(data, function(key,val){
												var addlist=$('.left');
												if(i>24)
													{
													addlist=$('.right');
													}
												if(jQuery.type(val) === "string" || jQuery.type(val) === "boolean" || jQuery.type(val) === "number")
													{
													   if(val !== "")
														{
														   if(jQuery.type(val) === "number" && key === "launchTime")
															   {
															   var date= new Date(val);
															   val=date.toUTCString();
															   }
														    key=toTitleCase(key);
															$('<li><span class="item-label">'+key+'</span><span class="item-value">'+val+'</span></li>').appendTo(addlist);	
															i=i+1;
															return ;
														}
													}
												else if(jQuery.type(val) === "null")
												{
													return ;
												}
												else if(jQuery.type(val) === "object")
													{
													$.each(val, function(k,v){
														if(v !== null && jQuery.type(v) !== "undefined" && v !=="")
															{
															if(key === "state")
																{
																if(k === "code"){
																	$('<li><span class="item-label">'+toTitleCase(key)+'.'+k+'</span><span class="item-value">'+v+'</span></li>').appendTo(addlist);
																}
																else
																	{
																	$('<li><span class="item-label">'+toTitleCase(key)+'</span><span class="item-value">'+v+'</span></li>').appendTo(addlist);
																	}
																}
															else{
																$('<li><span class="item-label">'+key+'.'+k+'</span><span class="item-value">'+v+'</span></li>').appendTo(addlist);	
																}
																i=i+1;
																return;
															}
													});													
													      return ;
													}
												else if(jQuery.type(val) === "array")
													{
														if (jQuery.type(val) !== undefined || val !== null || val.length !== 0)
														{													   								
															$.each(val, function(index,element){
																$.each(element, function(k,v){
																if(jQuery.type(v) === "string" || jQuery.type(v) === "boolean" || jQuery.type(v) === "number")
																{
																	if(v !== "")
																	{
																		if(jQuery.type(v) === "number" && k === "attachTime")
																		   {
																		   var date= new Date(v);
																		   v=date.toUTCString();
																		   }
																		$('<li><span class="item-label">'+key+'.'+k+'</span><span class="item-value">'+v+'</span></li>').appendTo(addlist);	
																		i=i+1;
																		return ;
																	}
																}
																else if(jQuery.type(v) === "null")
																{
																	return ;
																}
																else if(jQuery.type(v) === "object")
																	{
																	$.each(v, function(kk,vv){
																		if(vv !== null && jQuery.type(vv) !== "undefined" && vv !=="")
																			{
																			if(jQuery.type(vv) === "number" && kk === "attachTime")
																			   {
																			   var date= new Date(vv);
																			   vv=date.toUTCString();
																			   }
																				$('<li><span class="item-label">'+key+'.'+k+'.'+kk+'</span><span class="item-value">'+vv+'</span></li>').appendTo(addlist);	
																				i=i+1;
																				return;
																			}
																	});													
																	      return ;
																	}
																});	
															});	
														}
														return;
													}
											});
								$("#osx-container").unblock();		
								jQuery('html,body').animate({scrollTop:0},0);	
			  			}
				  } );  
				d.container.slideDown('slow', function () {
					setTimeout(function () {
						/*var h = $("#osx-modal-data", self.container).height()
							+ title.height()
							+ 400; // padding
							
*/						var h=$("#osx-container").height();
                        if(i>48 && i<54){
						 h =$("#osx-container").height()+100;
                        }
                        else if(i>53 && i<59)
                        	{
                        	h =$("#osx-container").height()+200;
                        	}
                        else if(i>58)
                    	{
                    	h =$("#osx-container").height()+300;
                    	}	
						d.container.animate(
							{height: h}, 
							200,
							function () {
								$("div.close", self.container).show();
								$("#osx-modal-data", self.container).show();
							}
						);
					}, 1200);
				});
			})
			$("#osx-container").attr("style","position: absolute; z-index: 1002; height: 1400px; width: 1229px; left: 60px; top: 40px; margin-bottom:40px;");
		},
		close: function (d) {
			var self = this; // this = SimpleModal object
			d.container.animate(
				{top:"-" + (d.container.height() + 20)},
				500,
				function () {
					self.close(); // or $.modal.close();
				}
			);
		}
	};

	OSX.init();
	function toTitleCase(str) {
	    return str.replace(/(?:^|\s)\w/g, function(match) {
	        return match.toUpperCase();
	    });
	}
});