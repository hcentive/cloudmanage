package com.hcentive.cloudmanage.service.provider.aws;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;

//@Service
public class MockAWSClientServiceImpl implements AWSClientService{
	
	@Value("${mock.endpoint}")
	private String endpoint = "http://localhost:8000/aws-mock/ec2-endpoint/";

	@Override
	public AmazonEC2Client getClient() {
		AmazonEC2Client amazonClient = new AmazonEC2Client(new BasicAWSCredentials("foo", "bar"));
		amazonClient.setEndpoint(endpoint);
		return amazonClient;
	}
	
	@PostConstruct
	public void init(){
		String imageId = "ami-12345678";
        final int runCount = 10;
        runInstances(imageId, runCount);
	}
	
    public List<Instance> runInstances(final String imageId, final int runCount) {
        AmazonEC2Client amazonEC2Client = getClient();
        String instanceType = "m1.large";
        RunInstancesRequest request = new RunInstancesRequest();
        final int minRunCount = runCount;
        final int maxRunCount = runCount;
        request.withImageId(imageId).withInstanceType(instanceType)
                .withMinCount(minRunCount).withMaxCount(maxRunCount);
        RunInstancesResult result = amazonEC2Client.runInstances(request);
        return result.getReservation().getInstances();
    }

}
