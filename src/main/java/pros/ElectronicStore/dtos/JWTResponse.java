package pros.ElectronicStore.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JWTResponse {

    private String token;

    private  UserDto user;

    private RefreshTokenDto refreshToken;

}
