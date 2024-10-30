package pros.ElectronicStore.services.Implementation;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pros.ElectronicStore.dtos.CategoryDto;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.dtos.ProductDto;
import pros.ElectronicStore.entities.Category;
import pros.ElectronicStore.entities.Product;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.helper.Helper;
import pros.ElectronicStore.repositories.CategoryRepository;
import pros.ElectronicStore.repositories.ProductRepository;
import pros.ElectronicStore.services.CategoryService;
import pros.ElectronicStore.services.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository  productRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${product.image.profile.active}")
    private String productImagePath;
    @Autowired
    CategoryRepository categoryRepository;
    private Logger logger= LoggerFactory.getLogger(ProductServiceImplementation.class);


    @Override
    public ProductDto create(ProductDto productDto) {
        String productId= UUID.randomUUID().toString().substring(0,8);
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto getProduct(String productId) {
        Product productFound = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found"));
        return mapper.map(productFound,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> all = productRepository.findAll(pageable);
        return Helper.getPageResponse(all,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> byLiveTrue = productRepository.findByLiveTrue(pageable);
        return Helper.getPageResponse(byLiveTrue,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product productFound = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found"));
        productFound.setProductName(productDto.getProductName());
        productFound.setLive(productDto.isLive());
        productFound.setPrice(productDto.getPrice());
        productFound.setStock(productDto.isStock());
        productFound.setAddedDate(new Date());
        productFound.setDiscountedPrice(productDto.getDiscountedPrice());
        productFound.setQuantity(productDto.getQuantity());
        productFound.setModelYear(productDto.getModelYear());
        productFound.setDescription(productDto.getDescription());
        productFound.setProductImage(productDto.getProductImage());
        Product updatedProduct = productRepository.save(productFound);

        return mapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product productFound = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found"));
        String FullPath=productImagePath+productFound.getProductImage();
        try{
            Path path = Paths.get(FullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex){
            logger.info("User image not found in this Folder ");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        productRepository.delete(productFound);
    }

    @Override
    public PageableResponse<ProductDto> search(String keyword,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> byProductNameContaining = productRepository.findByProductNameContaining(keyword, pageable);
        return Helper.getPageResponse(byProductNameContaining,ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound("Category not found"));
        String productId= UUID.randomUUID().toString().substring(0,8);
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(String categoryId, String productId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound("Category not Found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> findAllProductWithSameCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound("Category not found"));
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable  pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> allProductWithSameCategory = productRepository.findAllProductWithSameCategory(category, pageable);

        return Helper.getPageResponse(allProductWithSameCategory,ProductDto.class);
    }
}
