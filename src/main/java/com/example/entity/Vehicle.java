package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;  // e.g., KA01AB1234

    private String type;                // e.g., Two-Wheeler, Four-Wheeler, Commercial

    private String model;               // Model name, e.g., Swift

    private String fuelType;            // Petrol, Diesel, CNG, Electric

    private String color;

    private Integer manufactureYear;

    private String insuranceNumber;

    private LocalDate insuranceExpiryDate;

    private String pollutionCertificateNumber;

    private LocalDate pollutionExpiryDate;

    private String registrationAuthority;  // e.g., RTO Bangalore

    private LocalDate registrationDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleRepair> repairs;
}
