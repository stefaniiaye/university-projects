public class RestaurantRRCar extends RailroadCar {
    int seatingCapacity;
    boolean pplAreServed;

    public RestaurantRRCar( int netWeight) {
        super( netWeight);
        this.reqElGrid = true;
        this.type = "Railroad restaurant car";
        this.seatingCapacity = (int)(Math.random()*71+50);
        this.pplAreServed = false;
    }
    public void servePpl(){
        if(!pplAreServed){
            this.pplAreServed = true;
            this.grossWeight += seatingCapacity*80;
            this.isEmpty = false;
            System.out.println("People are served and enjoying their meal.\n");
        }else System.out.println("Wait for other costumers to finish their meal.\n");
    }
    public void prepareForNextCustomers(){
        if(pplAreServed) {
            this.pplAreServed = false;
            this.grossWeight = this.netWeight;
            this.isEmpty = true;
            System.out.println("Restaurant is ready to serve new people.\n");
        }else System.out.println("Restaurant is already ready for new customers.\n");
    }
}
