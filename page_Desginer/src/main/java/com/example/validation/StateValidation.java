package com.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.anno.State;

public class StateValidation implements ConstraintValidator<State, String> {
    /**
     * @param value                      将来要校验的数据
     * @param constraintValidatorContext
     * @return false不通过，true通过
     */

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        if (value.equals("待销售") || value.equals("有货")||value.equals("缺货")) {
            return true;
        }
        return false;
    }
}
