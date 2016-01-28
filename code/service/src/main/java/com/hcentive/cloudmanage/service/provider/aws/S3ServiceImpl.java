package com.hcentive.cloudmanage.service.provider.aws;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.resources.S3ObjectResource;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.hcentive.cloudmanage.domain.AWSClientProxy;

@Service("s3BucketService")
public class S3ServiceImpl implements S3Service {

	private static final Logger logger = LoggerFactory
			.getLogger(S3ServiceImpl.class.getName());

	@Autowired
	private AWSClientProxy awsClientProxy;

	/**
	 * Runtime retrieving the Session.
	 * 
	 * @return A Client with Session.
	 */
	public AmazonS3Client getS3Session() {
		return awsClientProxy.getS3Client();
	}

	/**
	 * List all available buckets.
	 */
	public List<Bucket> getBucketLists() {
		logger.info("Listing buckets");
		List<Bucket> buckets = null;
		buckets = getS3Session().listBuckets();
		for (Bucket bucket : buckets) {
			logger.debug("List of Buckets - " + bucket.getName());
		}
		return buckets;
	}

	/**
	 * Read a bucket for contents.
	 */
	public List<String> getBucketList(String bucketName) {
		logger.info("Listing bucket's content for " + bucketName);
		List<String> returnContentList = new ArrayList<>();
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
				.withBucketName(bucketName);
		ObjectListing contentList = null;
		logger.debug("List of " + bucketName + "bucket's content - ");
		do {
			contentList = getS3Session().listObjects(listObjectsRequest);
			// Lookup for Summaries.
			List<S3ObjectSummary> objSummaries = contentList
					.getObjectSummaries();
			for (S3ObjectSummary objSummary : objSummaries) {
				returnContentList.add(objSummary.getKey());
				logger.debug(objSummary.getKey());
			}
			listObjectsRequest.setMarker(contentList.getNextMarker());
		} while (contentList.isTruncated());
		return returnContentList;
	}

	/**
	 * Write to a bucket.
	 */
	public void uploadToBucket(File file, String bucketName) {
		AmazonS3Client session = getS3Session();
		Policy policy = getPolicyFor(bucketName);
		session.setBucketPolicy(bucketName, policy.toJson());
		// Now an attempt to write will fail.
	}

	private Policy getPolicyFor(String bucketName) {
		Statement allowPublicReadStatement = new Statement(Effect.Allow)
				.withPrincipals(Principal.AllUsers)
				.withActions(S3Actions.GetObject)
				.withResources(new S3ObjectResource(bucketName, "*"));
		Statement allowRestrictedWriteStatement = new Statement(Effect.Allow)
				.withPrincipals(new Principal("123456789"),
						new Principal("876543210"))
				.withActions(S3Actions.PutObject)
				.withResources(new S3ObjectResource(bucketName, "*"));

		Policy policy = new Policy().withStatements(allowPublicReadStatement,
				allowRestrictedWriteStatement);
		return policy;
	}

	/**
	 * Read specific artifact from a bucket
	 */
	public File getArtifact(String artifactName) {
		return new File(artifactName);		
	}

}
