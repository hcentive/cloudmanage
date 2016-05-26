'use strict';
/**
* @ngdoc directive
* @name cloudmanageApp.directive:billingChart
* @description
* # billingChart
*/
angular.module('cloudmanageApp')
.directive('billingChart', ['utils', function (utils) {

	var colors = ['#7cb5ec', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
   '#f15c80', '#e4d354', '#2b908f', '#f45b5b', '#91e8e1'];


	var baseColors = {};

	function getColor(stack, count){
		var shade = 0,
		stack = stack || 'misc',
		color = baseColors[stack];
		if(! color){
			color = colors[_.keys(baseColors).length];
			baseColors[stack] = color;
		}
		return color;

	}

	function parseData(data){
		var costCenterData = {};
		_.each(data, function(billingInfo){
			var instanceInfo = billingInfo.instanceInfo,
			cost = billingInfo.dayTotal;
			addCostForCostCenter(costCenterData, instanceInfo, cost);
		});
		return {
			main : _.values(costCenterData)
		};
	}

	function addCostForCostCenter(costCenterData, instanceInfo, cost){
		var instanceName = instanceInfo.name,
		stack = instanceInfo.stack,
		costCenter = instanceInfo.costCenter || 'Misc', 
		_costDataPoint = costCenterData[costCenter];
		if(_costDataPoint === undefined){
			_costDataPoint = {
				name: costCenter,
				y: 0,
				drilldown: true,
				drillDownData : {
					name: costCenter,
					data: {}
				}
			};
			costCenterData[costCenter] = _costDataPoint;
		}
		_costDataPoint.y += cost;
		var _instanceData = _costDataPoint.drillDownData.data[instanceName];
		if(_instanceData === undefined){
			_costDataPoint.drillDownData.data[instanceName] = {
				cost: 0,
				stack: stack
			};
		}
		_costDataPoint.drillDownData.data[instanceName].cost += cost;
	}

	function getOptions(data, elementId){
		var options = {
			chart: {
				type: 'pie',
	            renderTo: elementId,
	            events: {
	            	drilldown: function(e){
	            		var chart = this,
	            		count = -1,
	            		drillDownData = {};
	            		drillDownData.name = e.point.drillDownData.name;
	            		var drillDownDataLength = _.keys(e.point.drillDownData.data).length;
	            		drillDownData.data = _.map(e.point.drillDownData.data, function(obj, instanceName){
	            			count++;
	            			return {
	            				name: instanceName,
	            				y: obj.cost,
	            				color: getColor(obj.stack, count),
	            				stack: obj.stack
	            			}
		            		}).sort(function(a, b){
	            			var aStack = a.stack || 'misc',
	            			bStack = b.stack || 'misc';
	            			if(aStack === bStack)
	            				return 0;
	            			return (aStack < bStack) ? -1: 1;
	            		});
	            		chart.addSeriesAsDrilldown(e.point, drillDownData);
	            	}
	            }
	        },
	        plotOptions: {
	            series: {
	                dataLabels: {
	                    enabled: true,
	                    format: '{point.name}: ${point.y:,.2f}'
	                }
	            }
        	},
        	  tooltip: {
				headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
				pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>${point.y:,.2f}</b><br/>'
        	},
	        title: {
	            text: 'Billing by Cost center'
	        },
	        exporting:{
	        	enabled: false
	        },
	        series: [
	        {
	            name: 'Cost Center',
	            colorByPoint: true,
	            data: data.main
	        }
	        ],
	        drilldown: {
            	series: []
        	}
	    };
	    return options;
	}

	return {
		restrict: 'A',
		scope: {
			data : '=billingChartData'
		},
		link: function postLink(scope, element) {
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
					}
				}
			});
		}
	};
}]);
