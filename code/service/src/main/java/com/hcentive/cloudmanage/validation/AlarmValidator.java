package com.hcentive.cloudmanage.validation;

import com.hcentive.cloudmanage.domain.Alarm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AlarmValidator extends BaseValidation implements Validator {

    private static final String FREQUENCY_VALUE_MSG = "Frequency should be either 12 or 24";
    private static final String FREQUENCY_NULL_MSG = "Frequency field should not be null";
    private static final String THRESHOLD_VALUE_MSG = "Threshold value should be between 1 and 5";
    private static final String THRESHOLD_NULL_MSG = "Threshold field should not be null";
    private static final String INSTANCE_MSG = "Instance id cannot be null or empty";

    @Override
    public boolean supports(Class<?> clazz) {
        return Alarm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Alarm alarm = (Alarm) target;

        if(isStringNullOrEmpty(alarm.getInstanceId())){
            errors.rejectValue("instanceId",INSTANCE_MSG);
        }

        if(isIntegerNull(alarm.getFrequency())){
            errors.rejectValue("frequency",FREQUENCY_NULL_MSG);
        }else {
            if(!checkAllowFrequency(alarm.getFrequency())){
                errors.rejectValue("frequency", FREQUENCY_VALUE_MSG);
            }
        }
        if(isDoubleNull(alarm.getThreshold())){
            errors.rejectValue("threshold",THRESHOLD_NULL_MSG);
        }else{
            if(!checkAllowThreshold(alarm.getThreshold())){
                errors.rejectValue("threshold", THRESHOLD_VALUE_MSG);
            }
        }
    }

    private boolean checkAllowFrequency(Integer frequency){
        return (frequency == 12 || frequency == 24);
    }

    private boolean checkAllowThreshold(Double threshold){
        return threshold <= 5.0 && threshold >=1;
    }
}
