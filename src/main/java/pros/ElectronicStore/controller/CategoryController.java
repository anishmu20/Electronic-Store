package pros.ElectronicStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pros.ElectronicStore.dtos.ApiResponseMessage;
import pros.ElectronicStore.dtos.CategoryDto;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> save(@RequestBody CategoryDto categoryDto ){
        CategoryDto SavedCategoryDto = categoryService.create(categoryDto);
        return new ResponseEntity<>(SavedCategoryDto, HttpStatus.CREATED);

    }
    // getAllUser
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllUser(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber ,
            @RequestParam(value = "pageSize",defaultValue = "0",required = false) int pageSize,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "sortDir",required = false) String sortDir
    )
    {
        PageableResponse<CategoryDto> allCategories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allCategories,HttpStatus.OK);
    }

    //getSingle
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String id ){
        CategoryDto categoryById = categoryService.getCategoryById(id);
        return  new ResponseEntity<>(categoryById,HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> Update(@RequestBody CategoryDto  categoryDto,@PathVariable String id){
        CategoryDto updatedCategory = categoryService.Update(categoryDto, id);
        return  new ResponseEntity<>(updatedCategory,HttpStatus.OK);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseMessage> Delete(@PathVariable String id){
        categoryService.delete(id);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Deleted SuccessFully ").
                status(HttpStatus.OK)
                .build();
         return new  ResponseEntity<>(response,HttpStatus.OK);
    }




}
