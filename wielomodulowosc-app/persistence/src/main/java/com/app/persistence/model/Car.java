package com.app.persistence.model;

import com.app.persistence.model.enums.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    private String model;
    private BigDecimal price;
    private Color color;
    private Integer mileage;
    private List<String> components;

    @Override
    public String toString() {
        return String.join("\n",
            "MODEL: " + model,
            "PRICE: " + price,
            "COLOR: " + color,
            "MILEAGE: " + mileage,
            "COMPONENTS: " + components.stream().collect(Collectors.joining(", "))
        );
    }
}
