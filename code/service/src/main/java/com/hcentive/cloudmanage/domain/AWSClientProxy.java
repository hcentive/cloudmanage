package com.hcentive.cloudmanage.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.policy.Condition;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.EC2Actions;
import com.amazonaws.auth.policy.conditions.StringCondition;
import com.amazonaws.auth.policy.conditions.StringCondition.StringComparisonType;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.ServiceAbbreviations;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;

public class AWSClientProxy {

	private static final Logger logger = LoggerFactory
			.getLogger(AWSClientProxy.class.getName());

	// Restricting at max 2 users to get tokens using the current user.
	public ClientConfiguration getClientConfiguration() {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setMaxConnections(2);
		clientConfiguration.setProtocol(Protocol.HTTP);
		return clientConfiguration;
	}

	/**
	 * Retrieves temporary token for User from AWS.
	 * 
	 * @param awsService
	 *            - ServiceAbbreviations
	 * @return
	 */
	public AWSCredentials getSecurityToken(Regions regions, String awsService,
			boolean applyPolicy, Map<String, String> accessCondition) {
		// This client to STS will use default credentials.
		AWSSecurityTokenServiceClient sts_client = new AWSSecurityTokenServiceClient();
		// Setting the region - good practice. Optionally check if that is valid
		// FIXME: Not working
		if (isServiceSupported(regions, awsService)) {
			// sts_client.setEndpoint("s3.amazonaws.com");
		}

		// Instead of plain GetSessionTokenRequest Use AssumeRoleRequest.
		logger.info("Assume Role with a policy and get STS token");
		// Ensure the Role ARN(must) implying max access; sets the calling
		// entity as trusted.
		AssumeRoleRequest request = createRequest(applyPolicy, accessCondition);
		// Assume-Role-Response
		AssumeRoleResult assume_role_result = sts_client.assumeRole(request);
		Credentials session_creds = assume_role_result.getCredentials();

		// Handle response
		logger.debug("Credentials created " + session_creds);
		BasicSessionCredentials basic_session_creds = new BasicSessionCredentials(
				session_creds.getAccessKeyId(),
				session_creds.getSecretAccessKey(),
				session_creds.getSessionToken());
		return basic_session_creds;
	}

	/**
	 * Policy can filter down the max access this account has. As all actions
	 * are not supported - e.g. describeInstance. Its the caller's
	 * responsibility to apply aptly. Documented at
	 * 
	 * AWSEC2
	 * -APIReference-ec2-api-permissions.html#ec2-api-unsupported-resource
	 * -permissions
	 * 
	 * @param applyPolicy
	 * @param accessCondition
	 *            e.g. key: ec2:ResourceTag/Project; value: phix*
	 * @return
	 */
	private AssumeRoleRequest createRequest(boolean applyPolicy,
			Map<String, String> accessCondition) {
		AssumeRoleRequest request = new AssumeRoleRequest()
				.withRoleArn("arn:aws:iam::504357334963:role/cloudmanagerole")
				// (optional) default 12 hours.
				.withDurationSeconds(3600)
				// Easier to manage using this identifier
				.withRoleSessionName("cloudmanage");

		if (applyPolicy) {
			List<Condition> conditions = new ArrayList<>();
			for (String key : accessCondition.keySet()) {
				String valueStr = accessCondition.get(key);
				for (String value : valueStr.split(",")) {
					conditions.add(new StringCondition(
							StringComparisonType.StringLike, key, value));
				}
			}
			Statement statement = new Statement(Effect.Allow)
					.withActions(EC2Actions.StartInstances,
							EC2Actions.StopInstances)
					.withResources(
							new Resource(
									"arn:aws:ec2:us-east-1:504357334963:instance/*"));
			if (!conditions.isEmpty()) { // Should not be null
				statement.setConditions(conditions);
			}
			request.setPolicy(new Policy().withStatements(statement).toJson());
		}

		return request;
	}

	private boolean isServiceSupported(Regions regions, String awsService) {
		return Region.getRegion(regions).isServiceSupported(awsService);
	}

	// ///////////// EC2 Client //////////////////

	public AmazonEC2Client getEC2Client(boolean applyPolicy,
			Map<String, String> accessCondition) {
		return new AmazonEC2Client(getSecurityToken(Regions.US_EAST_1,
				ServiceAbbreviations.EC2, applyPolicy, accessCondition),
				getClientConfiguration());
	}

	public AmazonS3Client getS3Client() {
		return new AmazonS3Client(getSecurityToken(Regions.US_EAST_1,
				ServiceAbbreviations.S3, false, null), getClientConfiguration());
	}

	public AmazonSimpleEmailServiceClient getSESClient(boolean applyPolicy,
			Map<String, String> accessCondition) {
		return new AmazonSimpleEmailServiceClient(getSecurityToken(
				Regions.US_EAST_1, ServiceAbbreviations.Email, applyPolicy,
				accessCondition), getClientConfiguration());
	}
}
