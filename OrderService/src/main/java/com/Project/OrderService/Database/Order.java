package com.Project.OrderService.Database;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    int id;

    @Column(name = "address", length = 200)
    String address;

    @Column(name = "message", length = 300)
    String message;

    @Column(name = "issues", length = 300)
    String issues;

    @Column(name = "amounts", length = 300)
    String amounts;

    @Column(name = "total")
    float total;

}
