package com.hcentive.cloudmanage.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class SpringUtils {

    private static final String JSON = "json";
    public static <T> T convertToObject(HttpServletRequest request,Class<T> valueType) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = request.getReader();
        String readLine;
        try{
            while ((readLine = bufferedReader.readLine()) != null){
                stringBuilder.append(readLine).append('\n');
            }
            return (T)new ObjectMapper().readValue(stringBuilder.toString(),valueType);
        }finally {
            bufferedReader.close();
        }
    }

    public static boolean isContentTypeJson(HttpServletRequest request){
        return request.getContentType().contains(JSON);
    }

    public static BindingResult bindingResult(Object object,String objectName){
        return new BeanPropertyBindingResult(object,objectName);
    }

    public static String bindingResultErrorMsg(BindingResult bindingResult){
        StringBuilder stringBuilder = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> {
            if(error instanceof FieldError){
                FieldError fieldError = (FieldError)error;
                stringBuilder.append(fieldError.getCode()).append(",");
            }
        });
        return stringBuilder.toString();
    }
}
