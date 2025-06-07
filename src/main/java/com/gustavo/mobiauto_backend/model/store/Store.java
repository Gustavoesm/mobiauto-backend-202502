package com.gustavo.mobiauto_backend.model.store;

import java.util.List;

import com.gustavo.mobiauto_backend.model.offer.Offer;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    @Setter
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private StoreName name;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Offer> offers;

    public Store(String storeName) {
        this.name = new StoreName(storeName);
        this.offers = List.of();
    }
}