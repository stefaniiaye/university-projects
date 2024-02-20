import java.util.ArrayList;
import java.util.List;

public class Generators {
    static String[] stationNames = {"Phoenix", "Mumbai", "Sydney",
            "Vancouver", "Berlin", "Johannesburg", "Seoul", "Istanbul", "Toronto",
            "Sao Paulo", "Paris", "London", "Kharkiv", "Buenos Aires", "Tokyo", "New York City",
            "Rio de Janeiro", "Cairo", "Bangkok", "Rome", "Shanghai", "Los Angeles", "Beijing",
            "Amsterdam", "Nairobi", "Jakarta", "New Delhi", "Madrid", "Chicago", "Kuala Lumpur",
            "Singapore City", "San Francisco", "Toronto", "Abu Dhabi", "Mexico City", "Houston",
            "Johannesburg", "Atlanta", "Vienna", "Miami", "Boston", "Seoul", "Prague", "Nairobi",
            "Santiago", "Warsaw", "Mumbai", "Budapest", "Stockholm", "Copenhagen", "Montreal",
            "Lima", "Edinburgh", "Brussels", "Frankfurt", "Manila", "Zurich", "Helsinki", "Lisbon",
            "Riyadh", "Oslo", "Warsaw", "Bogota", "Hanoi", "Athens", "Wellington", "Caracas", "Tallinn",
            "Reykjavik", "Bucharest", "Vilnius", "Kuwait City", "Skopje", "Beirut", "Riga", "Sofia",
            "Ljubljana", "Tbilisi", "Astana", "Podgorica", "Yerevan", "Baku", "Tashkent", "Ashgabat",
            "Dushanbe", "Nur-Sultan", "Amman", "Damascus", "Sana'a", "Kabul", "Beirut", "Baghdad",
            "Abuja", "Luanda", "Kinshasa", "Johannesburg", "Nairobi", "Accra", "Dakar", "Abidjan"};
    public static List<RailwayStation> generatedStations;
    public static List<Trainset> generatedTrainsets;

    public static void generateStations(){
        List<RailwayStation> railwayStations = new ArrayList<>();
        for(int i = 0;i<100;i++){
            railwayStations.add(new RailwayStation(stationNames[i]));
        }

        for (RailwayStation railwayStation : railwayStations) {
            for (int j = 0; j < 5 ; j++) {
                railwayStation.addNeighborStation(railwayStations.get((int) (Math.random() * 100)), (int) (Math.random() * 7 + 4));
                while (railwayStation.neighborStations.containsKey(railwayStation)) {
                    railwayStation.neighborStations.remove(railwayStation);
                    railwayStation.addNeighborStation(railwayStations.get((int) (Math.random() * 100)), (int) (Math.random() * 7 + 4));
                }
            }
        }

       generatedStations = railwayStations;
    }

    public static void generateTrainsets() {
        List<Trainset> trainsets = new ArrayList<>();
        for(int i = 0;i < 25; i++){
            trainsets.add(new Trainset(new Locomotive(generatedStations.get((int)(Math.random()*100)),generatedStations)));
            int numOfCars = (int)(Math.random()*6+5);
            for(int k =0; k<numOfCars; k++){
                int typeCar = (int)(Math.random()*12+1);
                try{
                switch (typeCar) {
                    case 1 -> trainsets.get(i).addNewRRCar(new PassengerRRCar((int) (Math.random() * 7000 + 1000), (int) (Math.random() * 101 + 100)));
                    case 2 -> trainsets.get(i).addNewRRCar(new PostOffRRCar((int) (Math.random() * 6000 + 1000)));
                    case 3 -> trainsets.get(i).addNewRRCar(new BaggageAndMailRRCar((int) (Math.random() * 3000 + 1000)));
                    case 4 -> trainsets.get(i).addNewRRCar(new RestaurantRRCar((int) (Math.random() * 3000 + 1000)));
                    case 5 -> trainsets.get(i).addNewRRCar(new BasicFreightRRCar((int) (Math.random() * 5000 + 1000)));
                    case 6 -> trainsets.get(i).addNewRRCar(new HeavyFreightRRCar((int) (Math.random() * 9000 + 1000)));
                    case 7 -> trainsets.get(i).addNewRRCar(new RefrigeratedRRCar((int) (Math.random() * 3000 + 1000)));
                    case 8 -> trainsets.get(i).addNewRRCar(new LiquidMaterialsRRCar((int) (Math.random() * 3000 + 1000)));
                    case 9 -> trainsets.get(i).addNewRRCar(new GaseousMaterialsRRCar((int) (Math.random() * 3000 + 1000)));
                    case 10 -> trainsets.get(i).addNewRRCar(new ExplosivesRRCar((int) (Math.random() * 3000 + 1000)));
                    case 11 -> trainsets.get(i).addNewRRCar(new ToxicMaterialsRRCar((int) (Math.random() * 3000 + 1000)));
                    case 12 -> trainsets.get(i).addNewRRCar(new LiquidToxicMaterialsRRCar((int) (Math.random() * 4000 + 1000)));
                }

                }catch (ImpossibleToAddRRCar e){
                    System.err.println(e.getMessage());
                }
            }
        }
        generatedTrainsets = trainsets;
    }






}
