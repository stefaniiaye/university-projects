public class ToxicMaterialsRRCar extends HeavyFreightRRCar {
    String[] toxicMat = {"chemicals", "radioactive materials", "hazardous waste"};
    public ToxicMaterialsRRCar( int netWeight) {
        super( netWeight);
        this.type = "Railroad car for toxic materials";
    }
    public void loadCar(){
        if(isEmpty) {
            this.weightOfLoad = (int) (Math.random() * 1001 + 2000);
            this.grossWeight += weightOfLoad;
            this.isEmpty = false;
            this.loadType = toxicMat[(int)(Math.random()*3)];
            System.out.println("Car is successfully loaded with "+weightOfLoad+"kg of "+loadType+".\n");
        }else System.out.println("Car is already loaded.\n");
    }
}
