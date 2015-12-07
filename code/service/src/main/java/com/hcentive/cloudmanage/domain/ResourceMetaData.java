package com.hcentive.cloudmanage.domain;

import java.util.Map;

public class ResourceMetaData {
	
	private Integer total;
	
	private Map<String, Integer> countByRegion;
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Map<String, Integer> getCountByRegion() {
		return countByRegion;
	}

	public void setCountByRegion(Map<String, Integer> countByRegion) {
		this.countByRegion = countByRegion;
	}

}
