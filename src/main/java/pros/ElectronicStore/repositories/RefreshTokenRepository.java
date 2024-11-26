package pros.ElectronicStore.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pros.ElectronicStore.entities.RefreshToken;
import pros.ElectronicStore.entities.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {

    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
}
