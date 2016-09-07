package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest;
import com.amazonaws.services.route53.model.ListResourceRecordSetsResult;
import com.amazonaws.services.route53.model.ResourceRecord;
import com.amazonaws.services.route53.model.ResourceRecordSet;
import com.hcentive.cloudmanage.domain.AWSClientProxy;
import com.hcentive.cloudmanage.service.provider.zeus.ZeusLBClient;

@Service("dNSService")
public class DNSServiceImpl implements DNSService {

	private static final Logger logger = LoggerFactory
			.getLogger(DNSServiceImpl.class.getName());

	@Autowired
	private AWSClientProxy awsClientProxy;

	@Autowired
	private ZeusLBClient zeusLBClient;

	public Map<String, String> getDNS() {
		Map<String, String> dnsMap = new HashMap<String, String>();

		logger.info("Get DNS list from Route 53");
		Map<String, String> rt53Map = getRoute53Records();
		logger.info("Get mappings from Load Balancer"); // IP:dns
		Map<String, List<String>> lbMappings = zeusLBClient.getLBMappings();

		// Lets merge
		for (String externalIp : rt53Map.keySet()) {
			String dnsName = rt53Map.get(externalIp);
			if (dnsName.endsWith(".")) {
				dnsName = dnsName.substring(0, dnsName.length() - 1);
			}
			if (lbMappings.containsKey(externalIp)) {
				for (String internalIp : lbMappings.get(externalIp)) {
					dnsMap.put(internalIp, dnsName);
				}
			}
		}
		logger.info("Combined DNS list {}", dnsMap);
		return dnsMap;
	}

	public Map<String, String> getRoute53Records() {
		Map<String, String> dnsMap = new HashMap<String, String>();
		AmazonRoute53Client dnsClient = awsClientProxy.getRoute53Client();
		List<String> hostedZoneIds = new ArrayList<String>();
		hostedZoneIds.add("Z3VCU1E6URLDQ8"); // hcinternal.com. - Public
		hostedZoneIds.add("Z3GL9C2XDX85XY"); // hcinternal.net. - Public
		hostedZoneIds.add("Z39LU1HC3ZZWBM"); // hcentive.com. - Public
		hostedZoneIds.add("Z1S8IBGJGDP6J7"); // hcinternal.net. - Private
		for (String hostedZoneId : hostedZoneIds) {
			ListResourceRecordSetsRequest request = new ListResourceRecordSetsRequest()
					.withHostedZoneId(hostedZoneId);
			ListResourceRecordSetsResult result = dnsClient
					.listResourceRecordSets(request);
			List<ResourceRecordSet> recordSets = result.getResourceRecordSets();
			int counter = 0;
			int totalCount = recordSets.size();
			for (ResourceRecordSet recordSet : recordSets) {
				logger.debug("{} Resource Record Set {}: {} out of {}",
						hostedZoneId, recordSet.toString(), counter++,
						totalCount);
				if (recordSet.getType().equals("A")) {
					List<ResourceRecord> resourceRecords = recordSet
							.getResourceRecords();
					for (ResourceRecord resourceRecord : resourceRecords) {
						dnsMap.put(resourceRecord.getValue(),
								recordSet.getName());
					}
				}
			}
		}
		logger.info("Retrieved DNS list from Rt53 {}", dnsMap);
		return dnsMap;
	}
}