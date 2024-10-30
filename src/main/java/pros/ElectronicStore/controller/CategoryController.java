package pros.ElectronicStore.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pros.ElectronicStore.dtos.*;
import pros.ElectronicStore.services.CategoryService;
import pros.ElectronicStore.services.FileService;
import pros.ElectronicStore.services.ProductService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Value("${category.image.profile.active}")
    private String categoryPath;

    @Autowired
    private FileService fileService;

    @Autowired
    ProductService  productService;

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
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue = "asc") String sortDir
    )
    {
        PageableResponse<CategoryDto> allCategories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allCategories,HttpStatus.OK);
    }

    //getSingle
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable("id") String id ){
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

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable("keywords") String keyword){
        List<CategoryDto> categoryDtos = categoryService.searchCategory(keyword);
        return ResponseEntity.ok(categoryDtos);
    }


    @PostMapping("/image/upload/{categoryId}")
    public ResponseEntity<ImageResponse> upload(
            @PathVariable("categoryId") String id,
            @RequestParam ("categoryImage") MultipartFile file
    ) throws IOException {
        String categoryImageName = fileService.UploadFile(file, categoryPath);
        CategoryDto categoryById = categoryService.getCategoryById(id);
        categoryById.setCoverImage(categoryImageName);
        CategoryDto updated = categoryService.Update(categoryById,id);
        ImageResponse imageResponse = ImageResponse.builder().message("Image uploaded successfully ").status(HttpStatus.OK)
                .ImageName(categoryImageName)  .success(true).build();
        return ResponseEntity.ok(imageResponse);
    }


    @GetMapping("/image/download/{categoryId}")
    public void download(@PathVariable("categoryId") String id, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.getCategoryById(id);
        InputStream resource = fileService.getResource(categoryPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createWithCategory(
     @PathVariable("categoryId") String categoryId,
     @RequestBody ProductDto  productDto
    ){
        ProductDto withCategory = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(withCategory,HttpStatus.OK);
    }

    @PutMapping("/{categoryId}/{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable("categoryId") String categoryId,
      @PathVariable("productId")  String productId){
        ProductDto updateProduct = productService.update(categoryId, productId);
        return new ResponseEntity<>(updateProduct,HttpStatus.OK);
    }

    @GetMapping("/sameCategoryProducts/{categoryId}")
    public ResponseEntity<PageableResponse<ProductDto>> sameCategoryProducts(
     @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
     @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
     @RequestParam(value = "sortBy",defaultValue = "productName",required = false) String sortBy,
     @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir,
     @PathVariable("categoryId") String categoryId
    ){
        PageableResponse<ProductDto> allProductWithSameCategory = productService.findAllProductWithSameCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProductWithSameCategory,HttpStatus.OK);
    }

}
