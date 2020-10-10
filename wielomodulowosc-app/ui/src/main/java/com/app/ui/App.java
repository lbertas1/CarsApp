package com.app.ui;

import com.app.service.CarService;
import com.app.ui.controller.Controller;

import static spark.Spark.initExceptionHandler;
import static spark.Spark.port;

public class App {
    public static void main(String[] args) {

        final String FILENAME_PATH = "./resources/data/cars.json";
        var carService = new CarService(FILENAME_PATH);
        var menuService = new MenuService(carService);
        menuService.menu();

        port(8090);
        initExceptionHandler(e -> System.out.println(e.getMessage()));

        Controller controller = new Controller(carService);
        controller.initRoutes();
    }
}
