package com.hcentive.cloudmanage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.domain.Notification;
import com.hcentive.cloudmanage.domain.ResourceMetaData;
import com.hcentive.cloudmanage.service.provider.InstanceService;

//@Service
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired
	private InstanceService instanceService;
	
	@Autowired
	private MessageSendingOperations<String> messagingTemplate;
	
	@Autowired
	private NotificationService notificationService;

	@Override
	public Map<String, ResourceMetaData> getDashboardData() {
		Map<String, ResourceMetaData> resourceMetaDataMap = new HashMap<String, ResourceMetaData>();
		resourceMetaDataMap.put("instances", instanceService.getInstancesMetaData());
		return resourceMetaDataMap;
	}

	@Override
	@Scheduled(fixedDelay=60000)
	public void sendNotifications() {
		List<Notification> notifications = notificationService.getNotifications();
		this.messagingTemplate.convertAndSend("/topic/notifications", notifications);
	}

	
	

}
