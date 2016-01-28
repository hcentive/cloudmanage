package com.hcentive.cloudmanage.service.provider.aws;

import java.io.File;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.amazonaws.services.s3.model.Bucket;

public interface S3Service {

	/*
	 * @PreAuthorize("hasRole('XYZ')") is the same as
	 * @PreAuthorize("hasAuthority('ROLE_XYZ')")
	 */
	@PreAuthorize("hasAuthority('techops')")
	public List<Bucket> getBucketLists();

	// Read a bucket contents
	@PreAuthorize("hasAuthority('techops')")
	public List<String> getBucketList(String bucketName);
	
	// Write to a bucket contents
	@PreAuthorize("hasAuthority('techops')")
	public void uploadToBucket(File file, String bucketName);

	public File getArtifact(String artifactName);
	
}
