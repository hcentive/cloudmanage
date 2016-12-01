'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:CostOptimizationCtrl
 * @description
 * # CostOptimizationCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
    .controller('CostOptimizationCtrl', ['instance', 'jobDetails', 'alarm','instanceBillingCost',
    'ec2Service', '$uibModalInstance', '$timeout', 'utils', 'cloudwatchService',
        function(instance, jobDetails, instanceAlarm, instanceCost,ec2Service, $modalInstance, $timeout, utils, cloudwatchService) {

            var self = this,
                response = {};

            function isAlarmFieldEditable(instance) {
                var isEditable = false,
                    tags = instance.awsInstance.tags;

                _.each(tags, function(tag) {
                    if (tag["key"] === "Stack") {
                        if (tag["value"].toLowerCase() === "qa" || tag["value"].toLowerCase() === "dev") {
                            isEditable = true;
                        }
                    }
                });
                return isEditable;
            }

            function init() {
                self.jobDetails = jobDetails;
                self.instance = instance;
                self.update = (jobDetails) ? true : false;
                self.cron = {
                    start: (jobDetails ? jobDetails.start.cron : null),
                    stop: (jobDetails ? jobDetails.stop.cron : null)
                };
                self.cronClone = angular.copy(self.cron);
                self.alarm = instanceAlarm;
                self.instanceAlarmClone = angular.copy(instanceAlarm);
                self.response = response;
                self.isAlarmEditable = isAlarmFieldEditable(instance);
                self.instanceCost = instanceCost;
                self.createAlarmClicked = false;
            }

            self.submit = function() {

                var alarmPromise = null,
                    startCron = self.cron.start,
                    stopCron = self.cron.stop,
                    promise = null;

                if (!utils.isEqual(self.instanceAlarmClone, self.alarm)) {
                    // alarm parameter are changed so call backend api for update

                    alarmPromise = cloudwatchService.updateAlarm(self.alarm);
                    alarmPromise.then(function(data) {}, function(data) {
                        setResponse('danger', 'Cloudwatch Alarm not scheduled successfully. Please refer to console');
                    });
                }

                if (self.update) {
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
            self.delete = function() {
                ec2Service.deleteSchedule(instance).then(function(response) {
                    setResponse('success', 'Schedule deleted successfully!');
                    closeModal(response);
                }, function(data) {
                    setResponse('danger', 'Cowardly refuses to delete schedule. Please refer to console.');
                });
            };
            self.cancel = function() {
                $modalInstance.dismiss('cancel');
            };

            self.createAlarm = function(){
                self.createAlarmClicked = true;
                self.alarm.instanceId = self.instance.awsInstance.instanceId;
                self.alarm.frequency = 24;
                self.alarm.threshold = 1;
                self.alarm.enable = true;
            }

            function closeModal(data) {
                $timeout(function() {
                    $modalInstance.close(data);
                }, 2 * 1000);
            };

            function setResponse(status, message) {
                response.status = status;
                response.message = message;
            };

            init();
        }
    ]);
