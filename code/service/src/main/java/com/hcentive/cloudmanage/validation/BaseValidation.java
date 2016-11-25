package com.hcentive.cloudmanage.validation;


public class BaseValidation {

    public boolean isStringNullOrEmpty(String text){
        return text == null || text.isEmpty();
    }
    public boolean isIntegerNull(Integer integer){
        return integer == null;
    }
    public boolean isDoubleNull(Double doubleValue){
        return doubleValue == null;
    }
}
