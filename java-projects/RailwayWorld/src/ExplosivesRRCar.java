public class ExplosivesRRCar extends HeavyFreightRRCar {
    String[] types = {"mechanical", "nuclear", "chemical"};
    int levelOfDanger;

    public ExplosivesRRCar( int netWeight) {
        super( netWeight);
        this.type = "Railroad car for explosives";
        this.levelOfDanger = (int)(Math.random()*10+1);
    }

    public void loadCar(){
        if(isEmpty) {
            this.weightOfLoad = (int) (Math.random() * 3001 +1000);
            this.grossWeight += weightOfLoad;
            this.isEmpty = false;
            this.loadType = types[(int)(Math.random()*3)];
            System.out.println("Car is successfully loaded with "+loadType+" type of explosives.\n");
        }else System.out.println("Car is already loaded.\n");
    }
    public void checkLvlOfDanger(){
        if(levelOfDanger>7){
            System.out.println("This car requires some extra protection.\n");
        }else System.out.println("Level of danger within the normal range.\n");
    }


}
