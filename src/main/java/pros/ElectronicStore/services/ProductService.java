package pros.ElectronicStore.services;

import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto create(ProductDto productDto);
    ProductDto getProduct(String productId);
    PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy,String sortDir);
    PageableResponse<ProductDto> getAllProductLive(int pageNumber,int pageSize,String sortBy,String sortDir);
    ProductDto update(ProductDto productDto,String productId);
    void delete(String productId);
    PageableResponse<ProductDto> search(String keyword,int pageNumber,int pageSize,String sortBy,String sortDir);

    ProductDto createWithCategory(ProductDto productDto,String categoryId);

    ProductDto update(String categoryId,String productId);

    PageableResponse<ProductDto>findAllProductWithSameCategory (String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);


}
