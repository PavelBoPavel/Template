package com.epam;

import com.epam.api.GpsNavigator;
import com.epam.api.Path;
import com.epam.impl.Stub2GpsNavigator;

/**
 * This class app demonstrates how your implementation of {@link com.epam.api.GpsNavigator} is intended to be used.
 */
public class ExampleApp {

    public static void main(String[] args) {
        final GpsNavigator navigator = new Stub2GpsNavigator();
        navigator.readData("C:\\gps\\road_map.ext");

        final Path path = navigator.findPath("C", "D");
        if (path != null) {
            System.out.println(path);
        }else {
            System.out.println("Единственный кратчайший путь не найден");
        }
    }
}