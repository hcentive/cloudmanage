'use strict';
/**
* @ngdoc directive
* @name cloudmanageApp.directive:billingChart
* @description
* # billingChart
*/
angular.module('cloudmanageApp')
.directive('billingChart', function () {

	function parseData(data){
		var costCenterData = {};
		_.each(data, function(billingInfo){
			var instanceInfo = billingInfo.instanceInfo,
			instanceName = instanceInfo.name,
			costCenter = instanceInfo.costCenter,
			cost = billingInfo.dayTotal;
			addCostForCostCenter(costCenter, instanceName, costCenterData, cost);
		});
		return {
			main : _.values(costCenterData)
		};
	}

	function addCostForCostCenter(costCenter, instanceName, costCenterData, cost){
		costCenter = costCenter || "Misc";
		var _costDataPoint = costCenterData[costCenter];
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
			_costDataPoint.drillDownData.data[instanceName] = 0;
		}
		_costDataPoint.drillDownData.data[instanceName] += cost;
	}

	function getOptions(data, elementId){
		var options = {
			chart: {
				type: 'pie',
	            renderTo: elementId,
	            events: {
	            	drilldown: function(e){
	            		var chart = this,
	            		drillDownData = {};
	            		drillDownData.name = e.point.drillDownData.name;
	            		drillDownData.data = _.pairs(e.point.drillDownData.data);
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
						//chart.drillDown.series = parsedData.drillDown;
						chart.series[0].setData(parsedData.main);
					}
				}
			});
		}
	};
});
