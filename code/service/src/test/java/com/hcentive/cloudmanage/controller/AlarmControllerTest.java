package com.hcentive.cloudmanage.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.hcentive.cloudmanage.ApplicationTests;
import com.hcentive.cloudmanage.domain.Alarm;
import com.hcentive.cloudmanage.service.provider.aws.CloudWatchService;
import com.hcentive.cloudmanage.utils.SpringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringUtils.class)
public class AlarmControllerTest extends ApplicationTests {

    @InjectMocks private AlarmController controller = new AlarmController();
    @Mock private CloudWatchService cloudWatchService;
    @Mock private HttpServletRequest httpServletRequest;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAlarmByNameSuccess() throws Exception{
        when(cloudWatchService.getAlarmByName(anyString())).thenReturn(getAlarm());
        Alarm alarm = controller.getAlarmByName("UnitTestAlarm");
        Assert.assertEquals(alarm.getName(),"UnitTestAlarm");
        Assert.assertEquals(alarm.getInstanceId(),"UnitTestInstanceId");
        Assert.assertEquals(alarm.getThreshold(),Double.valueOf(1.0));
        Assert.assertEquals(alarm.getFrequency(), Integer.valueOf(12));
        verify(cloudWatchService,times(1)).getAlarmByName("UnitTestAlarm");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAlarmByNameIllegalArgumentException(){
        when(cloudWatchService.getAlarmByName(anyString())).thenThrow(IllegalArgumentException.class);
        Alarm alarm = controller.getAlarmByName("UnitTestAlarm");
    }

    @Test(expected = NullPointerException.class)
    public void testGetAlarmByNameNullPointerException(){
        when(cloudWatchService.getAlarmByName(anyString())).thenThrow(NullPointerException.class);
        Alarm alarm = controller.getAlarmByName("UnitTestAlarm");
    }

    @Test(expected = Exception.class)
    public void testGetAlarmByNameException(){
        when(cloudWatchService.getAlarmByName(anyString())).thenThrow(Exception.class);
        Alarm alarm = controller.getAlarmByName("UnitTestAlarm");
    }

    @Test
    public void testGetAlarmByInstanceSuccess() throws Exception{
        when(cloudWatchService.getAlarmByInstance(anyString())).thenReturn(getAlarm());
        Alarm alarm = controller.getAlarmByInstance("UnitTestInstanceId");
        Assert.assertEquals(alarm.getName(),"UnitTestAlarm");
        Assert.assertEquals(alarm.getInstanceId(),"UnitTestInstanceId");
        Assert.assertEquals(alarm.getThreshold(),Double.valueOf(1.0));
        Assert.assertEquals(alarm.getFrequency(), Integer.valueOf(12));
        verify(cloudWatchService,times(1)).getAlarmByInstance("UnitTestInstanceId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAlarmByInstanceIllegalArgumentException(){
        when(cloudWatchService.getAlarmByInstance(anyString())).thenThrow(IllegalArgumentException.class);
        Alarm alarm = controller.getAlarmByInstance("UnitTestInstanceId");
    }

    @Test(expected = NullPointerException.class)
    public void testGetAlarmByInstanceNullPointerException(){
        when(cloudWatchService.getAlarmByInstance(anyString())).thenThrow(NullPointerException.class);
        Alarm alarm = controller.getAlarmByInstance("UnitTestInstanceId");
    }

    @Test(expected = Exception.class)
    public void testGetAlarmByInstanceException(){
        when(cloudWatchService.getAlarmByInstance(anyString())).thenThrow(Exception.class);
        Alarm alarm = controller.getAlarmByInstance("UnitTestInstanceId");
    }

    @Test
    public void testCreateOrUpdateAlarmWithJsonContentTypeSuccess() throws IOException {
        PowerMockito.mockStatic(SpringUtils.class);
        given(SpringUtils.isContentTypeJson(any(HttpServletRequest.class))).willReturn(true);
        given(SpringUtils.convertToObject(any(HttpServletRequest.class),any(Class.class)))
                .willReturn(getAlarm());
        controller.createOrUpdateAlarm(httpServletRequest,getAlarm());
    }

    @Test
    public void testCreateOrUpdateAlarmWithUrlEncodedContentTypeSuccess() throws IOException {
        PowerMockito.mockStatic(SpringUtils.class);
        given(SpringUtils.isContentTypeJson(any(HttpServletRequest.class))).willReturn(false);
        controller.createOrUpdateAlarm(httpServletRequest,getAlarm());
    }

    @Test(expected = JsonParseException.class)
    public void testCreateOrUpdateAlarmJsonParseException() throws IOException {
        PowerMockito.mockStatic(SpringUtils.class);
        given(SpringUtils.isContentTypeJson(Mockito.any(HttpServletRequest.class))).willReturn(true);
        given(SpringUtils.convertToObject(any(HttpServletRequest.class),any(Class.class)))
                .willThrow(JsonParseException.class);
        controller.createOrUpdateAlarm(httpServletRequest,getAlarm());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrUpdateAlarmIllegalArgumentException() throws IOException {
        PowerMockito.mockStatic(SpringUtils.class);
        given(SpringUtils.isContentTypeJson(any(HttpServletRequest.class))).willReturn(false);
        doThrow(IllegalArgumentException.class).when(cloudWatchService).createOrUpdateAlarm(any(Alarm.class));
        controller.createOrUpdateAlarm(httpServletRequest,getAlarm());
    }

    @Test(expected = IOException.class)
    public void testCreateOrUpdateAlarmIOException() throws IOException {
        PowerMockito.mockStatic(SpringUtils.class);
        given(SpringUtils.isContentTypeJson(any(HttpServletRequest.class))).willReturn(false);
        doThrow(IOException.class).when(cloudWatchService).createOrUpdateAlarm(any(Alarm.class));
        controller.createOrUpdateAlarm(httpServletRequest,getAlarm());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateOrUpdateAlarmNullPointerException() throws IOException {
        PowerMockito.mockStatic(SpringUtils.class);
        given(SpringUtils.isContentTypeJson(any(HttpServletRequest.class))).willReturn(false);
        doThrow(NullPointerException.class).when(cloudWatchService).createOrUpdateAlarm(any(Alarm.class));
        controller.createOrUpdateAlarm(httpServletRequest,getAlarm());
    }

    @Test
    public void testDeleteAlarmByNameSuccess(){
        controller.deleteAlarmByName("UnitTestAlarm");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAlarmByNameIllegalArgumentException(){
        doThrow(IllegalArgumentException.class).when(cloudWatchService).deleteAlarmByName("UnitTestAlarm");
        controller.deleteAlarmByName("UnitTestAlarm");
    }

    @Test
    public void testDeleteAlarmByInstanceSuccess(){
        controller.deleteAlarmByInstance("UnitTestInstanceId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAlarmByInstanceIllegalArgumentException(){
        doThrow(IllegalArgumentException.class).when(cloudWatchService).deleteAlarmByInstance("UnitTestInstanceId");
        controller.deleteAlarmByInstance("UnitTestInstanceId");
    }

    private Alarm getAlarm(){
        Alarm alarm = new Alarm();
        alarm.setName("UnitTestAlarm");
        alarm.setInstanceId("UnitTestInstanceId");
        alarm.setFrequency(12);
        alarm.setThreshold(1.0);
        return alarm;
    }
}
