package pros.ElectronicStore.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pros.ElectronicStore.Validation.ImageValid;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {


    private String userId;
    @Size(min = 3,max = 20,message = "Invalid name !!")
    private String name;
//    @Email(message = "Invalid email !! ")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",message = "Invalid email")
    @NotBlank(message = "Email is required !!")
    private String email;
    @NotBlank(message = "Password is required !!")
    private String password;
    @Size(min = 4,max = 7,message = "Invalid gender !!")
    private String Gender;
    @NotBlank
    private String about;
    @ImageValid
    private String imageName;

}
