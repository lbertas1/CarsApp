package com.app.ui;

import com.app.service.CarService;
import com.app.ui.data.UserData;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class MenuService {
    private final CarService carService;

    private int chooseOption() {
        System.out.println("Menu:");
        System.out.println("1. Show cars");
        System.out.println("2. Sort");
        System.out.println("3. Get cars where mileage is bigger than");
        System.out.println("4. Group cars by color and quantity");
        System.out.println("5. Group cars by model");
        System.out.println("6. Get statistics for cars");
        System.out.println("7. Get most expensive car");
        System.out.println("8. Get by sorting components");
        System.out.println("9. Group by components");
        System.out.println("10. Get cars in price range");
        System.out.println("0. End of app");
        return UserData.getInt("Choose option:");
    }

    public void menu() {

        int option;
        do {
            option = chooseOption();
            switch (option) {
                case 1 -> option1();
                case 2 -> option2();
                case 3 -> option3();
                case 4 -> option4();
                case 5 -> option5();
                case 6 -> option6();
                case 7 -> option7();
                case 8 -> option8();
                case 9 -> option9();
                case 10 -> option10();
                case 0 -> {
                    System.out.println("Have a nice day!");
                    return;
                }
                default -> System.out.println("No such option");
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private void option1() {
        System.out.println(carService.cars());
    }

    private void option2() {
        var sortedCars = carService.sort(
                UserData.getSortItem(),
                UserData.getBoolean("Descending?"));
        System.out.println(sortedCars);
    }

    private void option3() {
        var cars = carService.getCarsWhereMileageIsBiggerThan(
               UserData.getDouble("Provide mileage")
        );
        System.out.println(cars);
    }

    private void option4() {
        var cars = carService.groupByColorAndQuantity();
        System.out.println(cars);
    }

    private void option5() {
        var cars = carService.groupByModel();
        System.out.println(cars);
    }

    private void option6() {
        carService.statisticsForCars();
    }

    private void option7() {
        System.out.println(carService.getMostExpensiveCar());
    }

    private void option8() {
        System.out.println(carService.sortComponentsCollection());
    }

    private void option9() {
        var groupByComponent = carService.groupByComponent();
        groupByComponent.forEach((s, cars) -> {
            System.out.println("COMPONENT: " + s + "\n");
            cars.forEach(car -> System.out.println(car + " \n"));
        });
    }

    private void option10() {
        System.out.println(carService.getCarsInPriceRange(
                UserData.getDouble("Enter first range"),
                UserData.getDouble("Enter second range")
        ));
    }
}
