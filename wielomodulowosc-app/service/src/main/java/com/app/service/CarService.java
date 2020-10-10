package com.app.service;

import com.app.persistence.converters.JsonCarsConverter;
import com.app.persistence.model.Car;
import com.app.persistence.model.enums.Color;
import com.app.service.enums.SortItem;
import com.app.service.exception.CarServiceException;
import com.app.service.help.MileageAndPowerStats;
import com.app.service.help.Stats;
import com.app.service.validator.CarValidator;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@NoArgsConstructor
public class CarService {
    private List<Car> cars;

    public CarService(String filename) {
        this.cars = init(filename);
    }

    public List<Car> getCars() {
        return cars;
    }

    private List<Car> init(String filename) {
        AtomicInteger counter = new AtomicInteger(1);
        return new JsonCarsConverter(filename)
                .fromJson()
                .orElseThrow(() -> new CarServiceException("cannot convert data from file"))
                .stream()
                .filter(car -> {
                    var carValidator = new CarValidator();
                    var errors = carValidator.validate(car);
                    if (!errors.isEmpty()) {
                        System.out.println("----------------------- validation errors for car no. " + counter.get() + " ------------------------");
                        System.out.println(errors
                                .entrySet()
                                .stream()
                                .map(e -> e.getKey() + ": " + e.getValue())
                                .collect(Collectors.joining("\n"))
                        );
                    }
                    counter.incrementAndGet();
                    return errors.isEmpty();
                }).collect(Collectors.toList());
    }

    public String cars() {
        return cars
                .stream()
                .map(Car::toString)
                .collect(Collectors.joining("\n\n"));
    }

    public List<Car> sort(SortItem sortItem, boolean descending) {
        Stream<Car> sortedCars = switch (sortItem) {
            case COLOR -> cars.stream().sorted(Comparator.comparing(Car::getColor));
            case MODEL -> cars.stream().sorted(Comparator.comparing(Car::getModel));
            case MILEAGE -> cars.stream().sorted(Comparator.comparing(Car::getMileage));
            default -> cars.stream().sorted(Comparator.comparing(Car::getPrice));
        };

        var sortedCarsAsList = sortedCars.collect(Collectors.toList());
        if (descending) {
            Collections.reverse(sortedCarsAsList);
        }
        return sortedCarsAsList;
    }

    public List<Car> getCarsWhereMileageIsBiggerThan(double mileage) {
        if (mileage < 0) throw new IllegalArgumentException("Given mileage is smaller than 0");

        return cars
                .stream()
                .filter(car -> car.getMileage() > mileage)
                .collect(Collectors.toList());
    }

    public Map<Color, Long> groupByColorAndQuantity() {
         return cars
                 .stream()
                .collect(Collectors
                        .groupingBy(Car::getColor, Collectors.counting()));
    }

    public Map<String, Car> groupByModel() {
        return cars.stream()
                .collect(
                        Collectors.groupingBy(
                                Car::getModel,
                                Collectors.collectingAndThen(
                                        Collectors.maxBy(Comparator.comparing(Car::getPrice)),
                                        Optional::orElseThrow
                                )))
                .entrySet()
                .stream()
                .sorted((stringCarEntry, t1) -> Integer.compare(t1.getKey().length(), stringCarEntry.getKey().length()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (car, car2) -> car,
                        LinkedHashMap::new
                ));
    }

    public MileageAndPowerStats statisticsForCars() {
        DoubleSummaryStatistics summaryStatisticsByPrice = cars.stream()
                .mapToDouble(car -> car.getPrice().doubleValue())
                .summaryStatistics();

        DoubleSummaryStatistics summaryStatisticsByMileage = cars.stream()
                .mapToDouble(Car::getMileage)
                .summaryStatistics();

        return MileageAndPowerStats
                .builder()
                .priceStats(Stats.<BigDecimal>builder()
                        .max(BigDecimal.valueOf(summaryStatisticsByPrice.getMax()))
                        .min(BigDecimal.valueOf(summaryStatisticsByPrice.getMin()))
                        .avg(BigDecimal.valueOf(summaryStatisticsByPrice.getAverage()))
                        .build())
                .mileageStats(Stats.<Double>builder()
                        .max(summaryStatisticsByMileage.getMax())
                        .min(summaryStatisticsByMileage.getMin())
                        .avg(summaryStatisticsByMileage.getAverage())
                        .build())
                .build();
    }

    public Car getMostExpensiveCar() {
        return cars
                .stream()
                .max(Comparator.comparing(Car::getPrice))
                .orElseThrow();
    }

    public List<Car> sortComponentsCollection() {
        return cars.stream()
                .map(car -> {
                    List<String> componentsTmpList = car.getComponents().stream()
                            .sorted(Comparator.comparing(s -> s))
                            .collect(toList());
                    car.setComponents(componentsTmpList);
                    return car;
                })
                .collect(toList());
    }

    public Map<String, List<Car>> groupByComponent() {
        return cars
                .stream()
                .flatMap(car -> car.getComponents().stream())
                .distinct()
                .collect(toMap(
                        Function.identity(),
                        component -> cars
                                .stream()
                                .filter(car -> car.getComponents().contains(component))
                                .collect(Collectors.toList())
                ));
    }

    public List<Car> getCarsInPriceRange(double a, double b) {
        if (a < 0) throw new IllegalArgumentException("Argument a is smaller than 0");
        if (b < 0) throw new IllegalArgumentException("Argument b is smaller than 0");

        return cars.stream()
                .filter(car -> car.getPrice().doubleValue() >= a && car.getPrice().doubleValue() <= b)
                .sorted(Comparator.comparing(Car::getModel))
                .collect(toList());
    }
}
