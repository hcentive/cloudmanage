package com.hcentive.cloudmanage.service;

import java.util.List;
import java.util.Map;

import com.hcentive.cloudmanage.domain.Notification;
import com.hcentive.cloudmanage.domain.ResourceMetaData;

public interface DashboardService {
	
	public Map<String, ResourceMetaData> getDashboardData();
	
	void sendNotifications();
	
}
