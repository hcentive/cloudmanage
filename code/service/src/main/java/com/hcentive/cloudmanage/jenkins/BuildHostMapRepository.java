package com.hcentive.cloudmanage.jenkins;

import org.springframework.data.repository.CrudRepository;

public interface BuildHostMapRepository extends
		CrudRepository<BuildMetaInfo, Long> {

	public BuildMetaInfo findByJobName(String jobName);
}
