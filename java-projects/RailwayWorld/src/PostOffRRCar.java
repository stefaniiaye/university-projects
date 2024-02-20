import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostOffRRCar extends RailroadCar {

    List<MailItem> listOfMail = new ArrayList<>();
    String postCode;

    class MailItem {
        int id;
        static int counter;
        String destination;
        int weight;
        boolean isPacked;

        public MailItem() {
            this.id = ++counter;
            this.destination = Generators.stationNames[(int)(Math.random()*100)];
            this.weight = (int)(Math.random()*10+3);
            this.isPacked = false;
        }
        public void checkWeight(){
            if(weight>=10){
                packItem();
            }else this.weight = (int)(Math.random()*10+3);
        }
        public void packItem(){
            this.isPacked = true;
        }
    }
    public PostOffRRCar( int netWeight) {
        super( netWeight);
        this.reqElGrid = true;
        this.type = "Railroad post office car";
        this.postCode = "PC"+(int)(Math.random()*8889+1111);
    }

    public void sortMail(){
        Collections.sort(listOfMail,new Comparator<MailItem>() {
            @Override
            public int compare(MailItem mail1, MailItem mail2) {
                return mail1.destination.compareTo(mail2.destination);
            }
        });
        System.out.println("Mail are sorted by destinations.\n");
    }
    public void receiveMail(){
        isEmpty = false;
        int numOfItems = (int)(Math.random()*6+5);
        for(int i =0;i<=numOfItems;i++){
            listOfMail.add(new MailItem());
        }
            for(MailItem m:listOfMail) {
                m.checkWeight();
                if(m.isPacked) {
                    this.grossWeight += m.weight;
                }
            }
            System.out.println("All mail are successfully received by post number "+this.postCode+".\n");
    }
    public void sendMail(){
        isEmpty = true;
        this.grossWeight = this.netWeight;
        listOfMail.clear();
        System.out.println("Mail are sent by post number "+this.postCode+".\n");
    }

}
