package com.hcentive.cloudmanage.jenkins;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LastSuccessfulBuildRepository extends
		CrudRepository<LastSuccessfulBuildInfo, JobHostKey> {

	@Query("select t from LastSuccessfulBuildInfo t where t.jobHostKey.jobName = ?1")
	public List<LastSuccessfulBuildInfo> findByJobName(String jobName);

}
