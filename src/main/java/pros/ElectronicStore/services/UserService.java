package pros.ElectronicStore.services;

import pros.ElectronicStore.dtos.UserDto;

import java.util.List;

public interface UserService {

    // create
  UserDto createUser(UserDto userDto);


    // update

  UserDto updateUser(UserDto userDto ,String id);

    // delete
    void deleteUser(String id);


    // getall

    List<UserDto> getAllUser();

    // get single user

    UserDto getUserById(String id);

    // get single user by email

    UserDto getUserByEmail(String email);

    // search user
    List<UserDto> searchUser(String keyword);

}
