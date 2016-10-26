'use strict';

/**
 * @ngdoc directive
 * @name cloudmanageApp.directive:toggleMenuBar
 * @description
 * # toggleMenuBar
 */
angular.module('cloudmanageApp')
  .directive('toggleMenuBar',['$rootScope',function ($rootScope) {
    return { 
      restrict: 'A',
      scope:{
        isExpanded: '=',
        toggleClass:'=',
        pageWrapper:'='
      },
      link: function postLink(scope, element, attrs) {
      	 var iconElement;

         element.css('cursor','pointer');
         element.bind('click',function(){
            iconElement = $(this).find("span").first();
            if(scope.isExpanded){
              scope.isExpanded = false;
              $("." + scope.toggleClass).hide();
              $("#" + scope.pageWrapper).css("margin-left",0);
              $(iconElement).removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-right');
              $(this).attr('title','Show');
            }else{
              scope.isExpanded = true;
              $("." + scope.toggleClass).show();
              $("#" + scope.pageWrapper).css("margin-left",250);
              $(iconElement).removeClass('glyphicon-chevron-right').addClass('glyphicon-chevron-left');
              $(this).attr('title','Hide');
            }
            // $(window).trigger('resize');
            $rootScope.$broadcast("navigation-menu:resize",true);
         });
      }
    };
  }]);
