package pros.ElectronicStore.services.Implementation;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pros.ElectronicStore.dtos.RefreshTokenDto;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.entities.RefreshToken;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.repositories.RefreshTokenRepository;
import pros.ElectronicStore.repositories.UserRepository;
import pros.ElectronicStore.services.RefreshTokenService;

import java.time.Instant;
import java.util.UUID;
@Service
public class RefreshTokenImplementation implements RefreshTokenService {

    private UserRepository userRepository;

    private RefreshTokenRepository refreshTokenRepository;

    private ModelMapper mapper;

    public RefreshTokenImplementation(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.mapper = mapper;
    }

    @Override
    public RefreshTokenDto createRefreshToken(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFound("User not found exception"));
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);
        if (refreshToken==null){
         refreshToken=RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(5*24*60*60))
                .build();}
        else{
            refreshToken.setExpiryDate(Instant.now().plusSeconds(5*24*60*60));
            refreshToken.setToken(UUID.randomUUID().toString());
        }
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        return mapper.map(savedToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto findByToken(String token) {
        RefreshToken RefreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFound("Refresh Token not found"));
        return mapper.map(RefreshToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verify(RefreshTokenDto token) throws Exception {

        RefreshToken token1 = mapper.map(token, RefreshToken.class);
        if (token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token1);
            throw new Exception("Refresh token expired");
        }
        else{
            return token;
        }
    }

    @Override
    public UserDto getUser(RefreshTokenDto tokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenDto.getToken()).orElseThrow(() -> new ResourceNotFound("Refresh token not found"));
        User user = refreshToken.getUser();
        return mapper.map(user,UserDto.class);
    }
}
