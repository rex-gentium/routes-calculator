package gis;

import gis.RouteSolver.Route;

import java.util.Arrays;

public class Main {

    static final int[][] roads = new int[][]{
            { 0, 112, 184, 281, 344, 199, 317 },
            { 112, 0, 78, 175, 238, 87, 211 },
            { 184, 78, 0, 97, 160, 34, 133 },
            { 281, 175, 97, 0, 63, 131, 36 },
            { 344, 238, 160, 63, 0, 194, 27 },
            { 199, 87, 34, 131, 194, 0, 167 },
            { 317, 211, 133, 36, 27, 167, 0 },
    };

    static final int[][] roads14 = new int[][]{
            { 0, 112, 184, 281, 344, 199, 317, 416, 219, 24, 142, 90, 457, 315 },
            { 112, 0, 78, 175, 238, 87, 211, 336, 113, 93, 30, 34, 351, 209 },
            { 184, 78, 0, 97, 160, 34, 133, 258, 35, 165, 48, 106, 273, 131 },
            { 281, 175, 97, 0, 63, 131, 36, 285, 62, 262, 145, 203, 300, 158 },
            { 344, 238, 160, 63, 0, 194, 27, 348, 125, 325, 208, 266, 363, 221 },
            { 199, 87, 34, 131, 194, 0, 167, 292, 69, 180, 57, 121, 307, 165 },
            { 317, 211, 133, 36, 27, 167, 0, 321, 98, 298, 181, 239, 336, 194 },
            { 416, 336, 258, 285, 348, 292, 321, 0, 223, 423, 306, 364, 272, 312 },
            { 219, 113, 35, 62, 125, 69, 98, 223, 0, 200, 83, 141, 238, 96 },
            { 24, 93, 165, 262, 325, 180, 298, 423, 200, 0, 123, 71, 438, 296 },
            { 142, 30, 48, 145, 208, 57, 181, 306, 83, 123, 0, 64, 321, 179 },
            { 90, 34, 106, 203, 266, 121, 239, 364, 141, 71, 64, 0, 379, 237 },
            { 457, 351, 273, 300, 363, 307, 336, 272, 238, 438, 321, 379, 0, 142 },
            { 315, 209, 131, 158, 221, 65, 194, 312, 96, 296, 179, 237, 142 , 0 }
    };

    public static void main(String[] args) {
        RouteSolver routeSolver = new RouteSolver(roads);

        long time = System.currentTimeMillis();
        Route routeBruteforce = routeSolver.findMinRouteBruteforce();
        printRoute(routeBruteforce, System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        Route routeGreedy = routeSolver.findMinRouteGreedy();
        printRoute(routeGreedy, System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        Route routeWoody = routeSolver.findMinRouteWoody();
        printRoute(routeWoody, System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        Route routeRandom = routeSolver.findMinRouteSimply();
        printRoute(routeRandom, System.currentTimeMillis() - time);

        routeSolver = new RouteSolver(roads14);

        time = System.currentTimeMillis();
        routeGreedy = routeSolver.findMinRouteGreedy();
        printRoute(routeGreedy, System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        routeWoody = routeSolver.findMinRouteWoody();
        printRoute(routeWoody, System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        routeRandom = routeSolver.findMinRouteSimply();
        printRoute(routeRandom, System.currentTimeMillis() - time);

        improveRoute(routeSolver, routeRandom, 2);
        printRoute(routeRandom, 0);
        improveRoute(routeSolver, routeRandom, 3);
        printRoute(routeRandom, 0);
    }

    public static void printRoute(Route route, long time) {
        for (int i = 0; i < route.cities.length; ++i)
            System.out.print(route.cities[i] + " ");
        System.out.println();
        System.out.println("Route length: " + route.length);
        System.out.println("Execution time: " + time);
    }

    public static void improveRoute(RouteSolver lengthProvider, Route route, int windowWidth) {
        for (int i = 1; i < route.cities.length - 1 - windowWidth; ++i) {
            Integer[] window = Arrays.copyOfRange(route.cities, i, i + windowWidth);
            Permutations<Integer> permutation = new Permutations<>(window);
            while (permutation.nextPermutation() != null) {
                Integer[] newRoute = Arrays.copyOf(route.cities, route.cities.length);
                for (int j = 0; j < windowWidth; ++j)
                    newRoute[i + j] = permutation.get()[j];
                int newLength = lengthProvider.calculateLength(route.cities);
                if (newLength < route.length) {
                    route.cities = newRoute;
                    route.length = newLength;
                }
            }
        }
    }
}
