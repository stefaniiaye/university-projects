public class HeavyFreightRRCar extends RailroadCar {
    int weightOfLoad;
    String loadType;
    String[] goods = {"heavy constructions", "heavy materials", "heavy equipment", "long pipes", "overweight machinery"};
    public HeavyFreightRRCar( int netWeight) {
        super(netWeight);
        this.type = "Heavy railroad freight car";
    }

    public void loadCar(){
        if(isEmpty) {
            this.weightOfLoad = (int) (Math.random() * 5001 + 2000);
            this.grossWeight += weightOfLoad;
            this.isEmpty = false;
            this.loadType = goods[(int)(Math.random()*5)];
            System.out.println("Car is successfully loaded with "+weightOfLoad+"kg of "+loadType+".\n");
        }else System.out.println("Car is already loaded.\n");
    }
    public void unloadCar(){
        if(!isEmpty) {
            this.isEmpty = true;
            this.grossWeight = this.netWeight;
            System.out.println("Car is empty now.\n");
        }else System.out.println("Car is already empty.\n");
    }

}
