package com.app.ui.controller;

import com.app.service.CarService;
import com.app.service.JsonTransformer;
import com.app.service.enums.SortItem;
import lombok.RequiredArgsConstructor;

import static spark.Spark.*;


@RequiredArgsConstructor
public class Controller {

    private final CarService carService;

    public void initRoutes() {

        path("/get-cars", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return carService.cars();
            }), new JsonTransformer());
        });

        path("/sort-by-item", () -> {
            get("/:sortItem/:order", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                SortItem sortItem = SortItem.valueOf(request.params("sortItem"));
                boolean order = Boolean.parseBoolean(request.params("order"));
                return carService.sort(sortItem, order);
            }), new JsonTransformer());
        });

        path("/cars-with-mileage-bigger-than", () -> {
            get("/:mileage", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                double mileage = Double.parseDouble(request.params("mileage"));
                return carService.getCarsWhereMileageIsBiggerThan(mileage);
            }), new JsonTransformer());
        });

        path("/group-by-model", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                carService.groupByModel();
                return null;
            }), new JsonTransformer());
        });

        path("/statistics", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                carService.statisticsForCars();
                return null;
            }), new JsonTransformer());
        });

        path("/most-expensive-car", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return carService.getMostExpensiveCar();
            }), new JsonTransformer());
        });

        path("/sort-component-collection", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return carService.sortComponentsCollection();
            }), new JsonTransformer());
        });

        path("/group-by-component", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                carService.groupByComponent();
                return null;
            }), new JsonTransformer());
        });

        path("/cars-in-price-range", () -> {
            get("/:price1/:price2", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                double price1 = Double.parseDouble(request.params("price1"));
                double price2 = Double.parseDouble(request.params("price2"));
                return carService.getCarsInPriceRange(price1, price2);
            }), new JsonTransformer());
        });
    }
}
