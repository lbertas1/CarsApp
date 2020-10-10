package com.app.service.validator;

import com.app.persistence.model.Car;
import com.app.persistence.model.enums.Color;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class CarValidator implements Validator<Car> {
    @Override
    public Map<String, String> validate(Car car) {
        if (car == null) {
            return Map.of("car object", "is null");
        }

        Map<String, String> errors = new HashMap<>();

        if (!isModelValid(car.getModel())) {
            errors.put("model", "is not valid");
        }

        if (!isPriceValid(car.getPrice())) {
            errors.put("price", "ins't valid");
        }

        if (!isColorValid(car.getColor())) {
            errors.put("color", "is null");
        }

        if (!isMileageValid(car.getMileage())) {
            errors.put("mileage", "ins't valid");
        }

        if (!isComponentsCollectionValid(car.getComponents())) {
            errors.put("components", "ins't valid");
        }

        return errors;
    }

    private boolean isModelValid(String model) {
        return model.matches("[A-Z]+\\s*[A-Z]*");
    }

    private boolean isPriceValid(BigDecimal price) {
        return price.intValue() > 0;
    }

    private boolean isColorValid(Color color) {
        return color != null;
    }

    private boolean isMileageValid(Integer mileage) {
        return mileage > 0;
    }

    private boolean isComponentsCollectionValid(List<String> components) {
        AtomicBoolean isValid = new AtomicBoolean(true);
        components.forEach(s -> {
            if (!s.matches("[A-Z]+\\s*[A-Z]*")) {
                isValid.set(false);
            }
        });
        return isValid.get();
    }
}
