package pros.ElectronicStore.controller;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import pros.ElectronicStore.dtos.GoogleLogin;
import pros.ElectronicStore.dtos.JWTRequest;
import pros.ElectronicStore.dtos.JWTResponse;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.security.JwtHelper;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private ModelMapper mapper;

    private final Logger logger= LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/generate-token")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request){
        logger.info("Username {} ,Password {}",request.getEmail(),request.getPassword());
        this.doAuthenticate(request.getEmail(),request.getPassword());
        User user = (User) userDetailsService.loadUserByUsername(request.getEmail());
        logger.info(user.toString());
        String token= jwtHelper.generateToken(user);
        JWTResponse response = JWTResponse.builder().token(token).user(mapper.map(user, UserDto.class)).build();
        return ResponseEntity.ok(response);
    }

    private void doAuthenticate(String email, String password) {

        try{
            Authentication authentication=new UsernamePasswordAuthenticationToken(email,password);
            logger.info(authentication.toString());
            authenticationManager.authenticate(authentication);

        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid email and password");
        }
    }

    @PostMapping("/login-with-google")
    public ResponseEntity<JWTResponse>LoginWithGoogle(@RequestBody GoogleLogin request ){
        logger.info("Google Token : {}",request.getGoogleToken());
        return null;
    }

}
