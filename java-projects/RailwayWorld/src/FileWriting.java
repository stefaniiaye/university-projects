import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriting extends Thread {
    List<Trainset> trainsets;
    public String fileName = "AppState.txt";

    public FileWriting(List<Trainset> trainsets){
        this.trainsets = trainsets;
    }

    public void run() {
        while (!Thread.interrupted()){
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
                for(Trainset trainset : trainsets){
                    while(trainset.locomotive.route == null){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                trainsets.sort((t1, t2) -> (int) (t2.locomotive.getDistance() - t1.locomotive.getDistance()));
                bufferedWriter.write("Trainsets in transit:\n\n");
                for(Trainset trainset : trainsets){
                    bufferedWriter.write(trainset.toString());
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                }
            } catch (IOException ex) {
                System.out.println("Something went wrong, file cannot be written.");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


