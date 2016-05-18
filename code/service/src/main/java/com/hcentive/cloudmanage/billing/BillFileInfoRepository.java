package com.hcentive.cloudmanage.billing;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BillFileInfoRepository extends
		CrudRepository<BillFileInfo, Long> {

	@Query("select b.billFile from BillFileInfo b")
	public List<String> getBillsIngested();
}
