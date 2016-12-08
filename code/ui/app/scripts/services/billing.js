'use strict';

/**
 * @ngdoc factory
 * @name cloudmanageApp.billing
 * @description
 * # billing factory
 * Factory in the cloudmanageApp.
 */

angular.module('cloudmanageApp')
  .factory('billingFactory', [function() {
    var billingFactory = {},
      _uniqueStackMap = {},
      stackedDataByClient = {};

    billingFactory.defaultClientSelection = function(billingDataByClient) {
      var clientCount = 0,
        selectedClients = [],
        isChecked = false;

      for (var client in billingDataByClient) {
        if (billingDataByClient.hasOwnProperty(client)) {
          if (clientCount > 4) {
            isChecked = false;
          } else {
            isChecked = true;
          }

          if (client === "unknown" || client === "") {
            isChecked = false;
          } else {
            clientCount++;
          }

          selectedClients.push({
            "name": client,
            "isChecked": isChecked ? true : false
          });
        }
      }
      return selectedClients;
    }

    billingFactory.filterSelectedClient = function(clients, data) {
      var selectedClients = {};

      if (data === undefined || data === null) {
        data = stackedDataByClient
      }
      _.each(clients, function(client) {
        if (client.isChecked) {
          selectedClients[client.name] = data[client.name];
        }
      });
      return selectedClients;
    }

    return billingFactory;
  }]);
