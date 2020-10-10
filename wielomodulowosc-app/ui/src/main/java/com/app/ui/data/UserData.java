package com.app.ui.data;

import com.app.service.enums.SortItem;
import com.app.ui.exception.UserDataException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public final class UserData {

    private final static Scanner sc = new Scanner(System.in);

    public static String getString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public static int getInt(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an integer value");
        }
        return Integer.parseInt(line);
    }

    public static double getDouble(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an double value");
        }
        return Double.parseDouble(line);
    }

    public static boolean getBoolean(String message) {
        System.out.println(message + " Press 'y' to say yes");
        var line = sc.nextLine();
        return line.equalsIgnoreCase("y");
    }

    public static SortItem getSortItem() {
        var counter = new AtomicInteger(0);
        Arrays
                .stream(SortItem.values())
                .forEach(sortItem -> System.out.println(counter.incrementAndGet() + ". " + sortItem));
        int choice;
        do {
            choice = getInt("Choose sort item [1-" + SortItem.values().length + "]:");
        } while (choice < 1 || choice > SortItem.values().length);
        return SortItem.values()[choice - 1];
    }

    public static void close() {
        if (sc != null) {
            sc.close();
        }
    }
}
