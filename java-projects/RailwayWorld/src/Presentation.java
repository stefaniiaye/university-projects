public class Presentation {
    public static void main(String[] args) {
       /* //Firstly I would like that my trains are in transit, for that I am generating stations and trains
        Generators.generateStations();
        Generators.generateTrainsets();

        List<RailwayStation> railwayStations = Generators.generatedStations;
        List<Trainset> trainsets = Generators.generatedTrainsets;

        for (Thread trainset : trainsets) {
            trainset.start();

        }*/
        //Creating all types of cars and showing their methods

        PassengerRRCar passengerRRCar = new PassengerRRCar(5555,180);
        passengerRRCar.loadPeople();
        passengerRRCar.unloadPpl();

        PostOffRRCar postOffRRCar = new PostOffRRCar(6767);
        postOffRRCar.receiveMail();
        postOffRRCar.sendMail();
        postOffRRCar.sortMail();

        BaggageAndMailRRCar baggageAndMailRRCar = new BaggageAndMailRRCar(4444);
        baggageAndMailRRCar.loadBaggage();
        baggageAndMailRRCar.unloadBaggage();
        baggageAndMailRRCar.sortBaggage();

        RestaurantRRCar restaurantRRCar = new RestaurantRRCar(1111);
        restaurantRRCar.servePpl();
        restaurantRRCar.prepareForNextCustomers();

        BasicFreightRRCar basicFreightRRCar = new BasicFreightRRCar(7890);
        basicFreightRRCar.loadCar();
        basicFreightRRCar.unloadCar();

        HeavyFreightRRCar heavyFreightRRCar = new HeavyFreightRRCar(12345);
        heavyFreightRRCar.loadCar();
        heavyFreightRRCar.unloadCar();

        RefrigeratedRRCar refrigeratedRRCar = new RefrigeratedRRCar(456);
        refrigeratedRRCar.loadCar();

        LiquidMaterialsRRCar liquidMaterialsRRCar = new LiquidMaterialsRRCar(234);
        liquidMaterialsRRCar.loadLiquids();
        liquidMaterialsRRCar.checkIfHazardous();

        GaseousMaterialsRRCar gaseousMaterialsRRCar = new GaseousMaterialsRRCar(999);
        gaseousMaterialsRRCar.loadCar();
        gaseousMaterialsRRCar.checkLvlOfPressure();

        ExplosivesRRCar explosivesRRCar = new ExplosivesRRCar(1555);
        explosivesRRCar.loadCar();
        explosivesRRCar.checkLvlOfDanger();

        ToxicMaterialsRRCar toxicMaterialsRRCar = new ToxicMaterialsRRCar(345);
        toxicMaterialsRRCar.loadCar();

        LiquidToxicMaterialsRRCar liquidToxicMaterialsRRCar = new LiquidToxicMaterialsRRCar(478);
        liquidToxicMaterialsRRCar.loadLiquids();
        liquidToxicMaterialsRRCar.toHandle();

        //Creating two stations and connection between them

        RailwayStation railwayStation1 = new RailwayStation("Aboba");
        RailwayStation railwayStation2 = new RailwayStation("Bob");
        railwayStation1.addNeighborStation(railwayStation2,666);

        //Creating a locomotive and generating stations to pass list of them to locomotive to generate
        // source and destination stations

        Generators.generateStations();
        Locomotive locomotive = new Locomotive(railwayStation1, Generators.generatedStations);;
        locomotive.createRoute(railwayStation1,railwayStation2);

        //Creating a trainset and adding a few created stations to it and starting its thread

        Trainset trainset = new Trainset(locomotive);
        try {
            trainset.addNewRRCar(passengerRRCar);
        } catch (ImpossibleToAddRRCar e) {
            System.err.print(e.getMessage());
        }
        try {
            trainset.addNewRRCar(basicFreightRRCar);
        } catch (ImpossibleToAddRRCar e) {
            System.err.print(e.getMessage());
        }

        trainset.start();


    }
}
