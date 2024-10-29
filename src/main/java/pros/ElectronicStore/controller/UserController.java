package pros.ElectronicStore.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pros.ElectronicStore.dtos.ApiResponseMessage;
import pros.ElectronicStore.dtos.ImageResponse;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.services.FileService;
import pros.ElectronicStore.services.UserService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;
    @Value("${user.image.profile.active}")
    private String ImagePath;        // value for this is defined in the application.properties file

    private Logger logger= LoggerFactory.getLogger(UserController.class);
    /**
     * Creates a new user in the system.
     *
     * <p>This method handles POST requests to create a new user by accepting a UserDto
     * object in the request body. It invokes the UserService to process and save the
     * user information, and then returns the created UserDto along with a status of
     * 201 (Created).</p>
     *
     * @param userDto the DTO object containing user information for creation
     * @return a ResponseEntity containing the created UserDto and HTTP status 201 (Created)
     */

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid  @RequestBody UserDto userDto){
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    /**
     * Updates an existing user in the system.
     *
     * <p>This method handles PUT requests to update an existing user's details. It
     * accepts a user ID as a path variable and a UserDto object in the request body.
     * It invokes the UserService to update the user information and then returns the
     * updated UserDto along with a status of 200 (OK).</p>
     *
     * @param user_id the ID of the user to be updated
     * @param userDto the DTO object containing updated user information
     * @return a ResponseEntity containing the updated UserDto and HTTP status 200 (OK)
     */
    @PutMapping("/{user_id}")
    public  ResponseEntity<UserDto> update(@PathVariable("user_id") String user_id,@Valid @RequestBody UserDto userDto){
        UserDto updatedUser = userService.updateUser(userDto, user_id);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }
    /**
     * Deletes an existing user from the system.
     *
     * <p>This method handles DELETE requests to remove a user identified by the user ID
     * provided as a path variable. It invokes the UserService to delete the user
     * and then returns a custom ApiResponseMessage indicating the success of the operation,
     * along with a status of 200 (OK).</p>
     *
     * @param user_id the ID of the user to be deleted
     * @return a ResponseEntity containing an ApiResponseMessage with success status and HTTP status 200 (OK)
     */


    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable ("user_id") String user_id){
        userService.deleteUser(user_id);
        ApiResponseMessage message = ApiResponseMessage.builder().message("User Deleted Successfully !!")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    /**
     * Retrieves all users from the system.
     *
     * <p>This method handles GET requests to fetch a list of all users in the system.
     * It invokes the UserService to retrieve all users and returns a list of UserDto
     * objects, along with a status of 200 (OK).</p>
     *
     * @return a ResponseEntity containing a list of UserDto objects and HTTP status 200 (OK)
     */

    @GetMapping()
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "Gender",required = false) String sortBy,
            @RequestParam(value = "sortDirection",defaultValue = "asc",required = false) String sortDirection

    ){
        return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDirection),HttpStatus.OK);
    }
    /**
     * Retrieves a user by their ID.
     *
     * <p>This method handles GET requests to fetch a user's details based on their
     * user ID, provided as a path variable. It invokes the UserService to retrieve
     * the user and returns the UserDto object along with a status of 200 (OK).</p>
     *
     * @param user_id the ID of the user to be retrieved
     * @return a ResponseEntity containing the UserDto object and HTTP status 200 (OK)
     */

    @GetMapping("/{user_id}")
    public  ResponseEntity<UserDto> getUser(@PathVariable ("user_id") String user_id ){
        return  new ResponseEntity<>(userService.getUserById(user_id),HttpStatus.OK);
    }
    /**
     * Retrieves a user by their email.
     *
     * <p>This method handles GET requests to fetch a user's details based on their
     * email address, provided as a path variable. It invokes the UserService to
     * retrieve the user and returns the UserDto object along with a status of 200 (OK).</p>
     *
     * @param user_email the email of the user to be retrieved
     * @return a ResponseEntity containing the UserDto object and HTTP status 200 (OK)
     */

    @GetMapping("/email/{user_email}")
    public  ResponseEntity<UserDto> getUserByEmail(@PathVariable("user_email") String user_email){
        return new ResponseEntity<>(userService.getUserByEmail(user_email),HttpStatus.OK);
    }

    /**
     * Searches for users by a keyword.
     *
     * <p>This method handles GET requests to search for users based on a keyword
     * provided as a path variable. It invokes the UserService to search for users
     * matching the keyword in their details and returns a list of UserDto objects
     * along with a status of 200 (OK).</p>
     *
     * @param keyword the search keyword to filter users
     * @return a ResponseEntity containing a list of UserDto objects and HTTP status 200 (OK)
     */

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> Search(@PathVariable("keyword") String keyword){
        return  new ResponseEntity<>(userService.searchUser(keyword),HttpStatus.OK);
    }

    @PostMapping("/upload/image/{userId}")
    public ResponseEntity<ImageResponse> saveImage(
            @PathVariable("userId") String userId,
            @RequestParam("UserImage")MultipartFile image
            ) throws IOException {


        String imageName = fileService.UploadFile(image, ImagePath);
        UserDto userById = userService.getUserById(userId);
        userById.setImageName(imageName);
        UserDto userDto = userService.updateUser(userById, userId);

        ImageResponse imageResponse=ImageResponse.builder().ImageName(imageName).message("Image Upload Successfully").
        success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    @GetMapping("/download/image/{userId}")
    public void serveImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {
        UserDto userById = userService.getUserById(userId);
        logger.info("Image name : {} ",userById.getImageName());
        InputStream resource = fileService.getResource(ImagePath, userById.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
