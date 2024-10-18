package pros.ElectronicStore.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageValidator.class)
public @interface ImageValid {

    // error message
    String message() default "Invalid  Image Name";
    // represents group of constraints
    Class<?>[] groups() default { };
    // represents additional information about annotation (ImageValid)
    Class<? extends Payload>[] payload() default { };

}
