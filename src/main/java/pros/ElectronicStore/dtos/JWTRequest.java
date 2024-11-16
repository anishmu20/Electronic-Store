package pros.ElectronicStore.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JWTRequest {

    private String email;

    private String password;
}
