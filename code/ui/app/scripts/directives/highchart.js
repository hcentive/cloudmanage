'use strict';

/**
 * @ngdoc directive
 * @name cloudmanageApp.directive:highchart
 * @description
 * # highchart
 */
angular.module('cloudmanageApp')
  .directive('highchart', function () {
  	var parseData = function(data){
  		return _.map(data, function(item){
  			var average = Math.floor(item.average*100)/100;
  			return [item.timestamp, average];
  		});
  	},
  	getOptions = function(data, elementId){
  		var options = {
	        chart: {
	            zoomType: 'x',
	            renderTo: elementId,
	        },
	        title: {
	            text: 'CPU Usage'
	        },
	        subtitle: {
	            text: document.ontouchstart === undefined ?
	                    'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
	        },
	        xAxis: {
	            type: 'datetime'
	        },
	        yAxis: {
	            title: {
	                text: 'CPU Usage'
	            }
	        },
	        legend: {
	            enabled: false
	        },
	        exporting:{
	        	enabled: false
	        },
	        plotOptions: {
	            area: {
	                fillColor: {
	                    linearGradient: {
	                        x1: 0,
	                        y1: 0,
	                        x2: 0,
	                        y2: 1
	                    },
	                    stops: [
	                        [0, Highcharts.getOptions().colors[0]],
	                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
	                    ]
	                },
	                marker: {
	                    radius: 2
	                },
	                lineWidth: 1,
	                states: {
	                    hover: {
	                        lineWidth: 1
	                    }
	                },
	                threshold: null
	            }
	        },
	        series: [{
	            type: 'area',
	            name: 'CPU Usage',
	            data: data
	        }]
	    };
	    return options;
  	};
  	
    return {     
      restrict: 'A',
      scope: {
      	data : '=highchartData'
      },
      link: function postLink(scope, element, attrs) {
   		var chart = null;
   		scope.$watch('data', function(data){
   			var parsedData = parseData(data);
   			if(data){
   				if(! chart){
   					var options = getOptions(parsedData, element[0].id);
   					chart = new Highcharts.Chart(options);
   				}
   				else{
   					chart.series[0].setData(parsedData);
   				}
   			}
   		});
      }
    };
  });
