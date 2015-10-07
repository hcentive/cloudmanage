package com.hcentive.cloudmanage.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.domain.Notification;

@Service
public class NotificationServiceImpl implements NotificationService{

	@Override
	public List<Notification> getNotifications() {
		List<Notification> notifications = new ArrayList<Notification>();
		for(int i = 0; i< 10; i++){
			Notification notification = new Notification();
			notification.setMessage(String.valueOf(RandomUtils.nextDouble()));
			notifications.add(notification);
		}
		return notifications;
	}

}
