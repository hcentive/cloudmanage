'use strict';

/**
 * @ngdoc factory
 * @name cloudmanageApp.billing
 * @description
 * # billing factory
 * Factory in the cloudmanageApp.
 */

angular.module('cloudmanageApp')
 .factory('billingFactory', [function () {
 	var billingFactory = {},
 		_uniqueStackMap = {},
      	stackedDataByClient = {};

	function addBill(first,second){
	    var sum = (first + second).toFixed(3);
	    return parseFloat(sum);
  	}

  	function uniqueStack(stack){
	    if(!_uniqueStackMap.hasOwnProperty(stack) && stack !== null){
	      _uniqueStackMap[stack] = stack;
	    }
	}

	function billingDataByTime(billingByTime,date,amount){

		if(billingByTime.hasOwnProperty(date)){
			billingByTime[date] = addBill(billingByTime[date],amount);
		}else{
			billingByTime[date] = amount;
		}
	}

  	billingFactory.defaultClientSelection = function(billingDataByClient){
  		var clientCount = 0,
  			selectedClients = [],
  			isChecked = false;

  		for(var client in billingDataByClient){
  			if(billingDataByClient.hasOwnProperty(client)){
	  			if(clientCount > 4){
		         isChecked = false;
		        }else{
		          isChecked = true; 
		        }

		        if(client === "Unknown" || client === ""){
		        	isChecked = false;
		        }else{
		        	clientCount++;
		        }

				selectedClients.push({
					"name":client,
					"isChecked": isChecked ? true : false
				});
  			}
  		}
  		return selectedClients;
  	}

 	billingFactory.parseBillingData = function (data){
	    var mapDataByStack = {},
	      	clientName,stackData,stack;

	    stackedDataByClient = {};
	    _uniqueStackMap = {};
	    _.each(data,function(billingInfo){

	      // Map data by client
	      clientName = billingFactory.getClientName(billingInfo);
	      
	      if(stackedDataByClient.hasOwnProperty(clientName)){
	        // client exists
	        stackData = billingFactory.getStackData(stackedDataByClient[clientName],billingInfo);

	      }else{
	        // client not exists
	        stackData = billingFactory.getStackData({},billingInfo);
	      }
	      stackedDataByClient[clientName] = stackData;
	    });
	    return stackedDataByClient;
	}

	billingFactory.getClientName = function(billingInfo){
	    var client = billingInfo.instanceInfo.client;
	    if(client === null){
	      return "Unknown";
	    }
	    return client;
	}

	billingFactory.getStackData = function(stackObj, billingInfo){
	    var stack = billingInfo.instanceInfo.stack,
	      billingCount = billingInfo.dayTotal,
	      output = {};

	      uniqueStack(stack);
	      if(stackObj.hasOwnProperty(stack)){
	        // stack exists 
	        // add billing cost into
	        stackObj[stack] = addBill(stackObj[stack],billingCount);
	      }else{
	        // stack not exists
	        stackObj[stack] = billingInfo.dayTotal;
	      }
	      return stackObj;
  	}
  	
  	billingFactory.filterSelectedClient = function(clients,data){
	    var selectedClients = {};

	    if(data === undefined || data === null){
	    	data = stackedDataByClient
	    }
	    _.each(clients,function(client){
	        if(client.isChecked){
	          selectedClients[client.name] = data[client.name];
	        }
	    });
	    return selectedClients;
 	}

 	billingFactory.trendBy = function(fromDate,toDate){
      var duration = moment.duration(moment(toDate).diff(moment(fromDate)));

      if(parseInt(duration.asMonths()) > 0){
      	return 'YYYYMM';
      } 
      return 'YYYYMMDD';
    }
    
    billingFactory.parseBillingTrendData = function(billingData,trendBy){
    	var billingDataByClient = {},
    		billingByTime,snapshot,client;

    	_.each(billingData,function(eachRow){
    		snapshot = parseInt(moment(eachRow.snapshotAt).format(trendBy));
    		client = billingFactory.getClientName(eachRow);

    		if(billingDataByClient.hasOwnProperty(client)){
    			billingDataByTime(billingDataByClient[client],snapshot,eachRow.dayTotal);
    		}else{
    			billingByTime = {};
    			billingByTime[snapshot] = eachRow.dayTotal;
    			billingDataByClient[client] = billingByTime;
    		}
    	});
    	return billingDataByClient;
    }

 	return billingFactory;
 }]);
