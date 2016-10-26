'use strict';
/**
* @ngdoc directive
* @name cloudmanageApp.directive:billingTimelineChart
* @description
* # billingTimelineChart
*/
angular.module('cloudmanageApp')
.directive('billingTimelineChart', ['utils', function (utils) {

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

	function timelineChartConfig(elementId){
		highChartThemes();
		return {
			chart : {
        		renderTo : elementId,
        		zoomType : 'xy'
        	},
	        title: {
	            text: 'Cost Trend',
	            x: -20 //center
	        },
	        exporting : {
	        	enabled : false
	        },
	        xAxis: {
	            categories: []
	        },
	        yAxis: {
	            title: {
	                text: 'Cost ($)'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valuePrefix: '$ '
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },
	        series: []
	    };
	}

	function getArrayFromObject(jsonObj){
		return Object.keys(jsonObj).map(function(key){
			return jsonObj[key];
		});
	}

	function formatKey(input){
		var month = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
	                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
			length = input.toString().length;

		if(length === 6){
			// get month
			return month[parseInt(input.toString().substr(4,6)) - 1];
		}else{
			return input.toString().substr(6,8);
		}
	}

	function getXAxis(obj){
		var xAxisHeader = [];

		for(var date in obj){
			if(obj.hasOwnProperty(date)){
				xAxisHeader.push(formatKey(date));
			}
		}
		return xAxisHeader;
	}

	function getTimelineData(data){
		var timelineData = [],
			xAxisHeader = [],
			count = 0;

		for(var client in data){
			if(data.hasOwnProperty(client)){
				if(count === 0){
					xAxisHeader = getXAxis(data[client]);
					count++;
				}
				timelineData.push({
					name : client,
					data : getArrayFromObject(data[client])
				});
			}
		}
		return {"series" : timelineData,"xAxis" : xAxisHeader};
	}

	return {
		restrict: 'A',
		scope: {
			data : '=billingTimelineData'
		},
		link: function postLink(scope, element) {
			var chart = null,
				parseData = {},
				clients = [],
				config = timelineChartConfig(element[0].id);
			scope.$watch('data', function(data){
				if(data){
					parseData = getTimelineData(data);
					config.series = parseData["series"];
					config.xAxis.categories = parseData["xAxis"];
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
