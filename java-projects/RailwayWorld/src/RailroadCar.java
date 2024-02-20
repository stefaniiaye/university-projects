public abstract class RailroadCar {
    public int id;
    static int counter = 0;
    protected boolean reqElGrid;
    int netWeight;
    int grossWeight;
    protected boolean isEmpty;
    public String type;

    public RailroadCar( int netWeight) {
        this.id = ++counter;
        this.reqElGrid = false;
        this.netWeight = netWeight;
        this.grossWeight = netWeight;
        this.isEmpty = true;
    }

    @Override
    public String toString() {
        return this.type +", weight: "+grossWeight+"(id: "+this.id+").";

    }
    public void delete(Trainset t){
        t.railroadCarList.remove(this);
    }
}
