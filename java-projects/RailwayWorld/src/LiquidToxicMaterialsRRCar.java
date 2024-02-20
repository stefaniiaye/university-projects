public class LiquidToxicMaterialsRRCar extends ToxicMaterialsRRCar implements LiquidMaterials {
    String[] toxicLiquids = {"acids", "solvents", "pesticides"};
    boolean requireSpecialHandling;
    public LiquidToxicMaterialsRRCar( int netWeight) {
        super( netWeight);
        this.type = "Railroad car for liquid and toxic materials";
    }

    @Override
    public void loadLiquids() {
        if(isEmpty) {
            this.weightOfLoad = (int) (Math.random() * 2001 + 1000);
            this.grossWeight += weightOfLoad;
            this.isEmpty = false;
            this.loadType = toxicLiquids[(int)(Math.random()*3)];
            System.out.println("Car is successfully loaded with "+weightOfLoad+" liters of "+loadType+".\n");
        }else System.out.println("Car is already loaded.\n");
    }
    public void toHandle(){
        int bool = (int)(Math.random()*2+1);
        if(bool == 1){
            this.requireSpecialHandling = true;
        } else this.requireSpecialHandling = false;
        if(requireSpecialHandling){
            System.out.println("Car are successfully handled.\n");
        }else System.out.println("Car don't need to be handled.\n");
    }
}
