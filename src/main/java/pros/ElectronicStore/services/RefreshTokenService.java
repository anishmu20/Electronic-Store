package pros.ElectronicStore.services;


import pros.ElectronicStore.dtos.RefreshTokenDto;
import pros.ElectronicStore.dtos.UserDto;

public interface RefreshTokenService {

    // create token

    RefreshTokenDto  createRefreshToken(String username);

    // findToken
    RefreshTokenDto  findByToken(String token);

    // verify
    RefreshTokenDto verify( RefreshTokenDto token) throws Exception;

    UserDto getUser(RefreshTokenDto tokenDto);
}
