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
import pros.ElectronicStore.entities.Category;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.helper.Helper;
import pros.ElectronicStore.repositories.CategoryRepository;
import pros.ElectronicStore.services.CategoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.image.profile.active}")
    private String categoryPath;
    private Logger logger= LoggerFactory.getLogger(CategoryServiceImplementation.class);


    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        String id= UUID.randomUUID().toString().substring(0,8);
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
        category.setCoverImage(categoryDto.getCoverImage());

        Category updateCategory = categoryRepository.save(category);

        return EntityToDto(updateCategory);
    }

    @Override
    public void delete(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Category with this id not Found"));
        String FullPath=categoryPath+category.getCoverImage();
        try{
            Path path = Paths.get(FullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex){
            logger.info("category image not found in this Folder ");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> byNameContaining = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> list = byNameContaining.stream().map((obj) -> mapper.map(obj, CategoryDto.class)).toList();
        return list;
    }


    CategoryDto EntityToDto(Category category){
        return mapper.map(category,CategoryDto.class);
    }

    Category DtoToEntity(CategoryDto categoryDto){
        return mapper.map(categoryDto,Category.class);
    }

}
