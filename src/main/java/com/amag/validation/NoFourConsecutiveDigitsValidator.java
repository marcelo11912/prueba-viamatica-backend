package com.amag.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoFourConsecutiveDigitsValidator implements ConstraintValidator<NoFourConsecutiveDigits, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.length() < 4) {
            return true; // No hay suficientes dígitos para validar
        }

        char[] chars = value.toCharArray();
        int count = 1;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == chars[i - 1]) {
                count++;
            } else {
                count = 1;
            }
            if (count == 4) {
                return false; // Se encontraron cuatro dígitos iguales consecutivos
            }
        }
		return true;
	}

}
