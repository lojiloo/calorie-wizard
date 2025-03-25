package project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CheckEnum {
    Class<? extends Enum<?>> enumClass();

    String message() default "Неверный формат enum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
