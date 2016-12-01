package com.hcentive.cloudmanage.service.provider.aws;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsRequest;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsResult;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricAlarm;
import com.amazonaws.services.ec2.model.Tag;
import com.hcentive.cloudmanage.ApplicationTests;
import com.hcentive.cloudmanage.audit.AuditContext;
import com.hcentive.cloudmanage.audit.AuditContextHolder;
import com.hcentive.cloudmanage.audit.AuditService;
import com.hcentive.cloudmanage.domain.AWSClientProxy;
import com.hcentive.cloudmanage.domain.Alarm;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.validation.AlarmValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

public class CloudWatchServiceImplTest extends ApplicationTests{

    @InjectMocks private CloudWatchService cloudWatchService = new CloudWatchServiceImpl();
    @Mock private AmazonCloudWatchClient cloudWatchClient;
    @Mock private AWSClientProxy awsClientProxy;
    @Spy private AlarmValidator alarmValidator;
    @Mock private EC2Service ec2Service;
    @Mock private AuditService auditService;
    @Mock private com.amazonaws.services.ec2.model.Instance awsInstance;
    @Mock private Instance instance;
    @Mock private DescribeAlarmsResult describeAlarmsResult;
    @Mock private Dimension dimension;
    @Mock private MetricAlarm metricAlarm;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(awsClientProxy.getCloudWatchClient(Mockito.anyBoolean())).thenReturn(cloudWatchClient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAlarmByNameNull(){
        Alarm alarm = cloudWatchService.getAlarmByName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAlarmByInstanceIdNull(){
        Alarm alarm = cloudWatchService.getAlarmByInstance(null);
    }

    @Test
    public void testGetAlarmByNameSuccess(){
        Mockito.when(cloudWatchClient.describeAlarms(Mockito.any(DescribeAlarmsRequest.class)))
                .thenReturn(describeAlarmsResult());
        Alarm alarm = cloudWatchService.getAlarmByName("UnitTestAlarm");
        Assert.assertNotNull(alarm);
        Assert.assertEquals(alarm.getName(),"UnitTestAlarm");
        Assert.assertEquals(alarm.getInstanceId(),"UnitTestInstanceId");
        Assert.assertEquals(alarm.getFrequency(),Integer.valueOf(12));
        Assert.assertEquals(alarm.getThreshold(),Double.valueOf(1.0));
        Assert.assertEquals(alarm.isAlarmConfigured(),Boolean.valueOf(true));
        Assert.assertEquals(alarm.isEnable(),Boolean.valueOf(true));
    }

    @Test
    public void testGetAlarmByInstanceSuccess(){
        DescribeAlarmsResult result = describeAlarmsResult();
        result.getMetricAlarms().get(0).setAlarmName("cpu-utilization-check-InstanceId");
        Mockito.when(cloudWatchClient.describeAlarms(Mockito.any(DescribeAlarmsRequest.class)))
                .thenReturn(result);
        Alarm alarm = cloudWatchService.getAlarmByInstance("InstanceId");
        Assert.assertNotNull(alarm);
        Assert.assertEquals(alarm.getName(),"cpu-utilization-check-InstanceId");
        Assert.assertEquals(alarm.getInstanceId(),"UnitTestInstanceId");
        Assert.assertEquals(alarm.getFrequency(),Integer.valueOf(12));
        Assert.assertEquals(alarm.getThreshold(),Double.valueOf(1.0));
        Assert.assertEquals(alarm.isAlarmConfigured(),Boolean.valueOf(true));
        Assert.assertEquals(alarm.isEnable(),Boolean.valueOf(true));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateOrUpdateAlarmAuditNameNull(){
        Alarm alarm = getAlarm();
        AuditContextHolder.setContext(null);
        cloudWatchService.createOrUpdateAlarm(alarm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrUpdateAlarmInstanceIdNull(){
        Alarm alarm = getAlarm();
        alarm.setInstanceId(null);
        auditContextHolder();
        cloudWatchService.createOrUpdateAlarm(alarm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrUpdateAlarmFrequencyNull(){
        Alarm alarm = getAlarm();
        alarm.setFrequency(null);
        auditContextHolder();
        cloudWatchService.createOrUpdateAlarm(alarm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrUpdateAlarmFrequencyOutOfRange(){
        Alarm alarm = getAlarm();
        alarm.setFrequency(2);
        auditContextHolder();
        cloudWatchService.createOrUpdateAlarm(alarm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrUpdateAlarmThresholdNull(){
        Alarm alarm = getAlarm();
        alarm.setThreshold(null);
        auditContextHolder();
        cloudWatchService.createOrUpdateAlarm(alarm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrUpdateAlarmThresholdOutOfRange(){
        Alarm alarm = getAlarm();
        alarm.setThreshold(10.0);
        auditContextHolder();
        cloudWatchService.createOrUpdateAlarm(alarm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrUpdateAlarmNotQaDevStack(){
        Mockito.when(ec2Service.getInstanceForJob(Mockito.any(String.class),Mockito.anyBoolean())).thenReturn(getInstance());
        Mockito.when(ec2Service.isTagPresent(Mockito.any(Instance.class),Mockito.anyString(),Mockito.anySet())).thenReturn(false);
        auditContextHolder();
        cloudWatchService.createOrUpdateAlarm(getAlarm());
    }

    @Test
    public void testCreateOrUpdateAlarmSuccess(){
        Mockito.when(ec2Service.getInstanceForJob(Mockito.any(String.class),Mockito.anyBoolean())).thenReturn(getInstance());
        Mockito.when(ec2Service.isTagPresent(Mockito.any(Instance.class),Mockito.anyString(),Mockito.anySet())).thenReturn(true);
        auditContextHolder();
        cloudWatchService.createOrUpdateAlarm(getAlarm());
    }

    private DescribeAlarmsResult describeAlarmsResult(){
        List<MetricAlarm> metricAlarms = new ArrayList<>();
        List<Dimension> dimensions = new ArrayList<>();
        metricAlarm = new MetricAlarm();
        dimension = new Dimension();
        describeAlarmsResult = new DescribeAlarmsResult();

        dimensions.add(dimension.withName("InstanceId").withValue("UnitTestInstanceId"));
        metricAlarm.withThreshold(1.0).withPeriod(10840).withActionsEnabled(true)
                .withAlarmName("UnitTestAlarm").withEvaluationPeriods(4);
        metricAlarm.withDimensions(dimensions);
        metricAlarms.add(metricAlarm);
        describeAlarmsResult.withMetricAlarms(metricAlarms);
        return describeAlarmsResult;
    }

    private Alarm getAlarm(){
        Alarm alarm = new Alarm();
        alarm.setName("UnitTestAlarm");
        alarm.setInstanceId("UnitTestInstanceId");
        alarm.setFrequency(12);
        alarm.setThreshold(1.0);
        return alarm;
    }

    private void auditContextHolder(){
        AuditContext auditContext = new AuditContext();
        auditContext.setInitiator("UnitTestInitiator");
        AuditContextHolder.setContext(auditContext);
    }

    public Instance getInstance(){
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Stack","qa"));
        awsInstance = new com.amazonaws.services.ec2.model.Instance();
        awsInstance.setTags(tags);
        return new Instance(awsInstance);
    }
}
