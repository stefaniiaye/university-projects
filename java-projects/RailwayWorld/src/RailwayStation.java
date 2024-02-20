import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RailwayStation {
    public String name;
    Map<RailwayStation,Integer> neighborStations;
    public Queue<Trainset> trainsetQueue;
    public boolean isLocked;

    public RailwayStation(String name){
        this.name = name;
        this.neighborStations = new HashMap<>();
        this.trainsetQueue = new LinkedList<>();
        this.isLocked = false;
    }

    public void addNeighborStation(RailwayStation station, int distance){
        neighborStations.put(station, distance);
    }

    public Map<RailwayStation, Integer> getNeighborStations(){
        return neighborStations;
    }

    @Override
    public String toString() {
        return "station " + name;
    }

    public synchronized void addTrainToQueue(Trainset train) {
        trainsetQueue.offer(train);
    }

    public synchronized void removeTrainFromQueue() {
        trainsetQueue.poll();
    }

    public synchronized Trainset getTrainFromQueue() {
        return trainsetQueue.peek();
    }

}
