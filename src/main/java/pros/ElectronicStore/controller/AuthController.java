package pros.ElectronicStore.controller;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import pros.ElectronicStore.dtos.*;
import pros.ElectronicStore.entities.Providers;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.exceptions.BadApiRequestException;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.security.JwtHelper;
import pros.ElectronicStore.services.RefreshTokenService;
import pros.ElectronicStore.services.UserService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

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

    @Value("${google.client.id}")
    private String googleClientApi;

    @Value("${google.user.default.password}")
    private  String googleDefaultPassword;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private final Logger logger= LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/regenerate-token")
    public ResponseEntity<JWTResponse> regenerateToken(@RequestBody RefreshTokenRequest request) throws Exception {
        RefreshTokenDto refreshTokenDto = refreshTokenService.findByToken(request.getRefreshToken());
        RefreshTokenDto verifiedToken = refreshTokenService.verify(refreshTokenDto);
        UserDto user = refreshTokenService.getUser(verifiedToken);
        String jwtToken = jwtHelper.generateToken(mapper.map(user, User.class));
        JWTResponse response= JWTResponse.builder()
                .token(jwtToken)
                .user(user)
                .refreshToken(verifiedToken)
                .build();

     return ResponseEntity.ok(response);
    }


    @PostMapping("/generate-token")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request){
        logger.info("Username {} ,Password {}",request.getEmail(),request.getPassword());
        this.doAuthenticate(request.getEmail(),request.getPassword());
        User user = (User) userDetailsService.loadUserByUsername(request.getEmail());
        logger.info(user.toString());
        String token= jwtHelper.generateToken(user);
        // Generating refreshToken
        RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        JWTResponse response = JWTResponse.builder()
                .token(token)
                .user(mapper.map(user, UserDto.class))
                .refreshToken(refreshToken)
                .build();
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
    public ResponseEntity<JWTResponse>LoginWithGoogle(@RequestBody GoogleLogin request ) throws GeneralSecurityException, IOException {
        logger.info("Google Token : {}",request.getGoogleToken());
        // token Verification

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new GsonFactory()).setAudience(List.of(googleClientApi)).build();

        GoogleIdToken idToken = verifier.verify(request.getGoogleToken());
        if (idToken!=null){
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String  name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            logger.info("Email {}",email);
            logger.info("Name {}",name);
            logger.info("familyName {}",familyName);
            logger.info("pictureUrl {}",pictureUrl);
            logger.info(" Given name {}",givenName);
        // creating the user

            UserDto userDto=new UserDto();
            userDto.setName(name);
            userDto.setImageName(pictureUrl);
            userDto.setAbout("User created using a Google");
            userDto.setEmail(email);
            userDto.setPassword(googleDefaultPassword);
            userDto.setProviders(Providers.GOOGLE);
            UserDto userDto1=null;
            try{
                 userDto1 = userService.getUserByEmail(userDto.getEmail());
                 logger.info("this time user is loaded from database ");

                if (userDto1.getProviders().equals(userDto.getProviders())){
                    // continue
                }
                else{
                    throw new BadCredentialsException("Your email is Registered Through other provider try to login with ur Email and password");
                }


            }catch (ResourceNotFound ex){
                logger.info("This time user is created  ");
                userDto1 = userService.createUser(userDto);
            }

            this.doAuthenticate(userDto1.getEmail(),userDto.getPassword());
            User user = mapper.map(userDto1, User.class);
            String token= jwtHelper.generateToken(user);
            JWTResponse response = JWTResponse.builder().token(token).user(mapper.map(user, UserDto.class)).build();
            return ResponseEntity.ok(response);
        }
        else{
            logger.info("Token is Invalid !!");
            throw new BadApiRequestException("Invalid Google user !!");
        }



    }

}
