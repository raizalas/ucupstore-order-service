package com.ucupstore.orderservice.order.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotBlank(message = "ISBN must be defined")
        String isbn,

        @NotNull(message = "Quantity is empty")
        @Min(value = 1, message = "Minimum order is 1 item")
        @Max(value = 5, message = "Maximum order is 5 items")
        Integer quantity
) {
}
