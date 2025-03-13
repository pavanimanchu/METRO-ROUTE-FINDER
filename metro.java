import java.util.*;

class MetroMap {
    private final Map<String, Map<String, Integer>> graph;

    public MetroMap() {
        graph = new HashMap<>();
    }

    public void addStation(String station) {
        graph.putIfAbsent(station, new HashMap<>());
    }

    public void addConnection(String station1, String station2, int distance) {
        addStation(station1);
        addStation(station2);
        graph.get(station1).put(station2, distance);
        graph.get(station2).put(station1, distance); // Bidirectional
    }

    public void dijkstra(String start, String end) {
        PriorityQueue<Node> minHeap = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String station : graph.keySet()) {
            distances.put(station, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        minHeap.add(new Node(start, 0));

        while (!minHeap.isEmpty()) {
            Node current = minHeap.poll();
            String currentStation = current.station;

            if (!visited.add(currentStation)) continue;
            if (currentStation.equals(end)) break;

            for (Map.Entry<String, Integer> neighbor : graph.get(currentStation).entrySet()) {
                String nextStation = neighbor.getKey();
                int newDist = distances.get(currentStation) + neighbor.getValue();

                if (newDist < distances.get(nextStation)) {
                    distances.put(nextStation, newDist);
                    previous.put(nextStation, currentStation);
                    minHeap.add(new Node(nextStation, newDist));
                }
            }
        }

        printResult(previous, start, end, distances.get(end));
    }

    private void printResult(Map<String, String> previous, String start, String end, int totalDistance) {
        List<String> path = new ArrayList<>();
        String current = end;

        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        if (!path.get(0).equals(start)) {
            System.out.println("No path found from " + start + " to " + end + ".");
            return;
        }

        // Print path with 🚆 emoji
        System.out.print("Shortest path: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) System.out.print("  "); // Add train emoji between stations
        }
        System.out.println();
        System.out.println("Total Distance: " + totalDistance + " km");
    }

    static class Node {
        String station;
        int distance;

        Node(String station, int distance) {
            this.station = station;
            this.distance = distance;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MetroMap metro = new MetroMap();

        System.out.print("Enter the number of connections: ");
        int numConnections = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numConnections; i++) {
            System.out.print("Enter station1, station2, and distance (space-separated): ");
            String[] input = scanner.nextLine().split(" ");
            metro.addConnection(input[0], input[1], Integer.parseInt(input[2]));
        }

        System.out.print("Enter the source station: ");
        String source = scanner.nextLine();

        System.out.print("Enter the destination station: ");
        String destination = scanner.nextLine();

        metro.dijkstra(source, destination);
        
        scanner.close();
    }
}

