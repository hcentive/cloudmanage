'use strict';
/**
* @ngdoc directive
* @name cloudmanageApp.directive:billingStackedChart
* @description
* # billingStackedChart
*/
angular.module('cloudmanageApp')
.directive('billingStackedChart', ['utils', function (utils) {

	function highChartThemes(){
		Highcharts.theme = {
		    colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', 
		             '#FF9655', '#FFF263', '#6AF9C4','#0B3B50','#2A6119','#8E3714',
		             '#9FA12A','#2C2F2F','#E3E178','#A15B30','#B416BA','#4B7767'],
		    chart: {
		        backgroundColor: {
		            linearGradient: [0, 0, 500, 500],
		            stops: [
		                [0, 'rgb(255, 255, 255)'],
		                [1, 'rgb(240, 240, 255)']
		            ]
		        },
		    },
		    title: {
		        style: {
		            color: '#000',
		            font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
		        }
		    },
		    subtitle: {
		        style: {
		            color: '#666666',
		            font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
		        }
		    },

		    legend: {
		        itemStyle: {
		            font: '9pt Trebuchet MS, Verdana, sans-serif',
		            color: 'black'
		        },
		        itemHoverStyle:{
		            color: 'gray'
		        }   
		    }
		};
		Highcharts.setOptions(Highcharts.theme);
	}

	function stackedChartConfig(elementId){
		highChartThemes();
		return {
			chart: {
	            type: 'column',
	            renderTo : elementId,
	            zoomType : 'xy',
	            height:600,
	            events: {
	            	load: function(){
	            		var chart = this,
	            			legend = chart.legend,
	            			visibleStackTag = {
								"qa" : "qa",
								"prod":"prod",
								"dev":"dev",
								"sit":"sit",
								"uat":"uat"
							};
						
						_.each(legend.allItems,function(item){
							
							if(!visibleStackTag.hasOwnProperty(item.name.toLowerCase())){
								item.update({
									visible:false
								});
							}
						});	     
	            	}
	            }
	        },
	        title: {
	            text: 'Cost per Client'
	        },
	        xAxis: {
	            categories : [],
	            title :{
	            	text : 'Client '
	            }
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: 'Billing Cost ( $ )'
	            },
	            stackLabels: {
	                enabled: true,
	                style: {
	                    fontWeight: 'bold',
	                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
	                }
	            }
	        },
	        legend: {
	            align: 'right',
	            x: -30,
	            verticalAlign: 'top',
	            y: 25,
	            floating: true,
	            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
	            borderColor: '#CCC',
	            borderWidth: 1,
	            shadow: false
	        },
	        tooltip: {
	            headerFormat: '<b>{point.x}</b><br/>',
	            pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
	        },
	        plotOptions: {
	            column: {
	                stacking: 'normal',
	                dataLabels: {
	                    enabled: true,
	                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
	                    formatter:function(){
	                    	if(this.y != 0){
	                    		return this.y;
	                    	}
	                    }
	                }
	            }
	        },
        	exporting: { enabled: false },
        	series: []
		}
	}

	function getDefaultStackArray(length){
		var stacks = Array.apply(null, Array(length)).map(Number.prototype.valueOf,0);
		return stacks;
	}

	function getStackTagData(stackOutputObj,stackInputObj,client,clientIndexMap){

		var clientLength = Object.keys(clientIndexMap).length,
			stackTagArray;
		for(var stackTag in stackInputObj){
			if(stackInputObj.hasOwnProperty(stackTag)){

				if(stackOutputObj.hasOwnProperty(stackTag)){
					// stack tag is present
					stackTagArray = stackOutputObj[stackTag];
					stackTagArray[clientIndexMap[client]] = stackInputObj[stackTag];
				}else{
					// stack tag is not present
					stackTagArray = getDefaultStackArray(clientLength);
					stackTagArray[clientIndexMap[client]] = stackInputObj[stackTag];
					stackOutputObj[stackTag] = stackTagArray;
				}
			}
		}
	}

	function getStackedChartData(data,clients){
		var series = [],clientIndexMap = {},
		stackClientDataMap = {},stackTag,client,
		stackObj = {};

		// Map by client
		_.each(clients,function(client,index){
			clientIndexMap[client] = index;
		});

		for(client in data){
			if(data.hasOwnProperty(client)){
				stackObj = data[client];
				getStackTagData(stackClientDataMap,stackObj,client,clientIndexMap);
			}
		}
		
		for(stackTag in stackClientDataMap){
			if(stackClientDataMap.hasOwnProperty(stackTag)){

				series.push({
					"name":stackTag,
					"data":stackClientDataMap[stackTag]
				});
			}
		}
		return series;
	}

	return {
		restrict: 'A',
		scope: {
			data : '=billingData'
		},
		link: function postLink(scope, element) {
			var chart = null,
				parseData = {},
				clients = [],
				config = stackedChartConfig(element[0].id);
			scope.$watch('data', function(data){
				if(data){
					clients = Object.keys(data);
					config.xAxis.categories = clients;
					config.series = getStackedChartData(data,clients);
					chart = new Highcharts.Chart(config);
				}
			});

			scope.$on("navigation-menu:resize",function(event,data){
				if(chart){
					chart = new Highcharts.Chart(config);
				}
			});
		}
	};
}]);
