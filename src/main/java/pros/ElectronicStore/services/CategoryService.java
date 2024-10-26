package pros.ElectronicStore.services;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import pros.ElectronicStore.dtos.CategoryDto;
import pros.ElectronicStore.dtos.PageableResponse;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);

    PageableResponse<CategoryDto> getAllCategories(int pageNumber,int pageSize,String sortBy,String sortDir);

    CategoryDto getCategoryById(String id);

    CategoryDto Update(CategoryDto categoryDto,String id);


    void delete(String id);



}
