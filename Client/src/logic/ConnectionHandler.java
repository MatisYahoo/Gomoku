package logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private Game game;
    private Socket socket;
    boolean stop;

    public ConnectionHandler(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
        this.stop = false;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            byte[] buffer = new byte[3];
            while (!stop) {
                if (game.enemyTurn) {
                    is.read(buffer);
                    if (buffer[0] == ':') {
                        game.enemyMoved(new String(buffer,1,2));
                        game.enemyTurn = false;
                    } else {
                        String info = new String(buffer,0,3);
                        if (info.equals("CON")) {
                            game.gameEnd("draw");
                        } else if (info.equals("WWI")) {
                            game.gameEnd("white");
                        } else if (info.equals("BWI")) {
                            game.gameEnd("black");
                        } else {
                            System.out.println("Otrzymano błędną wiadomość");
                        }
                    }
                } else if (game.readyToSend) {
                    os.write(game.readyMove.getBytes());
                    game.enemyTurn = true;
                    game.readyToSend = false;
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
