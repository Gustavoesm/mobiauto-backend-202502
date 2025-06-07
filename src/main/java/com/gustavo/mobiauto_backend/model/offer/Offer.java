package com.gustavo.mobiauto_backend.model.offer;

import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "offers")
@NoArgsConstructor
@Getter
@Setter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OfferStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @OneToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Offer(OfferStatus status, User client, Vehicle vehicle, Store store) {
        this.status = status;
        this.client = client;
        this.vehicle = vehicle;
        this.store = store;
    }

    public Offer(User client, Vehicle vehicle, Store store) {
        this.status = OfferStatus.NEW;
        this.client = client;
        this.vehicle = vehicle;
        this.store = store;
    }
}
