'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:BillingCtrl
 * @description
 * # BillingCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('BillingCtrl', ['$scope','ec2Service','billingFactory',function ($scope,ec2Service,billingFactory) {
  	var that = this;

    this.selectedTab = 'cost';

    function displaySelectedClientString(clients){
      var displayString = clients.join(),
          displayStringLength = displayString.length,
          documentWidth = window.innerWidth,
          defaultDocumentWidth = 1366,
          characterToSkip = parseInt((((defaultDocumentWidth - documentWidth) / defaultDocumentWidth) * 100)) * 2,
          characterCount = 130 - characterToSkip;


      if(displayStringLength > characterCount){
          displayString = "";
          _.each(clients,function(client){
              displayStringLength = displayString.length;
              if((displayStringLength + client.length) < characterCount){
                  displayString += client + ",";
              }
          });
          displayString += "...";
      }
      $scope.selectedClientString = displayString;
    }

  	this.filter = {
  		fromDate: moment().subtract(1, 'months').toDate(),
  		toDate: new Date()
  	};
  	this.filterBillingCostData = function(){
      var stackedDataByClient;
  		ec2Service.getBillingInfo(this.filter).then(function(data){
          stackedDataByClient = billingFactory.parseBillingData(data);
          if($scope.clients === undefined || $scope.clients === null || $scope.clients.length === 0){
            $scope.clients = billingFactory.defaultClientSelection(stackedDataByClient);  
          }
          that.dataPoints = billingFactory.filterSelectedClient($scope.clients);
          displaySelectedClientString(Object.keys(that.dataPoints));
    	});
    };
  	this.fromDatePopover = {
  		open: function(){
  			this.opened = true;
  		},
  		opened: false
  	};
  	this.toDatePopover = {
  		open: function(){
  			this.opened = true;
  		},
  		opened: false
  	};
    this.filterClient = function(){
      if(this.selectedTab === 'trend'){
        this.filterBillingTrendData();
      }else{
        this.dataPoints = billingFactory.filterSelectedClient($scope.clients);
        displaySelectedClientString(Object.keys(this.dataPoints));  
      }
    };
    this.filterByMonth = function(duration){
      this.filter = {
        fromDate : moment().subtract(parseInt(duration), 'months').toDate(),
        toDate: new Date()
      }
      if(this.selectedTab === 'trend'){
        this.filterBillingTrendData();
      }else{
        this.filterBillingCostData();  
      }
      
    };
    this.tabPanel = function(tab){
      if(tab === 'cost'){
        this.selectedTab = 'cost';
        this.filterBillingCostData();
      }else if(tab === 'trend'){
        this.selectedTab = 'trend';
        this.filterBillingTrendData();
      }
    };
    this.filterData = function(){
      if(this.selectedTab === 'trend'){
        this.filterBillingTrendData();
      }else{
        this.filterBillingCostData();
      }
    };
    this.filterBillingTrendData = function(){
      var timePeriodDataByClient,
          trendBy = billingFactory.trendBy(this.filter.fromDate,this.filter.toDate);

      ec2Service.getBillingInfo(this.filter).then(function(data){
        timePeriodDataByClient = billingFactory.parseBillingTrendData(data,trendBy);
        that.timeLineData = billingFactory.filterSelectedClient($scope.clients,timePeriodDataByClient);
      });
    };
    this.filterBillingCostData();    
}]);
