import java.util.*;

public class Locomotive  {

    final RailwayStation homeStation;
    public RailwayStation sourceStation;
    public RailwayStation destinationStation;
    public RailwayStation currentStation;
    final int id;
    static int counter;
    public List<RailwayStation> route;
    public static List<RailwayStation> allPossibleStations;

    int maxNumOfRRCars;
    int maxWeightOfTheTransportedLoad;
    int maxNumOfRRCarsToConnectToElGrid;

    double speed;

    public Locomotive(RailwayStation homeStation, List<RailwayStation> railwayStations) {
        this.id = ++counter;
        this.homeStation = homeStation;
        allPossibleStations = railwayStations;

       this.speed = (Math.random()*31)+140;
       this.currentStation = homeStation;
       this.maxNumOfRRCars = 10;//(int)((Math.random()*3)+8);
       this.maxNumOfRRCarsToConnectToElGrid = 10;//(int)((Math.random()*3)+8);
       this.maxWeightOfTheTransportedLoad = (int)((Math.random()*60000)+500000);

    }


    public synchronized void changeSpeed() {
        if(Math.random() < 0.5) speed *= 1.03;
        else speed *= 0.97;
    }


    public void setDestinationStation() {
        this.destinationStation = allPossibleStations.get((int)(Math.random()*100));
    }


    public void setSourceStation() {
        this.sourceStation = allPossibleStations.get((int)(Math.random()*100));
    }


    @Override
    public String toString() {
        return "Locomotive id: " + id ;
    }

    public void createRoute (RailwayStation depart, RailwayStation arrive) {
        Map<RailwayStation, Integer> neighborsOfStation = new HashMap<>();
        Map<RailwayStation, RailwayStation> previousStations = new HashMap<>();
        Set<RailwayStation> visited = new HashSet<>();
        PriorityQueue<RailwayStation> queue = new PriorityQueue<>(Comparator.comparingInt(neighborsOfStation::get));


        for (RailwayStation station : depart.getNeighborStations().keySet()) {
            neighborsOfStation.put(station, depart.getNeighborStations().get(station));
            previousStations.put(station, depart);
            queue.offer(station);
        }

        neighborsOfStation.put(depart, 0);

        visited.add(depart);

        while (!queue.isEmpty()) {
            RailwayStation current = queue.poll();
            if (current == arrive) {
                break;
            }
            visited.add(current);
            for (RailwayStation neighbor : current.getNeighborStations().keySet()) {
                if (!visited.contains(neighbor)) {
                    int distance = neighborsOfStation.get(current) + current.getNeighborStations().get(neighbor);
                    if (!neighborsOfStation.containsKey(neighbor) || distance < neighborsOfStation.get(neighbor)) {
                        neighborsOfStation.put(neighbor, distance);
                        previousStations.put(neighbor, current);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        List<RailwayStation> route = new ArrayList<>();
        RailwayStation current = arrive;

        while (previousStations.containsKey(current)) {
            route.add(0, current);
            current = previousStations.get(current);
        }
        route.add(0, depart);

        this.route = route;
    }

    public double getDistance(){
        double distance = 0;
        for(int i = 1; i < route.size();i++){
            distance += route.get(i-1).neighborStations.get(route.get(i));
        } return distance;
    }


}
