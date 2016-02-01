package com.hcentive.cloudmanage.service.provider.aws;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.resources.S3ObjectResource;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.hcentive.cloudmanage.AppConfig;
import com.hcentive.cloudmanage.domain.AWSClientProxy;

@Service("s3BucketService")
public class S3ServiceImpl implements S3Service {

	private static final Logger logger = LoggerFactory
			.getLogger(S3ServiceImpl.class.getName());

	@Autowired
	private AWSClientProxy awsClientProxy;

	@Value("${aws.bill.key}")
	private String accessKey;

	@Value("${aws.bill.secret}")
	private String secret;

	private static final int BUFFER_SIZE = 64 * 1024;

	/**
	 * Runtime retrieving the Session.
	 * 
	 * @return A Client with Session.
	 */
	public AmazonS3Client getS3Session() {
		return awsClientProxy.getS3Client();
	}

	public AmazonS3Client getBillS3Session() {
		return new AmazonS3Client(new BasicAWSCredentials(accessKey, secret));
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
		// TODO: DUMMY CODE.
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
	 * 
	 * @throws IOException
	 */
	public File getArtifact(String bucketName, String artifactName,
			String fileLocation) throws IOException {
		S3Object s3object = getS3Session().getObject(
				new GetObjectRequest(bucketName, artifactName));
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				s3object.getObjectContent()));
		String line;
		while ((line = reader.readLine()) != null) {
			// File Writer to write it off
			System.out.println(line);
		}
		// TODO: DUMMY CODE - NOT REQUIRED.
		return null;
	}

	public File getBill(String bucketName, String billFileName,
			String fileLocation) throws IOException {
		// Using default bucket name as per configuration.
		if (bucketName == null) {
			bucketName = AppConfig.billS3BucketName;
		}
		// S3 bill account is different.
		S3Object s3object = getBillS3Session().getObject(
				new GetObjectRequest(bucketName, billFileName));

		// Read the binary file
		InputStream stream = s3object.getObjectContent();

		// Write Zip
		byte[] content = new byte[BUFFER_SIZE];
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(fileLocation + File.separator
						+ billFileName));
		int totalSize = 0;
		int bytesRead;
		logger.info("Writing the file as {}{} ", fileLocation, billFileName);
		try {
			while ((bytesRead = stream.read(content)) != -1) {
				System.out.print(".");
				outputStream.write(content, 0, bytesRead);
				totalSize += bytesRead;
			}
			logger.info("Total Size of file {} bytes", totalSize);
		} finally {
			outputStream.close();
		}

		// Unzip
		ZipInputStream zis = new ZipInputStream(new FileInputStream(
				fileLocation + File.separator + billFileName));
		ZipEntry ze = zis.getNextEntry();
		content = new byte[BUFFER_SIZE];
		File newFile = null;
		try {
			while (ze != null) {
				String fileName = ze.getName();
				newFile = new File(fileLocation + File.separator + fileName);
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				logger.info("Unzipping as {} ", newFile.getAbsoluteFile());
				while ((len = zis.read(content)) > 0) {
					System.out.print(".");
					fos.write(content, 0, len);
				}
				fos.close();
				ze = zis.getNextEntry();
			}
			logger.info("Unzipped file available as {} ",
					newFile.getAbsoluteFile());
			// Delete the main file.
			File zipFile = new File(fileLocation + File.separator
					+ billFileName);
			zipFile.delete();
			logger.info("Zipped file {} is now deleted.",
					zipFile.getAbsoluteFile());
		} finally {
			zis.closeEntry();
			zis.close();
		}

		return newFile;
	}
}
