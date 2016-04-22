'use strict';

/**
 * @ngdoc directive
 * @name cloudmanageApp.directive:highchart
 * @description
 * # highchart
 */
angular.module('cloudmanageApp')
  .directive('highchart', ['utils',function (utils) {
  	var parseData = function(data){
  		var mainData = _.map(data, function(item){
  			var average = utils.uptoDecimals(item.average, 3);
  			return [item.timestamp, average];
  		});
  		var averageData = getAverage(mainData);
  		return {
  			main : mainData,
  			average: averageData
  		};
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
	        series: [
	        {
	            name: 'CPU Usage',
	            data: data.main
	        },
	        {
	            type: 'line',
	            name: 'Average',
	            data: data.average
	        }]
	    };
	    return options;
  	},
  	getAverage = function(data){
  		var _data = {};
  		_.each(data, function(item){
  			var timestamp = item[0],
  			cpuUsage = item[1],
  			startOfDayTimeStamp = utils.getStartOfDayTimeStamp(timestamp);
  			if(_data[startOfDayTimeStamp] == undefined){
  				_data[startOfDayTimeStamp] = [];
  			}
  			_data[startOfDayTimeStamp].push(cpuUsage);
  		});
  		return _.map(_data, function(cpuUsages, timestamp){
  			var avg = utils.uptoDecimals(utils.average.apply(utils, cpuUsages), 3);
  			return [+timestamp, avg];
  		});
  	}
  	
    return {     
      restrict: 'A',
      scope: {
      	data : '=highchartData'
      },
      link: function postLink(scope, element, attrs) {
   		var chart = null;
   		scope.$watch('data', function(data){
   			if(data){
   				var parsedData = parseData(data);
   				if(! chart){
   					var options = getOptions(parsedData, element[0].id);
   					chart = new Highcharts.Chart(options);
   				}
   				else{
   					chart.series[0].setData(parsedData.main);
   					chart.series[1].setData(parsedData.average);
   				}
   			}
   		});
      }
    };
  }]);
