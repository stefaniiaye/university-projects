public class GaseousMaterialsRRCar extends BasicFreightRRCar {
    String[] typesOfGases = {"liquefied petroleum gas", "ammonia", "chlorine", "carbon dioxide"};
    int levelOfPressure;
    public GaseousMaterialsRRCar( int netWeight) {
        super( netWeight);
        this.type = "Railroad car for gaseous materials";
        this.levelOfPressure = (int)(Math.random()*7+1);
    }
    public void loadCar(){
        if(isEmpty) {
            this.weightOfLoad = (int) (Math.random() * 2001 + 1000);
            this.grossWeight += weightOfLoad;
            this.isEmpty = false;
            this.loadType = typesOfGases[(int)(Math.random()*4)];
            System.out.println("Car is successfully loaded with "+weightOfLoad+" liters of "+loadType+".\n");
        }else System.out.println("Car is already loaded.\n");
    }
    public void checkLvlOfPressure(){
        if(levelOfPressure>5){
            System.out.println("Level of pressure is too high, call a specialist.\n");
        }else System.out.println("level of pressure is under control.\n");
    }

}
