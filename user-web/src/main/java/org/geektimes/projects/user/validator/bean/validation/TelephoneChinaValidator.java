package org.geektimes.projects.user.validator.bean.validation;

import org.apache.commons.lang.StringUtils;
import org.geektimes.projects.user.domain.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelephoneChinaValidator implements ConstraintValidator<TelephoneChina, String> {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    @Override
    public void initialize(TelephoneChina annotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        Matcher m = mobile_pattern.matcher(value);
        // 获取模板信息 context.getDefaultConstraintMessageTemplate();
        return m.matches();
    }
}
