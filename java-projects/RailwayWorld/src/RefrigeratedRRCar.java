public class RefrigeratedRRCar extends BasicFreightRRCar {
    String[] goodsR = {"fresh fruits and vegetables", "dairy products", "meats"};
    public RefrigeratedRRCar( int netWeight) {
        super( netWeight);
        this.reqElGrid = true;
        this.type = "Refrigerated railroad car";
    }
    public void loadCar(){
        if(isEmpty) {
            this.weightOfLoad = (int) (Math.random() * 2001 + 1000);
            this.grossWeight += weightOfLoad;
            this.isEmpty = false;
            this.loadType = goodsR[(int)(Math.random()*3)];
            System.out.println("Car is successfully loaded with "+weightOfLoad+"kg of "+loadType+".\n");
        }else System.out.println("Car is already loaded.\n");
    }
}
