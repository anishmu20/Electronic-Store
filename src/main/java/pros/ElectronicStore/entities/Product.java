package pros.ElectronicStore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id")
    private   String productId;
    @Column(name = "product_title")
    private String productName;
    @Column(name = "product_description",length = 10000)
    private String description;
    @Column(name = "product_price")
    private  int price;
    @Column(name = "product_discount_price")
    private int discountedPrice;
    @Column(name = "product_quantity")
    private int quantity;
    @Column(name = "product_addedDate")
    private Date addedDate;

    @Column(name = "product_live")
    private boolean live;

    @Column(name = "product_stock")
    private boolean stock;

    @Column(name = "product_modelYear")
    private int modelYear;

    @Column(name = "product_image")
    private String productImage;
}
