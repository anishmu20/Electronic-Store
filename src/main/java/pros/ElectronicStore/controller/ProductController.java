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
import pros.ElectronicStore.dtos.ApiResponseMessage;
import pros.ElectronicStore.dtos.ImageResponse;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.dtos.ProductDto;
import pros.ElectronicStore.services.FileService;
import pros.ElectronicStore.services.ProductService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${product.image.profile.active}")
    private String productImagePath;

    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto){
        ProductDto productDto1 = productService.create(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>>getAllProduct(
            @RequestParam(value = "pageNumber",required = false,defaultValue ="0") int pageNumber,
            @RequestParam(value = "pageSize",required = false,defaultValue ="10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue ="productName") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue ="asc") String sortDir
    ){
        PageableResponse<ProductDto> allProduct = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct,HttpStatus.OK);
    }
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductLive(
            @RequestParam(value = "pageNumber",required = false,defaultValue ="0") int pageNumber,
            @RequestParam(value = "pageSize",required = false,defaultValue ="10") int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue ="productName") String sortBy,
            @RequestParam(value = "sortDir",required = false,defaultValue ="asc") String sortDir
    ){
        PageableResponse<ProductDto> allProductLive = productService.getAllProductLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProductLive,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable("id")String id){
        ProductDto product = productService.getProduct(id);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@RequestBody ProductDto  productDto ,@PathVariable("id") String id){
        ProductDto updateProductDto = productService.update(productDto, id);
        return new ResponseEntity<>(updateProductDto,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable("id") String id){
        productService.delete(id);
        ApiResponseMessage productDeleteSuccessfully = ApiResponseMessage.builder().message("product delete successfully").status(HttpStatus.OK).success(true).build();
      return ResponseEntity.ok(productDeleteSuccessfully);
    }
    @GetMapping("/search/{keywords}")
    public ResponseEntity<PageableResponse<ProductDto>> search(
    @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
    @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
    @RequestParam(value="sortBy",defaultValue = "productName",required = false) String sortBy,
    @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir,@PathVariable("keywords") String keywords)
    {
        PageableResponse<ProductDto> search = productService.search(keywords, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(search,HttpStatus.OK);
    }


    @PostMapping("/image/upload/{productId}")
    public ResponseEntity<ImageResponse> upload(
            @PathVariable("productId") String id,
            @RequestParam ("productImage") MultipartFile file
    ) throws IOException {
        String productImageName = fileService.UploadFile(file, productImagePath);
        ProductDto product = productService.getProduct(id);
        product.setProductImage(productImageName);
        ProductDto updateProduct = productService.update(product, id);
        ImageResponse imageResponse = ImageResponse.builder().message("Image uploaded successfully ").status(HttpStatus.OK)
                .ImageName(productImageName)  .success(true).build();
        return ResponseEntity.ok(imageResponse);
    }


    @GetMapping("/image/download/{productId}")
    public void download(@PathVariable("productId") String id, HttpServletResponse response) throws IOException {
        ProductDto product = productService.getProduct(id);
        InputStream resource = fileService.getResource(productImagePath,product.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }


}
