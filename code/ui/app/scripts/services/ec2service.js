'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.ec2Service
 * @description
 * # ec2Service
 * Service in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .service('ec2Service', ['$http', '$log', function ec2Service($http, $log) {

    this.performActionOnInstance = function(action, instance) {
      var instanceID = this.getInstanceId(instance),
        url = '/instances/' + instanceID,
        params = {
          action: action
        };
      return $http.put(url, {}, {
        params: params,
        ignoreTracker: true
      }, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
    };

    var fetchData = function(url, polling) {
      var params = {};
      return $http.get(url, {
        params: params,
        ignoreTracker: polling
      }).then(function(response) {
        return response.data;
      });
    }

    this.getTagValue = function(instance, tagKey) {
      var tags = instance.awsInstance.tags,
        lTagKey = tagKey.toLowerCase(),
        targetTag = _.find(tags, function(tag) {
          if (tag.key.toLowerCase() === lTagKey)
            return true;
          return false;
        });
      return (targetTag) ? targetTag.value : null;
    };

    this.getVpcs = function() {
      return $http.get('/vpcs').then(function(response) {
        return response.data;
      });
    };

    this.getInstances = function(polling) {
      return fetchData('/instances', polling);
    };

    this.getResourcesMetaData = function(polling) {
      return fetchData('/dashboard/resourceMetaData', polling);
    };

    this.stopInstance = function(instance) {
      return this.performActionOnInstance('stop', instance);
    };

    this.startInstance = function(instance) {
      return this.performActionOnInstance('start', instance);
    };

    this.terminateInstance = function(instance) {
      return this.performActionOnInstance('terminate', instance);
    };

    this.getInstanceName = function(instance) {
      return this.getTagValue(instance, 'Name');
    };

    this.getCostCenter = function(instance) {
      return this.getTagValue(instance, 'cost-center');
    };

    this.getInstanceId = function(instance) {
      return instance.awsInstance.instanceId
    };

    this.createSchedule = function(instance, startCron, stopCron) {
      return this.schedule(instance, startCron, stopCron, 'create');
    };

    this.updateSchedule = function(instance, startCron, stopCron) {
      return this.schedule(instance, startCron, stopCron, 'update');
    };

    this.schedule = function(instance, startCron, stopCron, type) {
      var costCenter = this.getCostCenter(instance),
        instanceId = this.getInstanceId(instance),
        url = (type === 'create') ? '/instances/schedule/' + instanceId : '/instances/schedule/update/' + instanceId;
      return $http.post(url, {
        startJobTriggerInfo: {
          costCenter: costCenter,
          cronExpression: startCron,
          instanceId: instanceId
        },
        stopJobTriggerInfo: {
          costCenter: costCenter,
          cronExpression: stopCron,
          instanceId: instanceId
        }
      }, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
    };

    this.deleteSchedule = function(instance) {
      var costCenter = this.getCostCenter(instance),
        instanceId = this.getInstanceId(instance);
      return $http.post('/instances/schedule/delete/' + instanceId, {
        costCenter: costCenter
      }, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
    };

    this.getJobDetails = function(instance) {
      var instanceId = this.getInstanceId(instance);
      return $http.get('/instances/schedule/' + instanceId)
        .then(function(data) {
          return data.data;
        });
    };

    this.getInstanceByIP = function(ipAddress) {
      return $http.get('/instances/ip/' + ipAddress, {
          ignoreTracker: true
        })
        .then(function(response) {
          return response.data;
        });
    };

    this.getCPUUtilization = function(instance, filter) {
      var fromDateString = moment(filter.fromDate).format('MM/DD/YYYY'),
        toDateString = moment(filter.toDate).format('MM/DD/YYYY'),
        instanceId = this.getInstanceId(instance),
        url = '/instances/cpu/' + instanceId + '?fromDate=' + fromDateString + '&toDate=' + toDateString;
      return $http.get(url, {
          ignoreTracker: true
        })
        .then(function(response) {
          return response.data;
        });
    };

    this.getInstanceMetaList = function() {
      return $http.get('/instances/meta').then(function(response) {
        return response.data;
      });
    };

    this.getBillingInfo = function(filter) {
      var fromDateString = moment(filter.fromDate).format('MM/DD/YYYY'),
        toDateString = moment(filter.toDate).format('MM/DD/YYYY'),
        url = '/billing' + '?from=' + fromDateString + '&to=' + toDateString;
      return $http.get(url)
        .then(function(response) {
          return response.data;
        });
    };

    this.getBillingInfoByClient = function(filter) {
      var fromDateString = moment(filter.fromDate).format('MM/DD/YYYY'),
        toDateString = moment(filter.toDate).format('MM/DD/YYYY'),
        url = '/billing/cost/client' + '?from=' + fromDateString + '&to=' + toDateString;
      return $http.get(url)
        .then(function(response) {
          return response.data;
        });
    };

    this.getBillingTrendByClient = function(filter) {
      var fromDateString = moment(filter.fromDate).format('MM/DD/YYYY'),
        toDateString = moment(filter.toDate).format('MM/DD/YYYY'),
        url = '/billing/trend/client' + '?from=' + fromDateString + '&to=' + toDateString;
      return $http.get(url)
        .then(function(response) {
          return response.data;
        });
    };

    this.getInstanceBillingCost = function(dateFilter, instance) {
      var fromDate = moment(dateFilter.fromDate).format('MM/DD/YYYY'),
        toDate = moment(dateFilter.toDate).format('MM/DD/YYYY'),
        url = '/billing/cost?' + 'from=' + fromDate + '&to=' + toDate + '&instanceId=' + instance.awsInstance.instanceId;

      return $http.get(url).then(function(response) {
        return response.data;
      }, function(response) {
        return 0; // no data found
      })
    };
  }]);
