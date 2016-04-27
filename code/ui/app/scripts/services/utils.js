'use strict';

/**
 * @ngdoc service
 * @name cloudmanageApp.utils
 * @description
 * # utils
 * Factory in the cloudmanageApp.
 */
angular.module('cloudmanageApp')
  .factory('utils', function () {
   
    return {
       //inspired from http://stackoverflow.com/questions/5900840/post-nested-object-to-spring-mvc-controller-using-json
        serializeJSON : (function() {
            // copy from jquery.js
            var r20 = /%20/g,
                rbracket = /\[\]$/,
                serializeJSON = function(a) {
                    var s = [],
                        add = function(key, value) {
                            // If value is a function, invoke it and return its value
                            value = _.isFunction(value) ? value() : value;
                            s[s.length] = encodeURIComponent(key) + "=" + encodeURIComponent(value);
                        };

                    for (var prefix in a) {
                        buildParams(prefix, a[prefix], add);
                    }

                    // Return the resulting serialization
                    return s.join("&").replace(r20, "+");
                }
                /* private method*/
            function buildParams(prefix, obj, add) {
                if (_.isArray(obj)) {
                    // Serialize array item.
                    _.each(obj, function(v, i) {
                        if (rbracket.test(prefix)) {
                            // Treat each array item as a scalar.
                            add(prefix, v);

                        } else {
                            buildParams(prefix + "[" + (typeof v === "object" || _.isArray(v) ? i : "") + "]", v, add);
                        }
                    });

                } else if (obj != null && typeof obj === "object") {
                    // Serialize object item.
                    for (var name in obj) {
                        buildParams(prefix + "." + name, obj[name], add);
                    }

                } else {
                    // Serialize scalar item.
                    add(prefix, obj);
                }
            };
            return serializeJSON;
        })(),
        getStartOfDayTimeStamp: function(timestamp){
            return +moment.utc(timestamp).startOf('day').valueOf();
        },
        uptoDecimals: function(n, decimals){
            var t = Math.pow(10,decimals);
            return Math.floor(n*t)/t;
        },
        average: function(){
            var input = arguments,
            size = input.length,
            sum = _.reduce(input, function(memo, num){ return memo + num; }, 0);
            return sum/size;
        }
    };
  });
