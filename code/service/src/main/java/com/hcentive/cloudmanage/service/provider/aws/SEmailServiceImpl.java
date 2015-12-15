package com.hcentive.cloudmanage.service.provider.aws;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.hcentive.cloudmanage.audit.AuditEntity;
import com.hcentive.cloudmanage.domain.AWSClientProxy;

@Service("sEmailService")
public class SEmailServiceImpl implements SEmailService {

	private static final Logger logger = LoggerFactory
			.getLogger(SEmailServiceImpl.class.getName());

	private static final String FROM = "cloudmanange@hcentive.com";

	@Autowired
	private AWSClientProxy awsClientProxy;

	private boolean emailEnabledFlag = false;

	private AmazonSimpleEmailService getEC2Session(boolean applyPolicy) {
		Map<String, String> decisionMapAsPolicy = null;
		return awsClientProxy.getSESClient(applyPolicy, decisionMapAsPolicy);
	}

	@Override
	public void notifyViaEmail(AuditEntity audit) {
		if (emailEnabledFlag) {
			Destination destination = new Destination()
					.withToAddresses(new String[] { audit.getUserName()
							+ "@hcentive.com" });

			Content subject = new Content()
					.withData("CloudManage Audit Information!");
			Content textBody = new Content().withData(audit.toString());
			Body body = new Body().withText(textBody);
			Message message = new Message().withSubject(subject).withBody(body);

			SendEmailRequest request = new SendEmailRequest().withSource(FROM)
					.withDestination(destination).withMessage(message);

			SendEmailResult emailResult = getEC2Session(false).sendEmail(
					request);
			logger.info("Email Sent response " + emailResult);
		} else {
			logger.info("Email is not being sent as the feature has been disabled for aws SES account");
		}
	}

}
