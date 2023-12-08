package ru.yandex.practicum.filmorate.annotations;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsBeforeBirthdayMovieValidator.class)
public @interface IsBeforeBirthdayMovie {
    String message() default "Дата должна быть не раньше {value}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}
