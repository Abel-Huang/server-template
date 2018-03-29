package cn.abelib.shop.common.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Created by abel on 2018/2/6.
 *  手机号码格式验证器
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {
    private boolean required = false;

    @Override
    public void initialize(Mobile mobile) {
       required = mobile.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required){
            return ValidatorUtil.mobileValidator(s);
        }
        if (StringUtils.isEmpty(s)){
            return true;
        }else {
            return ValidatorUtil.mobileValidator(s);
        }
    }
}
