'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.hcModal
 * @description
 * # hcModal
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
 .factory('hcModal', ['$uibModal', function ($modal) {

  function defaultOptions(){
    return {
      templateUrl: 'templates/_dialog.html',
      controller: 'ModalCtrl',
      controllerAs: 'modalCtrl', 
    };
  }

  function defineModalInstance(text, type,options){
    var modalInstance =  $modal.open({
        templateUrl: options.templateUrl,
        controller: options.controller,
        controllerAs: options.controllerAs,
        resolve: {
          text: function () {
            return text;
          },
          type: function(){
            return type;
          }
        }
      });
    return modalInstance;
  }
  return {
    alert: function (text) {
      var modalInstance =  defineModalInstance(text, 'alert',defaultOptions());
      return modalInstance.result;
    },
    confirm: function(text){
      var modalInstance =  defineModalInstance(text, 'confirm',defaultOptions());
      return modalInstance.result;
    },
    info:function(text){
      var options = defaultOptions(),
          modalInstance;

      options.templateUrl = 'templates/_infodialog.html';
      modalInstance = defineModalInstance(text,'info',options);
      return modalInstance.result;
    }
  };
}]);
