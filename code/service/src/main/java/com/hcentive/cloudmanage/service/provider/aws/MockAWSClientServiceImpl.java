package com.hcentive.cloudmanage.service.provider.aws;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;

@Service
@Profile("dev")
public class MockAWSClientServiceImpl implements AWSClientService{
	
	@Value("${mock.endpoint}")
	private String endpoint;

	@Override
	public AmazonEC2Client getClient() {
		AmazonEC2Client amazonClient = new AmazonEC2Client(new BasicAWSCredentials("foo", "bar"));
		amazonClient.setEndpoint(endpoint);
		return amazonClient;
	}
	
	/**
     * Run some new ec2 instances.
     *
     * @param imageId
     *            AMI for running new instances from
     * @param runCount
     *            count of instances to run
     * @return a list of started instances
     */
    public static List<Instance> runInstances(final String imageId, final int runCount) {
        // pass any credentials as aws-mock does not authenticate them at all
        AWSCredentials credentials = new BasicAWSCredentials("foo", "bar");
        AmazonEC2Client amazonEC2Client = new AmazonEC2Client(credentials);

        // the mock endpoint for ec2 which runs on your computer
        String ec2Endpoint = "http://localhost:8000/aws-mock/ec2-endpoint/";
        amazonEC2Client.setEndpoint(ec2Endpoint);

        // instance type
        String instanceType = "m1.large";

        // run 10 instances
        RunInstancesRequest request = new RunInstancesRequest();
        final int minRunCount = runCount;
        final int maxRunCount = runCount;
        request.withImageId(imageId).withInstanceType(instanceType)
                .withMinCount(minRunCount).withMaxCount(maxRunCount);
        RunInstancesResult result = amazonEC2Client.runInstances(request);

        return result.getReservation().getInstances();
    }


    /**
     * Main method for command line use.
     *
     * @param args
     *            parameters from command line (no need here)
     */
    public static void main(final String[] args) {
        /*-
         * use one of the pre-defined amis
         * (all amis are pre-defined in aws-mock-default.properties or aws-mock.properties)
         */
        String imageId = "ami-12345678";
        final int runCount = 10;

        List<Instance> startedInstances = runInstances(imageId, runCount);

        System.out.println("Started instances: ");

        for (Instance i : startedInstances) {
            System.out.println(i.getInstanceId() + " - " + i.getState().getName());
        }
    }

}
