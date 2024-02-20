import java.util.ArrayList;
import java.util.List;


public class Trainset extends Thread {
    final int id;
    static int counter;
    public Locomotive locomotive;
    List<RailroadCar> railroadCarList;
    public int weightOfCars;
    public int numOfConnectedCars;
    public double distanceBehind = 0;
    public double distanceAB = 0;
    public double completedDistanceAB = 0;


    public Trainset(Locomotive locomotive) {
        this.locomotive = locomotive;
        this.railroadCarList = new ArrayList<>();
        this.id = ++counter;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted())   {
                    locomotive.changeSpeed();
                    speedCheck();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
    }
    public void delete(List<Trainset> trainsets){
        trainsets.remove(this);
        this.interrupt();
        for(Trainset t : this.locomotive.currentStation.trainsetQueue){
            if(t == this){
                this.locomotive.currentStation.removeTrainFromQueue();
            }
        }
    }

    public <T extends RailroadCar> void addNewRRCar(T railroadCar) throws ImpossibleToAddRRCar {
        if(railroadCarList.size() < locomotive.maxNumOfRRCars) {
            if (railroadCar.isEmpty) {
                if (weightOfCars+railroadCar.netWeight < locomotive.maxWeightOfTheTransportedLoad) {
                    if (railroadCar.reqElGrid) {
                        if (numOfConnectedCars < locomotive.maxNumOfRRCarsToConnectToElGrid) {

                            railroadCarList.add(railroadCar);
                            railroadCarList.sort((car1, car2) -> car1.grossWeight - car2.grossWeight);
                            weightOfCars += railroadCar.grossWeight;
                            numOfConnectedCars++;

                        } else throw new ImpossibleToAddRRCar();
                    } else {
                    railroadCarList.add(railroadCar);
                    railroadCarList.sort((car1, car2) -> car1.grossWeight - car2.grossWeight);
                    weightOfCars += railroadCar.grossWeight;}

                } else throw new ImpossibleToAddRRCar();

            }else {
                if (weightOfCars + railroadCar.grossWeight < locomotive.maxWeightOfTheTransportedLoad) {
                    if (railroadCar.reqElGrid) {
                        if (numOfConnectedCars < locomotive.maxNumOfRRCarsToConnectToElGrid) {

                            railroadCarList.add(railroadCar);
                            weightOfCars += railroadCar.grossWeight;
                            numOfConnectedCars++;

                        } else throw new ImpossibleToAddRRCar();
                    } else {
                        railroadCarList.add(railroadCar);
                        railroadCarList.sort((car1, car2) -> car1.grossWeight - car2.grossWeight);
                        weightOfCars += railroadCar.grossWeight;
                    }
                } else throw new ImpossibleToAddRRCar();
            }
            }
        else throw new ImpossibleToAddRRCar();
    }

    public void speedCheck() throws RailroadHazard{
        if (locomotive.speed > 200) {
            locomotive.speed = (Math.random()*31)+150;
            throw new RailroadHazard(this);
        }
    }

   public synchronized void move(){
        for(int i = 1;i < locomotive.route.size();i++){
            locomotive.currentStation = locomotive.route.get(i-1);
            //System.out.println("Trainset "+this.id+" is now on the station "+locomotive.currentStation.name+
              //     ", current speed is: "+String.format("%.2f", locomotive.speed));

                if(locomotive.currentStation.isLocked){
                    locomotive.currentStation.addTrainToQueue(this);
                    while(locomotive.currentStation.isLocked || locomotive.currentStation.getTrainFromQueue() != this){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    locomotive.currentStation.removeTrainFromQueue();
                }
                    locomotive.currentStation.isLocked = true;
                    double distanceBetweenTwoSt = locomotive.currentStation.getNeighborStations().get(locomotive.route.get(i));
                    distanceAB = locomotive.currentStation.getNeighborStations().get(locomotive.route.get(i));
                    while (distanceBetweenTwoSt > 0){
                        double a = distanceAB-distanceBetweenTwoSt;
                        completedDistanceAB = a;
                        distanceBetweenTwoSt -= locomotive.speed / 360;

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    this.distanceBehind += locomotive.currentStation.getNeighborStations().get(locomotive.route.get(i));
                    Wait2SecThread w2s = new Wait2SecThread();
                    w2s.start();
                    try {
                        w2s.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    locomotive.currentStation.isLocked = false;

        }
        locomotive.currentStation = locomotive.destinationStation;
      //System.out.println("Trainset "+this.id+"has reached it's destination station ("+locomotive.currentStation.name+").");
       Wait30SecThread w30s = new Wait30SecThread();
       w30s.start();
       try {
           w30s.join();
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
       }
    }


    @Override
    public void run() {
        while (!Thread.interrupted()) {
            locomotive.setSourceStation();
            locomotive.setDestinationStation();
            if (locomotive.currentStation.equals(locomotive.destinationStation)) {
                //System.out.println("Trainset " + this.id + "has reached it's destination station (" + locomotive.currentStation.name + ").");
                Wait30SecThread w30s = new Wait30SecThread();
                w30s.start();
                try {
                    w30s.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } else {
                locomotive.createRoute(locomotive.sourceStation, locomotive.destinationStation);
                if (!locomotive.sourceStation.equals(locomotive.homeStation)) {
                    List<RailwayStation> tmp = locomotive.route;
                    locomotive.createRoute(locomotive.homeStation, locomotive.sourceStation);
                    locomotive.route.remove(locomotive.route.size()-1);
                    locomotive.route.addAll(tmp);
                }

            move();}

            if (!locomotive.currentStation.equals(locomotive.homeStation)) {
                locomotive.createRoute(locomotive.currentStation, locomotive.homeStation);
                move();
            }


        }
    }

    @Override
    public String toString() {
        String list ="";
        for (RailroadCar railroadCar : railroadCarList) {
            list += railroadCar + "\n";
        }
        return "Trainset id: " + id + ", " + locomotive + ", attached cars:\n" +list+"Route length: "+ locomotive.getDistance();
    }

}