package pros.ElectronicStore.services.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.repositories.UserRepository;
import pros.ElectronicStore.services.UserService;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;
    Logger  logger= LoggerFactory.getLogger(UserServiceImplementation.class);

    @Override
    public UserDto createUser(UserDto userDto) {
     // generate id
        String id = UUID.randomUUID().toString();
        userDto.setUserId(id);
        //Dto-entity
        User user=dtotoEntity(userDto);
        User savedUser = userRepository.save(user);

        // entity to dto
        UserDto responseUserDto=entityToDto(savedUser);

        return responseUserDto;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String id) {
        return null;
    }

    @Override
    public void deleteUser(String id) {

    }

    @Override
    public List<UserDto> getAll() {
        return List.of();
    }

    @Override
    public UserDto getUserById(String id) {
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        return List.of();
    }


    private UserDto entityToDto(User savedUser) {

        UserDto userDto=UserDto.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .password(savedUser.getEmail())
                .Gender(savedUser.getGender())
                .about(savedUser.getAbout())
                .image(savedUser.getImageName())
                .build();
        return userDto;
    }

    private User dtotoEntity(UserDto userDto) {

        User user= User.builder().
                userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .about(userDto.getAbout())
                .Gender(userDto.getGender())
                .password(userDto.getPassword())
                .imageName(userDto.getImage()).build();
        return user;

    }


}
