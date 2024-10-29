package pros.ElectronicStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pros.ElectronicStore.entities.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {

     List<Category> findByTitleContaining(String keywords);
}
