public class LiquidMaterialsRRCar extends BasicFreightRRCar implements LiquidMaterials {
    String[] liquids = {"liquefied oxygen", "ethylene", "hydrogen chloride"};
    boolean isHazardous;
    public LiquidMaterialsRRCar( int netWeight) {
        super( netWeight);
        this.type = "Railroad car for liquid materials";
    }

    @Override
    public void loadLiquids() {
        if(isEmpty) {
            this.weightOfLoad = (int) (Math.random() * 2001 + 1000);
            this.grossWeight += weightOfLoad;
            this.isEmpty = false;
            this.loadType = liquids[(int)(Math.random()*3)];
            System.out.println("Car is successfully loaded with "+weightOfLoad+" liters of "+loadType+".\n");
        }else System.out.println("Car is already loaded.\n");
    }
    public void checkIfHazardous(){
        int bool = (int)(Math.random()*2+1);
        if(bool == 1){
            this.isHazardous = true;
        } else this.isHazardous = false;
        if(isHazardous){
            System.out.println("Liquid is hazardous, be careful.\n");
        }else System.out.println("Liquid is not hazardous, everything is fine.\n");
    }

}
