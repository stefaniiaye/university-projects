import java.util.ArrayList;
import java.util.List;

public class BaggageAndMailRRCar extends RailroadCar {
    List<Baggage> baggageList = new ArrayList<>();

    class Baggage{
        int weight;
        String category;
        String[] categories = {"Ordinary","Fragile","Very fragile"};
        public Baggage(){
            this.weight = (int)(Math.random()*11+10);
            this.category = categories[(int)(Math.random()*3)];
        }

    }

    public BaggageAndMailRRCar( int netWeight) {
        super( netWeight);
        this.type = "Railroad baggage and mail car";

    }
    public void sortBaggage(){
        baggageList.sort((b1, b2) -> (int) (b1.weight- b2.weight));
        System.out.println("Baggage are sorted.\n");
    }

    public void loadBaggage(){
        isEmpty = false;
        int numOfItems = (int)(Math.random()*31+20);
        for(int i =0;i<=numOfItems;i++){
            baggageList.add(new Baggage());
        }
        for(Baggage b : baggageList){
            this.grossWeight += b.weight;
        }

        System.out.println("Baggage are successfully loaded with "+baggageList.size()+" bags.\n");
    }
    public void unloadBaggage(){
        isEmpty = true;
        this.grossWeight = this.netWeight;
        baggageList.clear();
        System.out.println("Baggage successfully unloaded.\n");
    }
}
