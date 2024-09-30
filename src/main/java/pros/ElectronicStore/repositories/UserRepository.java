package pros.ElectronicStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pros.ElectronicStore.entities.User;

public interface UserRepository extends JpaRepository<User,String> {

    // customs methods


}
