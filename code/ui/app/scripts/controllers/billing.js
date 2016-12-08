'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:BillingCtrl
 * @description
 * # BillingCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('BillingCtrl', ['$scope', 'ec2Service', 'billingFactory', function($scope, ec2Service, billingFactory) {
    var self = this,
      billingCostByClientStack = {},
      billingCostTrendByClient = {};

    function displaySelectedClientString(clients) {
      var displayString = clients.join(),
        displayStringLength = displayString.length,
        documentWidth = window.innerWidth,
        defaultDocumentWidth = 1366,
        characterToSkip = parseInt((((defaultDocumentWidth - documentWidth) / defaultDocumentWidth) * 100)) * 2,
        characterCount = 130 - characterToSkip;

      if (displayStringLength > characterCount) {
        displayString = "";
        _.each(clients, function(client) {
          displayStringLength = displayString.length;
          if ((displayStringLength + client.length) < characterCount) {
            displayString += client + ",";
          }
        });
        displayString += "...";
      }
      self.selectedClientString = displayString;
    }

    function init() {
      self.selectedTab = 'cost';
      self.filterBillingCostByClientData();
    }

    self.filter = {
      fromDate: moment().subtract(1, 'months').toDate(),
      toDate: new Date()
    };

    self.filterBillingCostByClientData = function() {
      ec2Service.getBillingInfoByClient(self.filter).then(function(data) {
        billingCostByClientStack = data;
        if (self.clients === undefined || self.clients === null || self.clients.length === 0) {
          self.clients = billingFactory.defaultClientSelection(billingCostByClientStack);
        }
        self.dataPoints = billingFactory.filterSelectedClient(self.clients, billingCostByClientStack);
        displaySelectedClientString(Object.keys(self.dataPoints));
      });
    };

    self.filterBillingTrendByClientData = function() {
      ec2Service.getBillingTrendByClient(self.filter).then(function(data) {
        billingCostTrendByClient = data;
        self.timeLineData = billingFactory.filterSelectedClient(self.clients, billingCostTrendByClient);
      });
    };

    self.fromDatePopover = {
      open: function() {
        this.opened = true;
      },
      opened: false
    };

    self.toDatePopover = {
      open: function() {
        this.opened = true;
      },
      opened: false
    };

    self.filterClient = function() {
      if (self.selectedTab === 'trend') {
        self.timeLineData = billingFactory.filterSelectedClient(self.clients, billingCostTrendByClient);
        displaySelectedClientString(Object.keys(self.timeLineData));
      } else {
        self.dataPoints = billingFactory.filterSelectedClient(self.clients, billingCostByClientStack);
        displaySelectedClientString(Object.keys(self.dataPoints));
      }
    };

    self.filterByMonth = function(duration) {
      self.filter = {
        fromDate: moment().subtract(parseInt(duration), 'months').toDate(),
        toDate: new Date()
      }
      if (self.selectedTab === 'trend') {
        self.filterBillingTrendByClientData();
      } else {
        self.filterBillingCostByClientData();
      }
    };

    self.tabPanel = function(tab) {
      if (tab === 'cost') {
        self.selectedTab = 'cost';
        self.filterBillingCostByClientData();
      } else if (tab === 'trend') {
        self.selectedTab = 'trend';
        self.filterBillingTrendByClientData();
      }
    };

    self.filterData = function() {
      if (self.selectedTab === 'trend') {
        self.filterBillingTrendByClientData();
      } else {
        self.filterBillingCostByClientData();
      }
    };

    init();
  }]);
