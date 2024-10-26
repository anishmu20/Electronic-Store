package pros.ElectronicStore.services.Implementation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pros.ElectronicStore.dtos.CategoryDto;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.entities.Category;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.helper.Helper;
import pros.ElectronicStore.repositories.CategoryRepository;
import pros.ElectronicStore.services.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        String id= UUID.randomUUID().toString();
        categoryDto.setCategoryId(id);
        Category category = DtoToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return EntityToDto(savedCategory);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending()));
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<Category> pages = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> response= Helper.getPageResponse(pages,CategoryDto.class);
        return response;
    }

    @Override
    public CategoryDto getCategoryById(String id) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Category with this id not Found"));
        return EntityToDto(category);
    }

    @Override
    public CategoryDto Update(CategoryDto categoryDto, String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Category with this id not Found"));
        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(category.getCoverImage());

        Category updateCategory = categoryRepository.save(category);

        return EntityToDto(updateCategory);
    }

    @Override
    public void delete(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Category with this id not Found"));
        categoryRepository.delete(category);
    }


    CategoryDto EntityToDto(Category category){
        return mapper.map(category,CategoryDto.class);
    }

    Category DtoToEntity(CategoryDto categoryDto){
        return mapper.map(categoryDto,Category.class);
    }

}
