'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:CostOptimizationCtrl
 * @description
 * # CostOptimizationCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
    .controller('CostOptimizationCtrl', ['instance', 'jobDetails', 'alarm','ec2Service', '$uibModalInstance', '$timeout','utils',
        function(instance, jobDetails, instanceAlarm,ec2Service, $modalInstance, $timeout,utils) {
            this.jobDetails = jobDetails;
            this.instance = instance;
            this.update = (jobDetails) ? true : false;
            this.cron = {
                start: (jobDetails ? jobDetails.start.cron : null),
                stop: (jobDetails ? jobDetails.stop.cron : null)
            };
            this.cloudWatch = {
              alarm : instanceAlarm.isEnable,
              threshold : instanceAlarm.threshold,
              frequency : utils.convertSecondToHr(instanceAlarm.frequency)
            };
            this.lastMonthCost = 400;
            var response = {};
            this.response = response;
            this.submit = function() {
                var startCron = this.cron.start,
                    stopCron = this.cron.stop,
                    promise = null;
                if (this.update) {
                    promise = ec2Service.updateSchedule(instance, startCron, stopCron);
                } else {
                    promise = ec2Service.createSchedule(instance, startCron, stopCron);
                }
                promise.then(function(data) {
                    setResponse('success', 'Instance scheduled successfully');
                    closeModal(data);
                }, function(data) {
                    setResponse('danger', 'Instance not scheduled successfully. Please refer to console.');
                });
            };
            this.delete = function() {
                ec2Service.deleteSchedule(instance).then(function(response) {
                    setResponse('success', 'Schedule deleted successfully!');
                    closeModal(response);
                }, function(data) {
                    setResponse('danger', 'Cowardly refuses to delete schedule. Please refer to console.');
                });
            };
            this.cancel = function() {
                $modalInstance.dismiss('cancel');
            };

            function closeModal(data) {
                $timeout(function() {
                    $modalInstance.close(data);
                }, 2 * 1000);
            };

            function setResponse(status, message) {
                response.status = status;
                response.message = message;
            };
        }
    ]);
