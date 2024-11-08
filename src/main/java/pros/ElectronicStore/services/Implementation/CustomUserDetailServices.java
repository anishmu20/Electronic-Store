package pros.ElectronicStore.services.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.repositories.UserRepository;
@Service
public class CustomUserDetailServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User with this email not found"));
        return user;
    }
}
