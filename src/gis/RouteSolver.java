package gis;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RouteSolver {

    public class Route {
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

    public Route findMinRouteGreedy() {
        int n = roads.length;
        boolean[] visited = new boolean[n];
        Route route = new Route(new Integer[n], 0);
        route.cities[0] = 0;
        visited[0] = true;
        route.cities[n - 1] = n - 1;
        visited[n - 1] = true;
        for (int i = 1; i < n - 1; ++i) {
            int from = route.cities[i - 1];
            int to = findClosestUnvisitedNeighbor(from, visited);
            route.cities[i] = to;
            route.length += roads[from][to];
            visited[to] = true;
        }
        // + last road
        route.length += roads[route.cities[n - 2]][n - 1];
        return route;
    }

    private int findClosestUnvisitedNeighbor(int from, boolean[] visited) {
        int result = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < roads.length; ++i)
            if (i != from && !visited[i]) {
                int distance = roads[from][i];
                if (distance < minDistance) {
                    result = i;
                    minDistance = distance;
                }
            };
        return result;
    }

    private class WoodyNeighbor {
        public int insertionIndex;
        public int city;
        public int distanceToChain; // equals roads: prevCity->city + city->nextCity - prevCity->nextCity
    }

    public Route findMinRouteWoody() {
        int n = roads.length;
        List<Integer> route = new LinkedList<>();
        route.add(0);
        route.add(n - 1);
        int routeLength = roads[0][n - 1];
        List<Integer> unvisited = new LinkedList<>();
        for (int i = 1; i < n - 1; ++i)
            unvisited.add(i);
        while (!unvisited.isEmpty()) {
            WoodyNeighbor minNeighbor = new WoodyNeighbor();
            minNeighbor.distanceToChain = Integer.MAX_VALUE;
            for (int i = 0; i < route.size() - 1; ++i) {
                /* chain = a sequence of 2 cities within current route
                 * we want do find the "closest" unvisited city for each chain
                 * then we choose the closest one of them and include it within the corresponding chain*/
                WoodyNeighbor chainNeighbor = findClosestTriangleNeighbor(route.get(i), route.get(i + 1), unvisited);
                if (chainNeighbor.distanceToChain < minNeighbor.distanceToChain) {
                    minNeighbor = chainNeighbor;
                    minNeighbor.insertionIndex = i + 1; // place between current i and i + 1
                }
            }
            route.add(minNeighbor.insertionIndex, minNeighbor.city);
            routeLength += minNeighbor.distanceToChain;
            unvisited.remove((Object) minNeighbor.city); // need to delete the city name, not by index
        }
        return new Route(route.toArray(new Integer[route.size()]), routeLength);
    }

    private WoodyNeighbor findClosestTriangleNeighbor(int chainStart, int chainEnd, List<Integer> unvisited) {
        WoodyNeighbor minNeighbor = new WoodyNeighbor();
        minNeighbor.city = -1;
        for (int neighbor : unvisited) {
            int distance = roads[chainStart][neighbor] + roads[neighbor][chainEnd] - roads[chainStart][chainEnd];
            if (distance < minNeighbor.distanceToChain || minNeighbor.city < 0) {
                minNeighbor.city = neighbor;
                minNeighbor.distanceToChain = distance;
            }
        }
        return minNeighbor;
    }

    public Route findMinRouteSimply() {
        int n = roads.length;
        List<Integer> unvisited = new LinkedList<>();
        for (int i = 1; i < n - 1; ++i)
            unvisited.add(i);
        Route route = new Route(new Integer[n], 0);
        route.cities[0] = 0;
        route.cities[n - 1] = n - 1;
        Random random = new Random();
        for (int i = 1; i < n - 1; ++i) {
            int index = random.nextInt(unvisited.size());
            route.cities[i] = unvisited.remove(index);
            route.length += roads[route.cities[i - 1]][index];
        }
        route.length += roads[route.cities[n - 2]][n - 1];
        return route;
    }
}
