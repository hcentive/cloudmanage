// Karma configuration
// http://karma-runner.github.io/0.12/config/configuration-file.html
// Generated on 2015-08-14 using
// generator-karma 0.8.3

module.exports = function(config) {
  'use strict';

  config.set({
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,

    // base path, that will be used to resolve files and exclude
    basePath: '../',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],

    // list of files / patterns to load in the browser
    files: [
      'bower_components/angular/angular.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-cookies/angular-cookies.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-sanitize/angular-sanitize.js',
      'bower_components/angular-touch/angular-touch.js',
      "bower_components/jquery/dist/jquery.js",
      "bower_components/angular/angular.js",
      "bower_components/angular-animate/angular-animate.js",
      "bower_components/bootstrap/dist/js/bootstrap.js",
      "bower_components/bootstrap-material-design/dist/js/material.js",
      "bower_components/bootstrap-material-design/dist/js/ripples.js",
      "bower_components/angular-bootstrap/ui-bootstrap-tpls.js",
      "bower_components/angular-cookies/angular-cookies.js",
      "bower_components/angular-resource/angular-resource.js",
      "bower_components/angular-sanitize/angular-sanitize.js",
      "bower_components/angular-smart-table/dist/smart-table.js",
      "bower_components/angular-touch/angular-touch.js",
      "bower_components/json3/lib/json3.min.js",
      "bower_components/promise-tracker/promise-tracker.js",
      "bower_components/ui-router/release/angular-ui-router.js",
      "bower_components/underscore/underscore.js",
      "bower_components/angular-cron-jobs/dist/angular-cron-jobs.min.js",
      "bower_components/jquery-cron/cron/jquery-cron-min.js",
      "bower_components/ui-select/dist/select.js",
      "bower_components/datatables/media/js/jquery.dataTables.js",
      "bower_components/flot/jquery.flot.js",
      "bower_components/holderjs/holder.js",
      "bower_components/metisMenu/dist/metisMenu.js",
      "bower_components/raphael/raphael.js",
      "bower_components/mocha/mocha.js",
      "bower_components/morrisjs/morris.js",
      "bower_components/datatables-responsive/js/dataTables.responsive.js",
      "bower_components/flot.tooltip/js/jquery.flot.tooltip.js",
      "bower_components/startbootstrap-sb-admin-2/dist/js/sb-admin-2.js",
      "bower_components/highcharts/highcharts.js",
      "bower_components/highcharts/highcharts-more.js",
      "bower_components/highcharts/modules/exporting.js",
      "bower_components/angular-ui-mask/dist/mask.js",
      "bower_components/moment/moment.js",
      "bower_components/arrive/src/arrive.js",
      "bower_components/promise-tracker/promise-tracker-http-interceptor.js",
      "bower_components/highcharts/modules/drilldown.src.js",
      'app/scripts/**/*.js',
      // 'test/spec/**/*.js'
      'test/spec/controllers/audit.js'
    ],

    // list of files / patterns to exclude
    exclude: [],

    // web server port
    port: 8080,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: [
      'PhantomJS'
    ],

    // Which plugins to enable
    plugins: [
      'karma-phantomjs-launcher',
      'karma-jasmine'
    ],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    colors: true,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,

    // Uncomment the following lines if you are using grunt's server to run the tests
    // proxies: {
    //   '/': 'http://localhost:9000/'
    // },
    // URL root prevent conflicts with the site root
    // urlRoot: '_karma_'
  });
};
