package com.agan.onlinebuysellplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "buyselleha", name = "products")
@Data
@ToString(exclude = {"user", "images", "cities"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    @Column(name = "description")
    private String description;

    @Positive(message = "Price must be a positive number")
    @Column(name = "price")
    private Integer price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "product")
    private List<Image> images = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @Column(name = "preview_image_id")
    private Long previewImageId;

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;

    @ManyToMany
    @JoinTable(name = "product_cities",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "german_city_id")
    )
    private List<GermanCity> cities = new ArrayList<>();

    @PrePersist
    private void onCreate() {
        dateOfCreated = LocalDateTime.now();
    }

    public void addImageToProduct(Image image) {
        image.setProduct(this);
        images.add(image);
    }
}
