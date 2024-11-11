package pros.ElectronicStore.services.Implementation;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.entities.Role;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.helper.Helper;
import pros.ElectronicStore.repositories.RoleRepository;
import pros.ElectronicStore.repositories.UserRepository;
import pros.ElectronicStore.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RoleRepository roleRepository ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger  logger= LoggerFactory.getLogger(UserServiceImplementation.class);
    @Value("${user.image.profile.active}")
    private String ImagePath;

    /**
     * Creates a new user in the system.
     *
     * <p>This method generates a unique user ID, converts the UserDto to a User entity,
     * and saves it to the database. It then converts the saved User entity back to
     * a UserDto and returns it.</p>
     *
     * @param userDto the DTO object containing user information for creation
     * @return the created UserDto object with generated user ID and persisted data
     */


    @Override
    public UserDto createUser(UserDto userDto) {
     // generate id
        String id = UUID.randomUUID().toString();
        userDto.setUserId(id);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));



        //Dto-entity
        User user=dtoToEntity(userDto);
        // ASSIGNING THE DEFAULT ROLE
        Role role1=new Role();
        role1.setRoleId(UUID.randomUUID().toString().substring(0,3));
        role1.setName("ROLE_NORMAL");
        Role role=roleRepository.findByName("ROLE_NORMAL").orElse(role1);

        user.setRoles(List.of(role));
        User savedUser = userRepository.save(user);

        // entity to dto
        UserDto responseUserDto=entityToDto(savedUser);

        return responseUserDto;
    }

    /**
     * Updates an existing user by user ID.
     *
     * <p>This method fetches an existing User entity by its ID, updates its fields
     * based on the provided UserDto (excluding email), saves the updated entity
     * back to the database, and returns the updated UserDto.</p>
     *
     * @param userDto the DTO object containing updated user information
     * @param user_id the ID of the user to be updated
     * @return the updated UserDto object with the new details
     * @throws ResourceNotFound if the user with the given ID is not found
     */

    @Override
    public UserDto updateUser(UserDto userDto, String user_id) {

        User user = userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFound("user with given id not found"));
        user.setName(userDto.getName());
        // email not updating
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setImageName(userDto.getImageName());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        User updatedUser = userRepository.save(user);

        return entityToDto(updatedUser);
        
    }
    /**
     * Deletes a user by user ID.
     *
     * <p>This method fetches an existing User entity by its ID and deletes it
     * from the database.</p>
     *
     * @param user_id the ID of the user to be deleted
     * @throws ResourceNotFound if the user with the given ID is not found
     */

    @Override
    public void deleteUser(String user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFound("user with given id not found"));
        // delete Image of user if present
        String FullPath=ImagePath+user.getImageName();
        try{
            Path path = Paths.get(FullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex){
            logger.info("User image not found in this Folder ");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.getRoles().clear();
        User saved = userRepository.save(user);

        userRepository.delete(saved);

    }
    /**
     * Retrieves all users from the system.
     *
     * <p>This method fetches all User entities from the database, converts them
     * to UserDto objects, and returns a list of these DTOs.</p>
     *
     * @return a list of all UserDto objects representing users in the system
     */

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort=(sortDirection.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable  pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> pages = userRepository.findAll(pageable);
        PageableResponse<UserDto> pageResponse = Helper.getPageResponse(pages, UserDto.class);
        return pageResponse;

    }
    /**
     * Retrieves a user by user ID.
     *
     * <p>This method fetches a User entity by its ID, converts it to a UserDto,
     * and returns the DTO.</p>
     *
     * @param user_id the ID of the user to be retrieved
     * @return the UserDto object representing the user with the given ID
     * @throws ResourceNotFound if the user with the given ID is not found
     */


    @Override
    public UserDto getUserById(String user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFound("user with given id not found"));
        return entityToDto(user);
    }
    /**
     * Retrieves a user by email address.
     *
     * <p>This method fetches a User entity by its email address, converts it to
     * a UserDto, and returns the DTO.</p>
     *
     * @param email the email address of the user to be retrieved
     * @return the UserDto object representing the user with the given email
     * @throws ResourceNotFound if a user with the given email is not found
     */


    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFound("user with this email not found"));

        return entityToDto(user);
    }
    /**
     * Searches for users by a keyword.
     *
     * <p>This method searches for User entities whose names contain the given
     * keyword, converts them to UserDto objects, and returns a list of these DTOs.</p>
     *
     * @param keyword the search keyword used to filter users by name
     * @return a list of UserDto objects that match the search keyword
     */


    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        return users.stream().map(this::entityToDto).toList();
    }
    /**
     * Converts a User entity to a UserDto object.
     *
     * <p>This method uses the configured mapper to transform a User entity into
     * a UserDto object for data transfer between application layers.</p>
     *
     * @param savedUser the User entity to be converted
     * @return a UserDto object with data populated from the entity
     */


    private UserDto entityToDto(User savedUser) {
// Manual method
//        UserDto userDto=UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getEmail())
//                .Gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .image(savedUser.getImageName())
//                .build();
        return mapper.map(savedUser,UserDto.class);
    }
    /**
     * Converts a UserDto object to a User entity.
     *
     * <p>This method uses the configured mapper to transform a UserDto into a
     * User entity for use in database operations.</p>
     *
     * @param userDto the DTO object containing user information
     * @return a User entity with data populated from the DTO
     */

    private User dtoToEntity(UserDto userDto) {
// Manual method
//        User user= User.builder().
//                userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .about(userDto.getAbout())
//                .Gender(userDto.getGender())
//                .password(userDto.getPassword())
//                .imageName(userDto.getImage()).build();


        return mapper.map(userDto,User.class);

    }


}
