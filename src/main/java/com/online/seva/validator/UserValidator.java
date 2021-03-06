package com.online.seva.validator;

import com.online.seva.domain.User;
import com.online.seva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 6 || user.getUsername().length() > 50) {
            errors.rejectValue("email", "Size.user.username");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "user.firstName.NotEmpty");
        if (user.getName().length() < 3 || user.getName().length() > 50) {
            errors.rejectValue("firstName", "Size.user.firstName");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("email", "Duplicate.user.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.password.NotEmpty");
        if (user.getPassword().length() < 5 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.user.password");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}
