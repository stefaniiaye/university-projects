public class PassengerRRCar extends RailroadCar {
    int numOfSeats;

    int pplNum = 0;

    public PassengerRRCar(int netWeight, int numOfSeats) {
        super(netWeight);
        this.numOfSeats = numOfSeats;
        this.reqElGrid = true;
        this.type = "Passenger railroad car";
    }

    public void loadPeople(){
        if(isEmpty) {
            pplNum = numOfSeats;
            this.grossWeight += pplNum * 80;
            this.isEmpty = false;
            System.out.println("Car is loaded with "+pplNum+" people.\n");
        }else System.out.println("Car is already loaded.\n");
    }
    public void unloadPpl(){
        if(!isEmpty) {
            this.pplNum = 0;
            this.grossWeight = this.netWeight;
            this.isEmpty = true;
            System.out.println("Car is unloaded and ready for new people.\n");
        }else System.out.println("Car is empty, you cannot unload it.\n");
    }

    @Override
    public String toString() {
        return super.toString()+ " (" +pplNum+ " people on board)";

    }
}
