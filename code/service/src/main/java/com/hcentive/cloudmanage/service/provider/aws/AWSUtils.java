package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import com.hcentive.cloudmanage.domain.Instance;

public class AWSUtils {
	
	public static final String START_INSTANCE_JOB_TYPE = "start-ec2";
	
	public static final String STOP_INSTANCE_JOB_TYPE = "stop-ec2";
	
	public static final String INSTANCE_ID = "instanceId";

	public static String CRON_EXPRESSION = "cron-expression";
	
	public static List<Instance> extractInstances(DescribeInstancesResult result) {
		List<Reservation> reservations=result.getReservations();
		List<Instance> instances= new ArrayList<Instance>();
		for (Reservation reservation : reservations) {
		    for (com.amazonaws.services.ec2.model.Instance instance : reservation.getInstances()) {
		    	Instance _instance = new Instance(instance);
		    	instances.add(_instance);
		    }
		  } 
		return instances;
	}
	
	public static Instance extractInstance(Reservation reservation){
		com.amazonaws.services.ec2.model.Instance instance =  reservation.getInstances().get(0);
		Instance lInstance = new Instance(instance);
		return lInstance;
	}
	
	

}
