package DodgeballServer;

public class RunDodgeball {
    public static void main(String[] args) {
        GameManager manager = new GameManager();
        new Thread(manager).start();
    }
}
