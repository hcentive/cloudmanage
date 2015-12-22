package com.hcentive.cloudmanage.domain;

import com.hcentive.cloudmanage.service.provider.aws.AWSUtils;

public class StartJobTriggerInfo extends JobTriggerInfo{
	
	public StartJobTriggerInfo(){
		setType(AWSUtils.START_INSTANCE_JOB_TYPE);
	}

}
