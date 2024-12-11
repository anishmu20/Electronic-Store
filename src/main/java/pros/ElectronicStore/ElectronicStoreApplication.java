package pros.ElectronicStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pros.ElectronicStore.entities.Role;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.repositories.RoleRepository;
import pros.ElectronicStore.repositories.UserRepository;

import java.util.*;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role role1 = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		if (role1==null){
			role1=new Role();
			role1.setRoleId(UUID.randomUUID().toString().substring(0,3));
			role1.setName("ROLE_ADMIN");
			roleRepository.save(role1);
		}
		Role role2 = roleRepository.findByName("ROLE_NORMAL").orElse(null);
		if (role2==null){
			role2=new Role();
			role2.setRoleId(UUID.randomUUID().toString().substring(0,3));
			role2.setName("ROLE_NORMAL");
			roleRepository.save(role2);
		}

       //  creating one admin for testing purpose
		User user = userRepository.findByEmail("anishmu302@gmail.com").orElse(null);
		if (user==null){
			user=new User();
			user.setUserId(UUID.randomUUID().toString());
			user.setName("anish");
			user.setEmail("anishmu302@gmail.com");
			user.setPassword(passwordEncoder.encode("anish123"));
			user.setRoles(List.of(role1));
			userRepository.save(user);
		}


	}
}
