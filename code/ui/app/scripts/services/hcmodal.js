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

  function defineModalInstance(text, type){
    var modalInstance =  $modal.open({
        templateUrl: 'templates/_dialog.html',
        controller: 'ModalCtrl',
        controllerAs: 'modalCtrl',
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
      var modalInstance =  defineModalInstance(text, 'alert');
      return modalInstance.result;
    },
    confirm: function(text){
      var modalInstance =  defineModalInstance(text, 'confirm');
      return modalInstance.result;
    }
  };
}]);
