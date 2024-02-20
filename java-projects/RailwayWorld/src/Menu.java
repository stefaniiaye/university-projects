import java.util.*;

public class Menu {
    public static void main(String[] args) {
        Generators.generateStations();
        Generators.generateTrainsets();

        List<RailwayStation> railwayStations = Generators.generatedStations;
        List<Trainset> trainsets = Generators.generatedTrainsets;

        for (Thread trainset : trainsets) {
            trainset.start();

        }

        FileWriting fw = new FileWriting(Generators.generatedTrainsets);
        fw.start();

        Map<Integer, RailroadCar> createdCars = new HashMap<>();
        Map<String, RailwayStation> createdStations = new HashMap<>();
        Map<Integer,PassengerRRCar> passengerRRCars= new HashMap<>();
        Map<Integer, BasicFreightRRCar> basicFreightRRCars = new HashMap<>();
        Map<Integer, HeavyFreightRRCar> heavyFreightRRCars = new HashMap<>();


        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        System.out.println("Welcome to railway system simulator!\n25 trains are already on their routes, why don't you make your own?");
        System.out.println("But it is not only one option for you to test, here are list of them, enjoy!");


        while (!quit) {
            System.out.println("1: Create new locomotive");
            System.out.println("2: Create new railroad car");
            System.out.println("3: Create new railway station");
            System.out.println("4: Create new connection between stations");
            System.out.println("5: Attach railroad car to a locomotive");
            System.out.println("6: Load people/cargo onto railroad cars");
            System.out.println("7: Display report about trainset");
            System.out.println("8: Remove an object");
            System.out.println("9: Quit\n");
            System.out.println("Enter number of functionality you want to try:");

            int option = scanner.nextInt();

            try {
                switch (option) {
                    case 1 -> {
                        //Create new locomotive
                        Locomotive locomotive = new Locomotive(railwayStations.get((int) (Math.random() * 100)), railwayStations);
                        System.out.println("Locomotive "+locomotive.id + " has been created, his home station is: " + locomotive.homeStation+".\n");
                        Trainset trainset = new Trainset(locomotive);
                        trainsets.add(trainset);
                        trainset.start();
                    }
                    case 2 -> {
                        //Create new railroad car
                        System.out.println("Here you also have got options which car to create, here are list of them:");
                        System.out.println("1: Passenger railroad car");
                        System.out.println("2: Railroad post office car");
                        System.out.println("3: Railroad baggage and mail car");
                        System.out.println("4: Railroad restaurant car");
                        System.out.println("5: Basic railroad freight car");
                        System.out.println("6: Heavy railroad freight car");
                        System.out.println("7: Refrigerated railroad car");
                        System.out.println("8: Railroad car for liquid materials");
                        System.out.println("9: Railroad car for gaseous materials");
                        System.out.println("10: Railroad car for explosives");
                        System.out.println("11: Railroad car for toxic materials");
                        System.out.println("12: Railroad car for liquid and toxic materials");
                        System.out.println("Enter your choice:");
                        int c = scanner.nextInt();
                        try{
                            switch (c) {
                                case 1 -> {
                                    PassengerRRCar car = new PassengerRRCar((int) (Math.random() * 7000 + 1000), (int) (Math.random() * 101 + 100));
                                    createdCars.put(car.id,car);
                                    passengerRRCars.put(car.id,car);
                                    System.out.println("Passenger railroad car has been created.");
                                }
                                case 2 -> {
                                    RailroadCar car = new PostOffRRCar((int) (Math.random() * 6000 + 1000));
                                    createdCars.put(car.id,car);
                                    System.out.println("Railroad post office car has been created.");
                                }
                                case 3 -> {
                                    RailroadCar car = new BaggageAndMailRRCar((int) (Math.random() * 3000 + 1000));
                                    createdCars.put(car.id,car);
                                    System.out.println("Railroad baggage and mail car has been created.");
                                }
                                case 4 -> {
                                    RailroadCar car = new RestaurantRRCar((int) (Math.random() * 3000 + 1000));
                                    createdCars.put(car.id,car);
                                    System.out.println("Railroad restaurant car has been created.");
                                }
                                case 5 -> {
                                    BasicFreightRRCar car = new BasicFreightRRCar((int) (Math.random() * 5000 + 1000));
                                    createdCars.put(car.id,car);
                                    basicFreightRRCars.put(car.id,car);
                                    System.out.println("Basic railroad freight car has been created.");
                                }
                                case 6 -> {
                                    HeavyFreightRRCar car = new HeavyFreightRRCar((int) (Math.random() * 9000 + 1000));
                                    createdCars.put(car.id,car);
                                    heavyFreightRRCars.put(car.id,car);
                                    System.out.println("Heavy railroad freight car has been created.");
                                }
                                case 7 -> {
                                    BasicFreightRRCar car = new RefrigeratedRRCar((int) (Math.random() * 3000 + 1000));
                                    createdCars.put(car.id,car);
                                    basicFreightRRCars.put(car.id,car);
                                    System.out.println("Refrigerated railroad car has been created.");
                                }
                                case 8 -> {
                                    BasicFreightRRCar car = new LiquidMaterialsRRCar((int) (Math.random() * 3000 + 1000));
                                    createdCars.put(car.id,car);
                                    basicFreightRRCars.put(car.id,car);
                                    System.out.println("Railroad car for liquid materials has been created.");
                                }
                                case 9 -> {
                                    BasicFreightRRCar car = new GaseousMaterialsRRCar((int) (Math.random() * 3000 + 1000));
                                    createdCars.put(car.id,car);
                                    basicFreightRRCars.put(car.id,car);
                                    System.out.println("Railroad car for gaseous materials has been created.");
                                }
                                case 10 -> {
                                    HeavyFreightRRCar car = new ExplosivesRRCar((int) (Math.random() * 3000 + 1000));
                                    createdCars.put(car.id,car);
                                    heavyFreightRRCars.put(car.id,car);
                                    System.out.println("Railroad car for explosives has been created.");
                                }
                                case 11 -> {
                                    HeavyFreightRRCar car = new ToxicMaterialsRRCar((int) (Math.random() * 3000 + 1000));
                                    createdCars.put(car.id,car);
                                    heavyFreightRRCars.put(car.id,car);
                                    System.out.println("Railroad car for toxic materials has been created.");
                                }
                                case 12 -> {
                                    HeavyFreightRRCar car = new LiquidToxicMaterialsRRCar((int) (Math.random() * 4000 + 1000));
                                    createdCars.put(car.id,car);
                                    heavyFreightRRCars.put(car.id,car);
                                    System.out.println("Railroad car for liquid and toxic materials has been created.");
                                }
                                default -> System.out.println("Invalid number, you don't have such option");

                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Currently you have created "+createdCars.size()+" cars.\n");
                    }
                    case 3 -> {
                        //Create new railway station
                        System.out.print("Enter the name for the new railway station: ");
                        String name = scanner.next();
                        RailwayStation station = new RailwayStation(name);
                        System.out.println("Railway station " + station.name+ " has been created");
                        createdStations.put(station.name, station);
                        Generators.generatedStations.add(station);
                        System.out.println("Currently you have created "+createdStations.size()+" stations.\n");
                    }
                    case 4 -> {
                        //Create new connection between stations
                        if(createdStations.size()>=2) {
                            System.out.println("Here is a list of stations that you have created:\n");
                            for(String key : createdStations.keySet()){
                                System.out.println(key);
                            }
                            System.out.println();
                            System.out.println("Which of them you would like to connect?");
                            System.out.println("Enter name of the first one:");
                            String name1 = scanner.next();
                            if(createdStations.containsKey(name1)) {
                                System.out.println("Now enter name for the second one:");
                                String name2 = scanner.next();
                                if(createdStations.containsKey(name2)){
                                    createdStations.get(name1).addNeighborStation(createdStations.get(name2),(int) (Math.random() * 7 + 4));
                                    for (int j = 0; j < 3 ; j++) {
                                        createdStations.get(name1).addNeighborStation(railwayStations.get((int) (Math.random() * 100)), (int) (Math.random() * 7 + 4));
                                        while (createdStations.get(name1).neighborStations.containsKey(createdStations.get(name1))) {
                                            createdStations.get(name1).neighborStations.remove(createdStations.get(name1));
                                            createdStations.get(name1).addNeighborStation(railwayStations.get((int) (Math.random() * 100)), (int) (Math.random() * 7 + 4));
                                        }
                                    }
                                    for (int j = 0; j < 3 ; j++) {
                                        createdStations.get(name2).addNeighborStation(railwayStations.get((int) (Math.random() * 100)), (int) (Math.random() * 7 + 4));
                                        while (createdStations.get(name2).neighborStations.containsKey(createdStations.get(name2))) {
                                            createdStations.get(name2).neighborStations.remove(createdStations.get(name2));
                                            createdStations.get(name2).addNeighborStation(railwayStations.get((int) (Math.random() * 100)), (int) (Math.random() * 7 + 4));
                                        }
                                    }
                                    System.out.println("Connection between "+ createdStations.get(name1)+" and "+createdStations.get(name2)+" has been created.\n");
                                }else System.out.println("Station with this name cannot be found, try again.\n");
                            }else System.out.println("Station with this name cannot be found, try again.\n");
                        }else System.out.println("You don't have enough stations to make a connection, create them first.\n");
                    }
                    case 5 -> {
                        //Attach railroad car to a locomotive
                        if(createdCars.size()>0) {
                            System.out.println("Firstly, you need to chose to which trainset you would like to connect a car.");
                            System.out.println("Currently you have got "+trainsets.size()+" trainsets.");
                            System.out.println("Enter the trainset identifier: ");
                            int id = scanner.nextInt();
                            Trainset trainset = null;
                            for (int i = 0;i < trainsets.size();i++){
                                if(trainsets.get(i).id == id){
                                    trainset = trainsets.get(i);
                                    break;
                                }
                            }
                            if(trainset != null){
                                System.out.println("Here is a list of cars that you have created with their id before equality sigh:\n");
                                System.out.println(createdCars.toString().replaceAll("[\\{\\}]", "")+"\n");
                                System.out.println("Enter id of car that you would like to attach to chosen trainset:");
                                int idc = scanner.nextInt();
                                if(createdCars.containsKey(idc)){
                                    trainset.addNewRRCar(createdCars.get(idc));
                                    createdCars.remove(idc);
                                    System.out.println("Car is successfully added.\n");
                                }else System.out.println("No such id, try again.\n");
                            }else System.out.println("There is no trainset with such id, try again.\n");
                        }else System.out.println("You don't have any cars created, there is nothing to attach.\n");
                    }
                    case 6 -> {
                        // Load people/cargo onto railroad cars
                        System.out.println("You can perform loading on three types of cars:");
                        System.out.println("1: Passenger railroad car");
                        System.out.println("2: Basic railroad freight car");
                        System.out.println("3: Heavy railroad freight car");
                        System.out.println("Enter your choice:");
                        int type = scanner.nextInt();
                        switch (type){
                            case 1 -> {
                                if(passengerRRCars.size()>=1) {
                                    System.out.println("Here is a list of passenger cars that you have created with their id before equality sigh:\n");
                                    System.out.println(passengerRRCars.toString().replaceAll("[\\{\\}]", "") + "\n");
                                    System.out.println("Enter id of car that you would like to load people to:");
                                    int id = scanner.nextInt();
                                    if(passengerRRCars.containsKey(id)){
                                        passengerRRCars.get(id).loadPeople();
                                    }else System.out.println("Wrong id, try again please.\n");
                                }else System.out.println("You don't have any passenger cars, create them first.\n");
                            }
                            case 2 -> {
                                if(basicFreightRRCars.size()>=1) {
                                    System.out.println("Here is a list of basic freight cars that you have created with their id before equality sigh:\n");
                                    System.out.println(basicFreightRRCars.toString().replaceAll("[\\{\\}]", "") + "\n");
                                    System.out.println("Enter id of car that you would like to load with cargo:");
                                    int id = scanner.nextInt();
                                    if(basicFreightRRCars.containsKey(id)){
                                        basicFreightRRCars.get(id).loadCar();
                                    }else System.out.println("Wrong id, try again please.\n");
                                }else System.out.println("You don't have any basic freight cars, create them first.\n");
                            }
                            case 3 -> {
                                if(heavyFreightRRCars.size()>=1) {
                                    System.out.println("Here is a list of heavy freight cars that you have created with their id before equality sigh:\n");
                                    System.out.println(heavyFreightRRCars.toString().replaceAll("[\\{\\}]", "") + "\n");
                                    System.out.println("Enter id of car that you would like to load with cargo:");
                                    int id = scanner.nextInt();
                                    if(heavyFreightRRCars.containsKey(id)){
                                        heavyFreightRRCars.get(id).loadCar();
                                    }else System.out.println("Wrong id, try again please.\n");
                                }else System.out.println("You don't have any heavy freight cars, create them first.\n");
                            }
                            default -> System.out.println("You don't have such option, try again please.\n");
                        }

                    }
                    case 7 -> {
                        //Display report about trainset
                        System.out.println("Currently you have got "+trainsets.size()+" trainsets.");
                        System.out.println("Enter the trainset identifier: ");
                        int id = scanner.nextInt();
                        Trainset trainset = null;
                        for (int i = 0;i < trainsets.size();i++){
                            if(trainsets.get(i).id == id){
                                trainset = trainsets.get(i);
                                break;
                            }
                        }
                        double percentage1 = 0;
                        double percentage2 = 0;
                        if (trainset != null) {
                            percentage1 = trainset.distanceBehind/trainset.locomotive.getDistance()*100;
                            System.out.println(trainset);
                            System.out.println(String.format("%.2f", percentage1) + "% of the distance completed.");
                            percentage2 = trainset.completedDistanceAB/trainset.distanceAB*100;
                            System.out.println(String.format("%.2f", percentage2) + "% of the distance completed between the nearest railway stations on route.\n");
                        }else System.out.println("Trainset cannot be found, try another id.\n");
                    }
                    case 8 -> {
                        //removal
                        System.out.println("What would you like to remove?");
                        System.out.println("1: Railroad car");
                        System.out.println("2: Trainset");
                        System.out.println("Enter your choice:");
                        int c = scanner.nextInt();
                        switch (c){
                           case 1 -> {
                               System.out.println("Currently you have got "+trainsets.size()+" trainsets.");
                               System.out.println("Enter identifier of a trainset you would like to delete car from: ");
                               int id = scanner.nextInt();
                               Trainset trainset = null;
                               for (int i = 0;i < trainsets.size();i++){
                                   if(trainsets.get(i).id == id){
                                       trainset = trainsets.get(i);
                                       break;
                                   }
                               }
                               if(trainset != null) {
                                   System.out.println("Here is a list of cars this trainset have attached:");
                                   for(RailroadCar car : trainset.railroadCarList){
                                       System.out.println(car);
                                   }
                                   System.out.println("Enter id of one you would like to delete:");
                                   int idc = scanner.nextInt();
                                   RailroadCar carToDelete = null;
                                   for(int i = 0;i<trainset.railroadCarList.size();i++){
                                       if(trainset.railroadCarList.get(i).id == idc){
                                           carToDelete = trainset.railroadCarList.get(i);
                                           break;
                                       }
                                   }
                                   if(carToDelete != null){
                                      carToDelete.delete(trainset);
                                       System.out.println("Car is successfully deleted.\n");
                                   }else System.out.println("Car cannot be found, try again please.\n");
                               }else System.out.println("Trainset cannot be found, try again.\n");


                           }
                           case 2 -> {
                               System.out.println("Currently you have got "+trainsets.size()+" trainsets.");
                               System.out.println("Enter identifier of a trainset you would like to delete: ");
                               int id = scanner.nextInt();
                               Trainset trainset = null;
                               for (int i = 0;i < trainsets.size();i++){
                                   if(trainsets.get(i).id == id){
                                       trainset = trainsets.get(i);
                                       break;
                                   }
                               }
                               if(trainset != null) {
                                  trainset.delete(trainsets);
                                   System.out.println("Trainset is successfully deleted.\n");
                               }else System.out.println("Trainset cannot be found, try again.\n");
                            }
                            default -> System.out.println("There is no such option, try again please.\n");
                        }
                    }
                    case 9 -> {
                        quit = true;
                    }
                    default -> System.out.println("There is no such option. Please try again!\n");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}