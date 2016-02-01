'use strict';

/**
 * @ngdoc function
 * @name cloudmanageApp.controller:DiagnoseinstanceCtrl
 * @description
 * # DiagnoseinstanceCtrl
 * Controller of the cloudmanageApp
 */
angular.module('cloudmanageApp')
  .controller('DiagnoseinstanceCtrl', ['ec2Service','$uibModalInstance','securityContextHolder','promiseTracker',
    function (ec2Service, $modalInstance, securityContextHolder, promiseTracker) {
    	var that = this;
    	this.ipAddress = null;
      this.loadingTracker = promiseTracker();
    	this.findInstance = function(){
    		var promise = ec2Service.getInstanceByIP(this.ipAddress)
    		.then(function(data){
    			that.instance = data;
          that.instanceVsUserTags = getInstanceVsUserTags(data);
    		});
        this.loadingTracker.addPromise(promise);
    	};
    	this.dismiss = function () {
    		$modalInstance.dismiss('cancel');
    	};
      var getInstanceVsUserTags = function(instance){
        var profile = securityContextHolder.profile,
        tags = profile.tags;
        return _.map(tags, function(tag){
          var tagType = tag.tagType,
          instanceTagValue = ec2Service.getTagValue(instance, tagType),
          userTagValue = tag.tagValue;
          return {
            tagType: tagType,
            instanceTagValue: instanceTagValue,
            userTagValue : userTagValue
          }
        });
      };
  }]);
