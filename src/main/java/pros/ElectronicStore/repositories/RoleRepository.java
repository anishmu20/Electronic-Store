package pros.ElectronicStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pros.ElectronicStore.entities.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {

    Optional<Role> findByName(String name);
}
