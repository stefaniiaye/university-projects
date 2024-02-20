public class BasicFreightRRCar extends RailroadCar {
    int weightOfLoad;
    String loadType;
    String[] goods = {"Coal", "Lumber", "Ore"};


    public BasicFreightRRCar( int netWeight) {
        super( netWeight);
        this.type = "Basic railroad freight car";
    }

    public void loadCar(){
        if(isEmpty) {
            this.weightOfLoad = (int) (Math.random() * 2001 + 1000);
            this.grossWeight += weightOfLoad;
            this.isEmpty = false;
            this.loadType = goods[(int)(Math.random()*3)];
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
