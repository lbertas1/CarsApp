package com.app.service;

import com.app.persistence.model.Car;
import com.app.persistence.model.enums.Color;
import com.app.service.enums.SortItem;
import com.app.service.extensions.ExtensionForCar;
import com.app.service.help.MileageAndPowerStats;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ExtensionForCar.class)
@RequiredArgsConstructor
class CarServiceTest {

    private final CarService carService;

    @Test
    public void getCars() {
        assertNotNull(carService.getCars());
    }

    @Test
    public void sortByMileage() {
        List<Car> listAfterSort = carService.sort(SortItem.MILEAGE, false);

        Car carWithMaxMileage = carService
                .getCars()
                .stream()
                .max(Comparator.comparing(Car::getMileage))
                .orElseThrow();

        Car carWithMinMileage = carService
                .getCars()
                .stream()
                .min(Comparator.comparing(Car::getMileage))
                .orElseThrow();

        assertAll(() -> {
            assertEquals(carWithMaxMileage, listAfterSort.get(listAfterSort.size() - 1));
            assertEquals(carWithMinMileage, listAfterSort.get(0));
        });
    }

    @Test
    public void sortByMileageDescending() {
        List<Car> listAfterSort = carService.sort(SortItem.MILEAGE, true);

        Car carWithMaxMileage = carService
                .getCars()
                .stream()
                .max(Comparator.comparing(Car::getMileage))
                .orElseThrow();

        Car carWithMinMileage = carService
                .getCars()
                .stream()
                .min(Comparator.comparing(Car::getMileage))
                .orElseThrow();

        assertAll(() -> {
            assertEquals(carWithMaxMileage, listAfterSort.get(0));
            assertEquals(carWithMinMileage, listAfterSort.get(listAfterSort.size() - 1));
        });
    }

    @Test
    public void sortByModelAscending() {
        List<Car> listAfterSort = carService.sort(SortItem.MODEL, false);

        Car lastCarOnList = carService
                .getCars()
                .stream()
                .max(Comparator.comparing(Car::getModel))
                .orElseThrow();

        Car firstCarOnList = carService
                .getCars()
                .stream()
                .min(Comparator.comparing(Car::getModel))
                .orElseThrow();

        assertAll(() -> {
            assertEquals(lastCarOnList, listAfterSort.get(listAfterSort.size() - 1));
            assertEquals(firstCarOnList, listAfterSort.get(0));
        });
    }

    @Test
    public void sortByModelDescending() {
        List<Car> listAfterSort = carService.sort(SortItem.MODEL, true);

        Car lastCarOnList = carService
                .getCars()
                .stream()
                .max(Comparator.comparing(Car::getModel))
                .orElseThrow();

        Car firstCarOnList = carService
                .getCars()
                .stream()
                .min(Comparator.comparing(Car::getModel))
                .orElseThrow();

        assertAll(() -> {
            assertEquals(lastCarOnList, listAfterSort.get(0));
            assertEquals(firstCarOnList, listAfterSort.get(listAfterSort.size() - 1));
        });
    }

    @Test
    public void sortByColorAscending() {
        List<Car> listAfterSort = carService.sort(SortItem.COLOR, false);

        Car firstCarOnList = carService
                .getCars()
                .stream()
                .sorted(Comparator.comparing(Car::getColor))
                .collect(Collectors.toList())
                .get(0);

        Car lastCarOnList = carService
                .getCars()
                .stream()
                .sorted(Comparator.comparing(Car::getColor))
                .collect(Collectors.toList())
                .get(listAfterSort.size() - 1);

        assertAll(() -> {
            assertEquals(lastCarOnList, listAfterSort.get(listAfterSort.size() - 1));
            assertEquals(firstCarOnList, listAfterSort.get(0));
        });
    }

    @Test
    public void sortByColorDescending() {
        List<Car> listAfterSort = carService.sort(SortItem.COLOR, true);

        Car firstCarOnList = carService
                .getCars()
                .stream()
                .sorted(Comparator.comparing(Car::getColor))
                .collect(Collectors.toList())
                .get(0);

        Car lastCarOnList = carService
                .getCars()
                .stream()
                .sorted(Comparator.comparing(Car::getColor))
                .collect(Collectors.toList())
                .get(listAfterSort.size() - 1);

        assertAll(() -> {
            assertEquals(firstCarOnList, listAfterSort.get(listAfterSort.size() - 1));
            assertEquals(lastCarOnList, listAfterSort.get(0));
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {5000, 2000, 1000, 3000, 6000, 7000, 10000})
    public void getCarsWhereMileageIsBiggerThan(int mileage) {
        List<Car> cars = carService.getCarsWhereMileageIsBiggerThan(mileage);

        List<Car> collect = cars
                .stream()
                .filter(car -> car.getMileage() <= mileage)
                .collect(Collectors.toList());

        assertTrue(collect.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(Color.class)
    public void groupByColorAndQuantity(Color color) {
        long quantity = carService
                .getCars()
                .stream()
                .filter(car -> car.getColor().equals(color))
                .count();

        Map<Color, Long> colorLongMap = carService.groupByColorAndQuantity();

        assertEquals(quantity, colorLongMap.get(color));
    }

    @RepeatedTest(20)
    public void groupByModel() {
        int carNumber = new Random().nextInt(carService.getCars().size());
        Map<String, Car> stringCarMap = carService.groupByModel();
        assertEquals(stringCarMap.get(carService.getCars().get(carNumber).getModel()).getModel(), carService.getCars().get(carNumber).getModel());
    }

    @Test
    public void getStatisticsForCarsByPrice() {
        MileageAndPowerStats mileageAndPowerStats = carService.statisticsForCars();

        double biggestPriceDoubleValue = carService
                .getCars()
                .stream()
                .max(Comparator.comparing(Car::getPrice))
                .orElseThrow()
                .getPrice()
                .doubleValue();

        BigDecimal biggestPrice = BigDecimal.valueOf(biggestPriceDoubleValue);

        double smallestPriceDoubleValue = carService
                .getCars()
                .stream()
                .min(Comparator.comparing(Car::getPrice))
                .orElseThrow()
                .getPrice()
                .doubleValue();

        BigDecimal smallestPrice = BigDecimal.valueOf(smallestPriceDoubleValue);

        double average = carService
                .getCars()
                .stream()
                .mapToDouble(car -> car.getPrice().doubleValue())
                .average()
                .orElseThrow();

        BigDecimal avg = BigDecimal.valueOf(average);

        assertAll(() -> {
            assertEquals(biggestPrice, mileageAndPowerStats.getPriceStats().getMax());
            assertEquals(smallestPrice, mileageAndPowerStats.getPriceStats().getMin());
            assertEquals(avg, mileageAndPowerStats.getPriceStats().getAvg());
        });
    }

    @Test
    public void getStatisticsForCarsByMileage() {
        MileageAndPowerStats mileageAndPowerStats = carService.statisticsForCars();

        double biggestMileage = carService
                .getCars()
                .stream()
                .max(Comparator.comparing(Car::getMileage))
                .orElseThrow()
                .getMileage()
                .doubleValue();

        double smallestMileage = carService
                .getCars()
                .stream()
                .min(Comparator.comparing(Car::getMileage))
                .orElseThrow()
                .getMileage()
                .doubleValue();

        double average = carService
                .getCars()
                .stream()
                .mapToDouble(car -> car.getMileage().doubleValue())
                .average()
                .orElseThrow();

        assertAll(() -> {
            assertEquals(biggestMileage, mileageAndPowerStats.getMileageStats().getMax());
            assertEquals(smallestMileage, mileageAndPowerStats.getMileageStats().getMin());
            assertEquals(average, mileageAndPowerStats.getMileageStats().getAvg());
        });
    }

    @Test
    public void getMostExpensiveCar() {
        Car mostExpensiveCar = carService.getMostExpensiveCar();
        Car car = carService
                .getCars()
                .stream()
                .max(Comparator.comparing(Car::getPrice))
                .orElseThrow();

        assertAll(() -> {
            assertNotNull(mostExpensiveCar);
            assertEquals(car, mostExpensiveCar);
        });
    }

    @Test
    @RepeatedTest(20)
    public void sortComponentCollection() {
        List<Car> carsWithSortedComponents = carService.sortComponentsCollection();
        int carNumber = new Random().nextInt(carsWithSortedComponents.size());
        assertTrue(carsWithSortedComponents.get(carNumber).getComponents().get(1).charAt(0) >= carsWithSortedComponents.get(carNumber).getComponents().get(0).charAt(0));
    }

    @Test
    @RepeatedTest(20)
    public void groupByComponent() {
        int carNumber = new Random().nextInt(carService.getCars().size());
        int componentNumber = new Random().nextInt(carService.getCars().get(carNumber).getComponents().size());
        String component = carService
                .getCars()
                .get(carNumber)
                .getComponents()
                .get(componentNumber);

        Map<String, List<Car>> stringListMap = carService.groupByComponent();

        assertTrue(stringListMap.get(component).contains(carService.getCars().get(carNumber)));
    }

    @ParameterizedTest
    @MethodSource("valueAsPriceRange")
    public void getCarsInPriceRange(Double price1, Double price2) {
        List<Car> carsInPriceRange = carService.getCarsInPriceRange(price1, price2)
                .stream()
                .sorted(Comparator.comparing(Car::getPrice))
                .collect(Collectors.toList());

        System.out.println("carsInPriceRange = " + carsInPriceRange);

        assertAll(() -> {
            assertTrue(carsInPriceRange.get(0).getPrice().doubleValue() >= price1);
            assertTrue(carsInPriceRange.get(carsInPriceRange.size() - 1).getPrice().doubleValue() <= price2);
        });
    }

    static List<Arguments> valueAsPriceRange() {
        return List.of(
                Arguments.of(30.0, 130.0),
                Arguments.of(40.0, 140.0),
                Arguments.of(50.0, 100.0),
                Arguments.of(20.0, 80.0),
                Arguments.of(30.0, 90.0),
                Arguments.of(60.0, 100.0)
        );
    }
}