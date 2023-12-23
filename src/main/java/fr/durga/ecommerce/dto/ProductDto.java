package fr.durga.ecommerce.dto;

import fr.durga.ecommerce.model.Product;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

public class ProductDto {



    private Integer id;

    private @NotNull String name;
    private @NotNull String imageURL;
    private @NotNull double price;
    private @NotNull String description;
    private @NotNull Integer category_Id;


    public ProductDto(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategory_id() {
        return category_Id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_Id = category_id;
    }

    public ProductDto(Product product) {
        //this.setId(product.getId());
        this.setName(product.getName());
        this.setImageURL(product.getImageURL());
        this.setDescription(product.getDescription());
        this.setPrice(product.getPrice());
        this.setCategory_id(product.getCategory().getId());

    }

    public ProductDto(@NotNull String name, @NotNull String imageURL, @NotNull double price, @NotNull String description, @NotNull Integer category_id) {
        this.name = name;
        this.imageURL = imageURL;
        this.price = price;
        this.description = description;
        this.category_Id = category_id;
    }
}
