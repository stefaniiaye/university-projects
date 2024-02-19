public class BonusProbabilityThread extends Thread{
    @Override
    public void run() {
        while (!Thread.interrupted()){
            double prob = Math.random();
            if(prob<=0.25){
                Game.bonuses.add((int)(Math.random()*5+1));
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.print(e.getMessage());
            }
        }
    }
}
