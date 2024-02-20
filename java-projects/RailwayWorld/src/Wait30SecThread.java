public class Wait30SecThread extends Thread{
    public void run(){
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
