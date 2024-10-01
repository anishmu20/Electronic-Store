package pros.ElectronicStore.services.Implementation;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.repositories.UserRepository;
import pros.ElectronicStore.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
     // generate id
        String id = UUID.randomUUID().toString();
        userDto.setUserId(id);
        //Dto-entity
        User user=dtoToEntity(userDto);
        User savedUser = userRepository.save(user);

        // entity to dto
        UserDto responseUserDto=entityToDto(savedUser);

        return responseUserDto;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String user_id) {

        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("user with given id not found"));
        user.setName(userDto.getName());
        // email noy updating
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        User updatedUser = userRepository.save(user);

        return entityToDto(updatedUser);
        
    }

    @Override
    public void deleteUser(String user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("user with given id not found"));
        userRepository.delete(user);

    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> userlist = userRepository.findAll();
        return userlist.stream().map(this::entityToDto).toList();

    }

    @Override
    public UserDto getUserById(String user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("user with given id not found"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user with this email not found"));

        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        return users.stream().map(this::entityToDto).toList();
    }


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
