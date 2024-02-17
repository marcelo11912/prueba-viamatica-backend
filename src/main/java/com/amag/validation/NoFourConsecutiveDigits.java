package com.amag.validation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoFourConsecutiveDigitsValidator.class)
public @interface NoFourConsecutiveDigits {
	String message() default "El número no puede tener cuatro dígitos iguales consecutivos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
