package com.hcentive.cloudmanage.service.provider.zeus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcentive.cloudmanage.AppConfig;

@Service("zeusService")
public class ZeusLBClient {

	private static final Logger logger = LoggerFactory
			.getLogger(ZeusLBClient.class.getName());

	private BasicAuthRestTemplate restTemplate;

	private static final String virtualServerURI = "virtual_servers";
	private static final String poolURI = "pools";
	private static final String ruleURI = "rules";
	private static final String trafficGroupURI = "traffic_ip_groups";

	public ZeusLBClient() {
		super();
		restTemplate = new BasicAuthRestTemplate(AppConfig.zeusUsername,
				AppConfig.zeusPassword);
	}

	public Map<String, List<String>> getLBMappings() {
		// Get All Virtual Servers
		List<String> virtualServers = getAllVirtualServers();
		Map<String, List<String>> vsConfig = new HashMap<String, List<String>>();
		// Get All Pools
		List<String> pools = getAllPools();
		// Skip the pools which are not configured to any virtual server.
		for (String virtualServer : virtualServers) {
			Map<String, String> configForVS = getConfigForVS(virtualServer);
			// Internal Endpoint
			String poolAttached = configForVS.get("pool");
			String rulesAttached = configForVS.get("requestRules");
			// External Endpoint
			String tgAttached = configForVS.get("trafficIpGroups");
			// For each external ip endpoints multi internal ip endpoints exists
			if (tgAttached != null) {
				// Loop through each traffic group(s)
				for (String tg : tgAttached.split(",")) {
					// Loop through each traffic group's Ip(s)
					String[] ipList = getConfigForTrafficGroup(tg).get("ips")
							.split(",");
					for (String ip : ipList) {
						// getPools from rules
						Set<String> poolsFromRules = new HashSet<String>();
						if (rulesAttached != null) {
							for (String ruleAttached : rulesAttached.split(",")) {
								poolsFromRules.addAll(getConfigForRule(
										ruleAttached).get("pools"));
							}
						}
						logger.info("Pools gathered from rules {}",
								poolsFromRules);
						// merge list of pools
						Set<String> poolsAttached = new HashSet<String>();
						poolsAttached.add(poolAttached);
						poolsAttached.addAll(poolsFromRules);
						// Loop thrgouh each pool
						for (String pool : poolsAttached) {
							// Pools internal Ip(s) need to be matched.
							if (pools.contains(pool)) {
								List<String> nodes = getConfigForPool(pool)
										.get("nodes");
								// Add if already exists
								if (vsConfig.containsKey(ip)) {
									// Simple way to remove duplicates
									vsConfig.get(ip).removeAll(nodes);
									vsConfig.get(ip).addAll(nodes);
								} else {
									vsConfig.put(ip, nodes);
								}
							}
						}
					}
				}
			}
		}
		logger.info("Virtual Server:Node IPs => {}", vsConfig);
		return vsConfig;
	}

	private Map<String, Set<String>> getConfigForRule(String ruleName) {
		Map<String, Set<String>> ruleConfig = new HashMap<String, Set<String>>();
		logger.info("Retrieving rule-config from {}", AppConfig.zeusUrl + "/"
				+ ruleURI + "/" + ruleName);
		String ruleText = restTemplate.getForObject(AppConfig.zeusUrl + "/"
				+ ruleURI + "/" + ruleName, String.class);
		Set<String> poolNames = new HashSet<String>();
		Pattern p = Pattern.compile("pool\\.use\\( \"(.*)\" \\);");
		Matcher m = p.matcher(ruleText);
		if (m.find()) {
			for (int i = 0; i < m.groupCount(); i++)
				poolNames.add(m.group(1));
		}
		ruleConfig.put("pools", poolNames);
		logger.info("Retrieved {} rule-config as {}", ruleName, ruleConfig);
		return ruleConfig;
	}

	private Map<String, List<String>> getConfigForPool(String poolName) {
		Map<String, List<String>> poolConfig = new HashMap<String, List<String>>();
		logger.info("Retrieving pool-config from {}", AppConfig.zeusUrl + "/"
				+ poolURI + "/" + poolName);
		ObjectNode nodeList = restTemplate.getForObject(AppConfig.zeusUrl + "/"
				+ poolURI + "/" + poolName, ObjectNode.class);
		JsonNode nodes = nodeList.get("properties").get("basic")
				.get("nodes_table");
		List<String> nodeNames = new ArrayList<String>();
		for (Iterator<JsonNode> node = nodes.iterator(); node.hasNext();) {
			String nodeName = node.next().get("node").asText();
			if (nodeName.indexOf(":") != -1) {
				nodeName = nodeName.split(":")[0];
			}
			nodeNames.add(nodeName);
		}
		poolConfig.put("nodes", nodeNames);
		logger.info("Retrieved {} pool-config as {}", poolName, poolConfig);
		return poolConfig;
	}

	private Map<String, String> getConfigForTrafficGroup(String trafficGroup) {
		Map<String, String> tgConfig = new HashMap<String, String>();
		logger.info("Retrieved tg-config from {}", AppConfig.zeusUrl + "/"
				+ trafficGroupURI + "/" + trafficGroup);
		ObjectNode trafficGrp = restTemplate.getForObject(AppConfig.zeusUrl
				+ "/" + trafficGroupURI + "/" + trafficGroup, ObjectNode.class);
		JsonNode ipArray = trafficGrp.get("properties").get("basic")
				.get("ipaddresses");
		StringBuilder strBld = new StringBuilder();
		if (ipArray.isArray()) {
			for (JsonNode ip : ipArray) {
				strBld.append(",").append(ip.asText());
			}
		}
		tgConfig.put("ips", strBld.substring(1));
		logger.info("Retrieved {} tg-config as {}", trafficGroup, tgConfig);
		return tgConfig;
	}

	/**
	 * Can return any list of parameters. Extensible design with Map<property
	 * name, property value>
	 * 
	 * @param virtualServer
	 * @return
	 */
	private Map<String, String> getConfigForVS(String virtualServer) {
		Map<String, String> vsConfig = new HashMap<String, String>();
		logger.info("Retrieving vs-config from {}", AppConfig.zeusUrl + "/"
				+ virtualServerURI + "/" + virtualServer);
		ObjectNode virtualServerList = restTemplate.getForObject(
				AppConfig.zeusUrl + "/" + virtualServerURI + "/"
						+ virtualServer, ObjectNode.class);
		// Set the pool
		String poolName = virtualServerList.get("properties").get("basic")
				.get("pool").asText();
		vsConfig.put("pool", poolName);
		// Set the traffic ips
		JsonNode trafficIpGroups = virtualServerList.get("properties")
				.get("basic").get("listen_on_traffic_ips");
		String tgName = null;
		if (trafficIpGroups.isArray()) {
			StringBuilder strBld = new StringBuilder();
			for (JsonNode trafficIpGrp : trafficIpGroups) {
				strBld.append(",").append(trafficIpGrp.asText());
			}
			if (strBld.length() != 0) {
				tgName = strBld.substring(1);
			}
		}
		vsConfig.put("trafficIpGroups", tgName);
		// Set the request rules
		JsonNode requestRules = virtualServerList.get("properties")
				.get("basic").get("request_rules");
		String rule = null;
		if (requestRules.isArray()) {
			StringBuilder strBld = new StringBuilder();
			for (JsonNode requestRule : requestRules) {
				strBld.append(",").append(requestRule.asText());
			}
			if (strBld.length() != 0) {
				rule = strBld.substring(1);
			}
		}
		vsConfig.put("requestRules", rule);
		logger.info("Retrieved {} vs-config as {}", virtualServer, vsConfig);
		return vsConfig;
	}

	/**
	 * Returns list of virtual servers as JSON with name and href. "name":
	 * "PHIX_PRODUCT_QA_DOMAIN:443", "href":
	 * "/api/tm/3.4/config/active/virtual_servers/PHIX_PRODUCT_QA_DOMAIN:443"
	 * 
	 * @return
	 */
	private List<String> getAllVirtualServers() {
		List<String> virtualServers = new ArrayList<String>();
		logger.info("Retrieving Virtual Servers from {}", AppConfig.zeusUrl
				+ "/" + virtualServerURI);
		ObjectNode virtualServerList = restTemplate.getForObject(
				AppConfig.zeusUrl + "/" + virtualServerURI, ObjectNode.class);
		JsonNode children = virtualServerList.get("children");
		for (Iterator<JsonNode> vs = children.iterator(); vs.hasNext();) {
			virtualServers.add(vs.next().get("name").asText());
		}
		logger.info("Retrieved Virtual Servers as {}", virtualServers);
		return virtualServers;
	}

	/**
	 * Returns list of pools as JSON with name and href. "name":
	 * "CP cwpowellins-uat-individual", "href":
	 * "/api/tm/3.4/config/active/pools/CP%20cwpowellins-uat-individual"
	 * 
	 * @return
	 */
	private List<String> getAllPools() {
		List<String> pools = new ArrayList<String>();
		logger.info("Retrieving Pools from {}", AppConfig.zeusUrl + "/"
				+ poolURI);
		ObjectNode poolList = restTemplate.getForObject(AppConfig.zeusUrl + "/"
				+ poolURI, ObjectNode.class);
		JsonNode children = poolList.get("children");
		for (Iterator<JsonNode> vs = children.iterator(); vs.hasNext();) {
			pools.add(vs.next().get("name").asText());
		}
		logger.info("Retrieved Pools as {}", pools);
		return pools;
	}
}
