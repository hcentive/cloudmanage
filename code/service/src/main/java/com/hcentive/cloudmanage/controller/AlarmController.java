package com.hcentive.cloudmanage.controller;

import com.hcentive.cloudmanage.domain.Alarm;
import com.hcentive.cloudmanage.service.provider.aws.CloudWatchService;
import com.hcentive.cloudmanage.utils.SpringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Api(basePath = "/alarms",
        value = "alarms",
        description = "Api endpoint related to Cloud watch alarms",
        produces = "application/json",
        tags = "alarms")
@RestController
@RequestMapping("/alarms")
public class AlarmController {

    @Autowired
    private CloudWatchService cloudWatchService;

    @ApiOperation(value = "Get cpu utilization alarm by name",
            nickname = "Get cpu utilization alarm by name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmName",value = "Alarm Name",required = true,dataType = "string",paramType = "path")}
    )
    @RequestMapping(value="/{alarmName}",method= RequestMethod.GET)
    public Alarm getAlarmByName(@PathVariable(value = "alarmName") String alarmName){
        return cloudWatchService.getAlarmByName(alarmName);
    }

    @ApiOperation(value = "Get cpu utilization alarm by instance id",
            nickname = "Get cpu utilization alarm by instance id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "instanceId",value = "Instance id",required = true,dataType = "string",paramType = "path")}
    )
    @RequestMapping(value="/instances/{instanceId}",method= RequestMethod.GET)
    public Alarm getAlarmByInstance(@PathVariable(value = "instanceId") String instanceId){
        return cloudWatchService.getAlarmByInstance(instanceId);
    }

    @ApiOperation(value = "Create / Update cpu utilization alarm on ec2 instance",
            nickname = "Create / Update cpu utilization alarm on ec2 instance")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method=RequestMethod.POST)
    public void createOrUpdateAlarm(HttpServletRequest request,Alarm alarm) throws IOException {
        if(SpringUtils.isContentTypeJson(request)){
            alarm = SpringUtils.convertToObject(request,Alarm.class);
        }
        cloudWatchService.createOrUpdateAlarm(alarm);
    }
}
