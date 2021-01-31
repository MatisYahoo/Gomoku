package logic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Game {
    public boolean whiteColor = true;
    public boolean enemyTurn;
    public boolean readyToSend = false;
    public ConnectionHandler connectionThread;

    private int[][] board;

    private Main main;

    public String enemyMove;
    public String readyMove;
    public BooleanProperty enemyListener = new SimpleBooleanProperty();

    public Game() {
        board = new int[10][10];
        for (int i = 1; i < 10; i++)
            for (int j = 1; j < 10; j++) {
                board[i][j] = 0;
            }
    }

    public int onClick(String where) {
        int state = 0;
        int[] move = Decode.decodeMove(where);
        int i = move[0];
        int j = move[1];
        if (board[i][j] == 0) {
            board[i][j] = 1;
            state = 1;
        }
        readyMove = ':' + where;
        return state;
    }

    public void enemyMoved(String where) {

        int[] move = Decode.decodeMove(where);
        int i = move[0];
        int j = move[1];
        board[i][j] = 1;
        enemyMove = where;
        enemyListener.set(true);
    }


    public void setBlack( ) {
        this.whiteColor = false;
        this.enemyTurn = true;
    }

    public void gameEnd(String who) {
        String title;
        String content;
        if (who.equals("draw")) {
            title = "Błąd";
            content = "Nastąpił problem z połączeniem.";
        } else if (who.equals("white")) {
            content = "Zwyciężyły białe";
            if (whiteColor) title = "Zwycięstwo";
            else title = "Porażka";
        } else {
            content = "Zwyciężyły czarne";
            if (!whiteColor) title = "Zwycięstwo";
            else title = "Porażka";
        }
        connectionThread.stop = true;
        main.backToConnectionView(title, content);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setConnectionThread(ConnectionHandler connectionHandler) {
        this.connectionThread = connectionHandler;
    }
}
