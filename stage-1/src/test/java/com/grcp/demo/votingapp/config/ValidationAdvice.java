package com.grcp.demo.votingapp.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.executable.ExecutableValidator;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.lang.Nullable;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.reflect.Method;
import java.util.Set;

public class ValidationAdvice implements MethodBeforeAdvice {
//        https://github.com/loiane/crud-angular-spring/blob/ae2f58cdbc8cf1287ff156760589991f9f48b16e/crud-spring/src/test/java/com/loiane/course/CourseServiceTest.java#L39
//        https://github.com/loiane/crud-angular-spring/blob/ae2f58cdbc8cf1287ff156760589991f9f48b16e/crud-spring/src/test/java/com/loiane/course/CourseServiceTest.java#L23

    static private final ExecutableValidator executableValidator;

    static {
        try (LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean()) {
            factory.afterPropertiesSet();
            executableValidator = factory.getValidator().forExecutables();
        }
    }

    @Override
    public void before(Method method, Object[] args, @Nullable Object target) {
        Set<ConstraintViolation<Object>> violations = executableValidator
                .validateParameters(target, method, args);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
