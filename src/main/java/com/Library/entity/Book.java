package com.Library.entity;

import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int quantity;
    private String barcode;

    // Default constructor
    public Book() {}

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {   // 👈 REQUIRED
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {  // 👈 REQUIRED
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {   // 👈 REQUIRED
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() { // 👈 REQUIRED
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
