package pros.ElectronicStore.exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFound extends RuntimeException{
   public ResourceNotFound(){
        super("Resource not Found");
    }
    public ResourceNotFound(String message){
        super(message);
    }

}
