package pros.ElectronicStore.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageValidator implements ConstraintValidator<ImageValid,String> {
   private Logger logger= LoggerFactory.getLogger(ImageValidator.class);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        logger.info("is valid method{} ",value);
        //logic
        if (value.isBlank()){
            return false;
        }
        else{
            return true;
        }

    }
}
