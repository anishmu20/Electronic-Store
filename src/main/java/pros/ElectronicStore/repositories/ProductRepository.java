package pros.ElectronicStore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pros.ElectronicStore.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

   Page<Product> findByProductNameContaining(String keywords,Pageable pageable);
   Page<Product> findByLiveTrue(Pageable pageable);

}
