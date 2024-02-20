public class RailroadHazard extends RuntimeException{
    public RailroadHazard(Trainset trainset){
        super("\nThis trainset exceeded maximum allowed speed, here is information about it:\n" + trainset);
    }

}
