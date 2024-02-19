
public class App {
    public static void main(String[] args) {
        Thread menu = new Thread(new MenuLoop());
        menu.start();
    }
}
