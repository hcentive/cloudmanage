package com.hcentive.cloudmanage.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.ServiceAbbreviations;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;

public class AWSClientProxy {

	private static final Logger logger = LoggerFactory
			.getLogger(AWSClientProxy.class.getName());

	private boolean stsEnabled = true;

	/**
	 * Returns AWS Client for the service.
	 * 
	 * @param awsService
	 * @return
	 */
	public AmazonWebServiceClient getClient(String awsService) {
		AmazonWebServiceClient awsClient = null;
		switch (awsService) {
		case ServiceAbbreviations.S3:
			awsClient = getS3Client();
			break;
		case ServiceAbbreviations.EC2:
			awsClient = getEC2Client();
			break;
		default:
			throw new IllegalArgumentException("Invalid aws service: "
					+ awsService);
		}
		return awsClient;
	}

	// Restricting at max 2 users to get tokens using the current user.
	public ClientConfiguration getClientConfiguration() {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setMaxConnections(2);
		return clientConfiguration;
	}

	/**
	 * Retrieves temporary token for User from AWS.
	 * 
	 * @param awsService
	 *            - ServiceAbbreviations
	 * @return
	 */
	public AWSCredentials getSecurityToken(Regions regions, String awsService) {
		// Temporary STS creds and token
		// This client to STS will use default credentials.
		AWSSecurityTokenServiceClient sts_client = new AWSSecurityTokenServiceClient();
		// Setting the region - good practice. Optionally check if that is valid
		// FIXME: Not working
		if (isServiceSupported(regions, awsService)) {
			// sts_client.setEndpoint("s3.amazonaws.com");
		}
		GetSessionTokenRequest session_token_request = new GetSessionTokenRequest();
		// optional - good practice; default is 12 hours or 1 hour for root.
		session_token_request.setDurationSeconds(1 * 60 * 60);
		GetSessionTokenResult session_token_result = sts_client
				.getSessionToken(session_token_request);
		Credentials session_creds = session_token_result.getCredentials();
		BasicSessionCredentials basic_session_creds = new BasicSessionCredentials(
				session_creds.getAccessKeyId(),
				session_creds.getSecretAccessKey(),
				session_creds.getSessionToken());
		return basic_session_creds;
	}

	public boolean isServiceSupported(Regions regions, String awsService) {
		return Region.getRegion(regions).isServiceSupported(awsService);
	}

	// ///////////// All Clients //////////////////

	private AmazonS3Client getS3Client() {
		AmazonS3Client s3Client;
		if (stsEnabled) {
			s3Client = new AmazonS3Client(getSecurityToken(Regions.US_EAST_1,
					ServiceAbbreviations.S3), getClientConfiguration());
		} else {
			// Default - from ~/.aws/credentials.
			// new AmazonS3Client(new DefaultAWSCredentialsProviderChain());
			// OR
			// Any implementation of Credential Provider viz.
			// new AmazonS3Client(new EnvironmentVariableCredentialsProvider());
			// OR
			// Explicitly Specifying Credentials
			// BasicAWSCredentials cr = new BasicAWSCredentials(access,secret);
			// new AmazonS3Client(cr);
			s3Client = new AmazonS3Client();
		}
		return s3Client;
	}

	private AmazonEC2Client getEC2Client() {
		AmazonEC2Client ec2Client;
		if (stsEnabled) {
			ec2Client = new AmazonEC2Client(getSecurityToken(Regions.US_EAST_1,
					ServiceAbbreviations.EC2), getClientConfiguration());
		} else {
			ec2Client = new AmazonEC2Client();
		}
		return ec2Client;
	}

}
