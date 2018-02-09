package gis;

import java.util.Arrays;

public class RouteSolver {

    public static class Route {
        public Integer[] cities;
        public int length;
        public Route(Integer[] cities, int length) {
            this.cities = Arrays.copyOf(cities, cities.length);
            this.length = length;
        }
    }

    private int[][] roads;

    public RouteSolver(int[][] roads) {
        this.roads = new int[roads.length][];
        for (int i = 0; i < roads.length; ++i) {
            this.roads[i] = Arrays.copyOf(roads[i], roads[i].length);
        }
    }

    public Route findMinRouteBruteforce() {
        int n = roads.length;
        Integer[] route = new Integer[n];
        for (int i = 0; i < n; ++i)
            route[i] = i;
        Route minRoute = new Route(route, calculateLength(route));
        Permutations<Integer> routePerms = new Permutations<>(route);
        do {
            int permLength = calculateLength(routePerms.get());
            if (permLength < minRoute.length) {
                minRoute.cities = routePerms.get();
                minRoute.length = permLength;
            }
        } while (routePerms.nextPermutation() != null);
        return minRoute;
    }

    public int calculateLength(Integer[] route) {
        int length = 0;
        for (int i = 0; i < route.length - 1; ++i) {
            int from = route[i];
            int to = route[i + 1];
            length += roads[from][to];
        }
        return length;
    }

    public Integer[] findMinRouteGreedy() {
        int n = roads.length;
        boolean[] visited = new boolean[n];
        Integer[] route = new Integer[n];
        route[0] = 0;
        visited[0] = true;
        route[n - 1] = n - 1;
        visited[n - 1] = true;
        for (int i = 1; i < n - 1; ++i) {
            //int dest = findClosestNeighbor(route[i - 1]);
            //route
        }
        return null;
    }
}
