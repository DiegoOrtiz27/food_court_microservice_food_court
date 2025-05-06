package com.foodquart.microservicefoodcourt.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEmployeeEntity {
    @Id
    @Column(name = "employee_id", nullable = false, unique = true)
    private Long employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;
}
