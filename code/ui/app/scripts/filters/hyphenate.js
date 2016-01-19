'use strict';

/**
 * @ngdoc filter
 * @name cloudmanageApp.filter:hyphenate
 * @function
 * @description
 * # hyphenate
 * Filter in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .filter('hyphenate', function () {
    return function (input) {
      return (input) ? input : '-';
    };
  });
