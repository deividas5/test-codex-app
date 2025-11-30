package com.example.warehouseapp.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ItemRequest {
    @NotBlank
    private String name;
    private String description;
    @Min(0)
    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
