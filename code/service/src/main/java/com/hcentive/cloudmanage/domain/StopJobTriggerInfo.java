package com.hcentive.cloudmanage.domain;

import com.hcentive.cloudmanage.service.provider.aws.AWSUtils;

public class StopJobTriggerInfo extends JobTriggerInfo{

	public StopJobTriggerInfo(){
		setType(AWSUtils.STOP_INSTANCE_JOB_TYPE);
	}
}
