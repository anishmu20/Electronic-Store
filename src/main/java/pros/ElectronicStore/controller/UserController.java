package pros.ElectronicStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pros.ElectronicStore.dtos.ApiResponseMessage;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    // Create
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto){
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    // Update
    @PutMapping("/{user_id}")
    public  ResponseEntity<UserDto> update(@PathVariable("user_id") String user_id,@RequestBody UserDto userDto){
        UserDto updatedUser = userService.updateUser(userDto, user_id);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }
    // Delete
    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable ("user_id") String user_id){
        userService.deleteUser(user_id);
        ApiResponseMessage message = ApiResponseMessage.builder().message("User Deleted Successfully !!")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }


    // GetAll
    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUser(),HttpStatus.OK);
    }
    // GetByUser
    @GetMapping("/{user_id}")
    public  ResponseEntity<UserDto> getUser(@PathVariable ("user_id") String user_id ){
        return  new ResponseEntity<>(userService.getUserById(user_id),HttpStatus.OK);
    }

    //GetByEmail
    @GetMapping("/email/{user_email}")
    public  ResponseEntity<UserDto> getUserByEmail(@PathVariable("user_email") String user_email){
        return new ResponseEntity<>(userService.getUserByEmail(user_email),HttpStatus.OK);
    }

    // Search

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> Search(@PathVariable("keyword") String keyword){
        return  new ResponseEntity<>(userService.searchUser(keyword),HttpStatus.OK);
    }
}
