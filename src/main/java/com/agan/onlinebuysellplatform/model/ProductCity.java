package com.agan.onlinebuysellplatform.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(schema = "buyselleha", name = "product_cities")
public class ProductCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "german_city_id")
    private GermanCity germanCity;
}
