package com.hcentive.cloudmanage.utils;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.hcentive.cloudmanage.domain.Alarm;
import com.hcentive.cloudmanage.domain.Instance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SpringUtilsTest{

    @Mock private HttpServletRequest httpServletRequest;
    @Mock private BindingResult bindingResult;
    @Mock private FieldError fieldError;
    @Mock private ObjectError objectError;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsContentTypeJsonTrue(){
        when(httpServletRequest.getContentType()).thenReturn("application/json");
        assertEquals(SpringUtils.isContentTypeJson(httpServletRequest),true);
    }

    @Test
    public void testIsContentTypeJsonFalse(){
        when(httpServletRequest.getContentType()).thenReturn("application/x-www-form-urlencoded");
        assertEquals(SpringUtils.isContentTypeJson(httpServletRequest),false);
    }

    @Test(expected = NullPointerException.class)
    public void testIsContentTypeJsonNullPointerException(){
        SpringUtils.isContentTypeJson(httpServletRequest);
    }

    @Test
    public void testBindingResultErrorMsgFieldError(){
        List<ObjectError> errors = new ArrayList<>();
        errors.add(fieldError);
        when(bindingResult.getAllErrors()).thenReturn(errors);
        when(fieldError.getCode()).thenReturn("Error occur in validating field");
        assertEquals("Error occur in validating field,",SpringUtils.bindingResultErrorMsg(bindingResult));
    }

    @Test
    public void testBindingResultErrorMsgWithObjectError(){
        List<ObjectError> errors = new ArrayList<>();
        errors.add(objectError);
        when(bindingResult.getAllErrors()).thenReturn(errors);
        when(objectError.getCode()).thenReturn("Error occur in validating field");
        assertEquals("",SpringUtils.bindingResultErrorMsg(bindingResult));
    }

    @Test
    public void convertToObjectSuccess() throws IOException {
        String jsonString = "{\n" +
                "  \"alarmConfigured\": true,\n" +
                "  \"enable\": false,\n" +
                "  \"frequency\": 0,\n" +
                "  \"instanceId\": \"UnitTestInstanceId\",\n" +
                "  \"name\": \"UnitTestAlarm\",\n" +
                "  \"threshold\": 0\n" +
                "}";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(jsonString));
        when(httpServletRequest.getReader()).thenReturn(bufferedReader);
        Alarm alarm = SpringUtils.convertToObject(httpServletRequest, Alarm.class);
        assertEquals(alarm.getName(),"UnitTestAlarm");
        assertEquals(alarm.getInstanceId(),"UnitTestInstanceId");
        assertEquals(alarm.getThreshold(),Double.valueOf(0));
        assertEquals(alarm.getFrequency(),Integer.valueOf(0));
        assertEquals(alarm.isAlarmConfigured(),true);
        assertEquals(alarm.isEnable(),false);
    }

    @Test(expected = UnrecognizedPropertyException.class)
    public void convertToObjectUnrecognizedPropertyException() throws IOException {
        String jsonString = "{\n" +
                "  \"alarmConfigured\": true,\n" +
                "  \"enable\": false,\n" +
                "  \"frequency\": 0,\n" +
                "  \"instanceId\": \"UnitTestInstanceId\",\n" +
                "  \"name\": \"UnitTestAlarm\",\n" +
                "  \"threshold\": 0,\n" +
                "  \"dummyField\": \"DummyValue\"\n" +
                "}";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(jsonString));
        when(httpServletRequest.getReader()).thenReturn(bufferedReader);
        Alarm alarm = SpringUtils.convertToObject(httpServletRequest, Alarm.class);
    }

    @Test(expected = JsonMappingException.class)
    public void convertToObjectJsonMappingException() throws IOException {
        // When jsonString and Object in which cast are different
        String jsonString = "{\n" +
                "  \"alarmConfigured\": true,\n" +
                "  \"enable\": false,\n" +
                "  \"frequency\": 0,\n" +
                "  \"instanceId\": \"UnitTestInstanceId\",\n" +
                "  \"name\": \"UnitTestAlarm\",\n" +
                "  \"threshold\": 0\n" +
                "}";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(jsonString));
        when(httpServletRequest.getReader()).thenReturn(bufferedReader);
        Instance alarm = SpringUtils.convertToObject(httpServletRequest, Instance.class);
    }
}
