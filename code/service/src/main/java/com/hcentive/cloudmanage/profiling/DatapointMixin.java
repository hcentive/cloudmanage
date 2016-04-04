package com.hcentive.cloudmanage.profiling;

import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface DatapointMixin {
	
	@JsonIgnore public void setUnit(StandardUnit unit);

}
