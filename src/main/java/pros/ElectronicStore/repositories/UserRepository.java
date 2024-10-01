package pros.ElectronicStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pros.ElectronicStore.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    // customs methods
    // query type
//    @Query("select u from User u where u.email=?1")
//    Optional<User> getByEmail(String user_email);

   // method type
    Optional<User> findByEmail(String user_email);

    Optional<User> findByEmailAndPassword(String user_email,String user_password);

    // Search
    List<User> findByNameContaining(String keywords);

}
