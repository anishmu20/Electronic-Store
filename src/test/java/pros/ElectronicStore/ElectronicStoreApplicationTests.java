package pros.ElectronicStore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.repositories.UserRepository;
import pros.ElectronicStore.security.JwtHelper;

@SpringBootTest
class ElectronicStoreApplicationTests {
	@Autowired
    private  UserRepository userRepository;
	@Autowired
	private JwtHelper jwtHelper;
	@Test
	void contextLoads() {
	}
	@Test
	void testToken(){
		User user = userRepository.findByEmail("anishmu302@gmail.com").get();
		String token=jwtHelper.generateToken(user);
		System.out.println(token);

		System.out.println(jwtHelper.getUsernameFromToken(token));
		System.out.println(jwtHelper.isTokenExpired(token));
	}

}
