package logic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Pane a1pane, a2pane, a3pane, a4pane, a5pane, a6pane, a7pane, a8pane, a9pane,
            b1pane, b2pane, b3pane, b4pane, b5pane, b6pane, b7pane, b8pane, b9pane,
            c1pane, c2pane, c3pane, c4pane, c5pane, c6pane, c7pane, c8pane, c9pane,
            d1pane, d2pane, d3pane, d4pane, d5pane, d6pane, d7pane, d8pane, d9pane,
            e1pane, e2pane, e3pane, e4pane, e5pane, e6pane, e7pane, e8pane, e9pane,
            f1pane, f2pane, f3pane, f4pane, f5pane, f6pane, f7pane, f8pane, f9pane,
            g1pane, g2pane, g3pane, g4pane, g5pane, g6pane, g7pane, g8pane, g9pane,
            h1pane, h2pane, h3pane, h4pane, h5pane, h6pane, h7pane, h8pane, h9pane,
            i1pane, i2pane, i3pane, i4pane, i5pane, i6pane, i7pane, i8pane, i9pane;

    public ImageView a1, a2, a3, a4, a5, a6, a7, a8, a9,
            b1, b2, b3, b4, b5, b6, b7, b8, b9,
            c1, c2, c3, c4, c5, c6, c7, c8, c9,
            d1, d2, d3, d4, d5, d6, d7, d8, d9,
            e1, e2, e3, e4, e5, e6, e7, e8, e9,
            f1, f2, f3, f4, f5, f6, f7, f8, f9,
            g1, g2, g3, g4, g5, g6, g7, g8, g9,
            h1, h2, h3, h4, h5, h6, h7, h8, h9,
            i1, i2, i3, i4, i5, i6, i7, i8, i9;

    public Label yourNick;
    public Label enemyNick;
	public Label whoStarts;

    public String yourStone;
    public String enemyStone;

    private Game game = new Game();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game.enemyListener.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                drawStone(game.enemyMove, enemyStone);
                game.enemyListener.set(false);
            }
        });
    }

    public void startGame(String color, Main main) {
        if (color.equals("BLA")) {
            game.setBlack();
        }
        game.setMain(main);
        String you;
        String enemy;
        if (game.whiteColor) {
            you = "white";
            enemy = "black";
            yourStone = "file:images/"+you+".png";
            enemyStone = "file:images/"+enemy+".png";
            whoStarts.setText("Zaczynasz");
        }
        else {
            you = "black";
            enemy = "white";
            yourStone = "file:images/"+you+".png";
            enemyStone = "file:images/"+enemy+".png";
            whoStarts.setText("Przeciwnik zaczyna");
        }
    }

    public void clickOn(String where) {
        if(!game.enemyTurn) {
            int wyn = game.onClick(where);
            switch (wyn) {
                case 0: //Wybrano pole z kamieniem
                    System.out.println("Bledny ruch");
                    break;
                case 1: //Wybrano poprawne pole
                    drawStone(where, yourStone);
                    game.readyToSend = true;
                    break;
            }
        }
    }

    public void setConnectionThread(Socket socket) {
        ConnectionHandler connectionHandler = new ConnectionHandler(socket, game);
        Thread connectionThread = new Thread(connectionHandler);
        connectionThread.start();
        game.setConnectionThread(connectionHandler);
    }

    @FXML
    public void setNicks(String you, String enemy) {
        yourNick.setText(you);
        enemyNick.setText(enemy);
    }

    public void drawStone(String field, String stone) {
        switch (field) {
            case "A1": {
                a1.setImage(new Image(stone));
                break;
            }
            case "B1": {
                b1.setImage(new Image(stone));
                break;
            }
            case "C1": {
                c1.setImage(new Image(stone));
                break;
            }
            case "D1": {
                d1.setImage(new Image(stone));
                break;
            }
            case "E1": {
                e1.setImage(new Image(stone));
                break;
            }
            case "F1": {
                f1.setImage(new Image(stone));
                break;
            }
            case "G1": {
                g1.setImage(new Image(stone));
                break;
            }
            case "H1": {
                h1.setImage(new Image(stone));
                break;
            }
            case "I1": {
                i1.setImage(new Image(stone));
                break;
            }
            case "A2": {
                a2.setImage(new Image(stone));
                break;
            }
            case "B2": {
                b2.setImage(new Image(stone));
                break;
            }
            case "C2": {
                c2.setImage(new Image(stone));
                break;
            }
            case "D2": {
                d2.setImage(new Image(stone));
                break;
            }
            case "E2": {
                e2.setImage(new Image(stone));
                break;
            }
            case "F2": {
                f2.setImage(new Image(stone));
                break;
            }
            case "G2": {
                g2.setImage(new Image(stone));
                break;
            }
            case "H2": {
                h2.setImage(new Image(stone));
                break;
            }
            case "I2": {
                i2.setImage(new Image(stone));
                break;
            }
            case "A3": {
                a3.setImage(new Image(stone));
                break;
            }
            case "B3": {
                b3.setImage(new Image(stone));
                break;
            }
            case "C3": {
                c3.setImage(new Image(stone));
                break;
            }
            case "D3": {
                d3.setImage(new Image(stone));
                break;
            }
            case "E3": {
                e3.setImage(new Image(stone));
                break;
            }
            case "F3": {
                f3.setImage(new Image(stone));
                break;
            }
            case "G3": {
                g3.setImage(new Image(stone));
                break;
            }
            case "H3": {
                h3.setImage(new Image(stone));
                break;
            }
            case "I3": {
                i3.setImage(new Image(stone));
                break;
            }
            case "A4": {
                a4.setImage(new Image(stone));
                break;
            }
            case "B4": {
                b4.setImage(new Image(stone));
                break;
            }
            case "C4": {
                c4.setImage(new Image(stone));
                break;
            }
            case "D4": {
                d4.setImage(new Image(stone));
                break;
            }
            case "E4": {
                e4.setImage(new Image(stone));
                break;
            }
            case "F4": {
                f4.setImage(new Image(stone));
                break;
            }
            case "G4": {
                g4.setImage(new Image(stone));
                break;
            }
            case "H4": {
                h4.setImage(new Image(stone));
                break;
            }
            case "I4": {
                i4.setImage(new Image(stone));
                break;
            }
            case "A5": {
                a5.setImage(new Image(stone));
                break;
            }
            case "B5": {
                b5.setImage(new Image(stone));
                break;
            }
            case "C5": {
                c5.setImage(new Image(stone));
                break;
            }
            case "D5": {
                d5.setImage(new Image(stone));
                break;
            }
            case "E5": {
                e5.setImage(new Image(stone));
                break;
            }
            case "F5": {
                f5.setImage(new Image(stone));
                break;
            }
            case "G5": {
                g5.setImage(new Image(stone));
                break;
            }
            case "H5": {
                h5.setImage(new Image(stone));
                break;
            }
            case "I5": {
                i5.setImage(new Image(stone));
                break;
            }
            case "A6": {
                a6.setImage(new Image(stone));
                break;
            }
            case "B6": {
                b6.setImage(new Image(stone));
                break;
            }
            case "C6": {
                c6.setImage(new Image(stone));
                break;
            }
            case "D6": {
                d6.setImage(new Image(stone));
                break;
            }
            case "E6": {
                e6.setImage(new Image(stone));
                break;
            }
            case "F6": {
                f6.setImage(new Image(stone));
                break;
            }
            case "G6": {
                g6.setImage(new Image(stone));
                break;
            }
            case "H6": {
                h6.setImage(new Image(stone));
                break;
            }
            case "I6": {
                i6.setImage(new Image(stone));
                break;
            }
            case "A7": {
                a7.setImage(new Image(stone));
                break;
            }
            case "B7": {
                b7.setImage(new Image(stone));
                break;
            }
            case "C7": {
                c7.setImage(new Image(stone));
                break;
            }
            case "D7": {
                d7.setImage(new Image(stone));
                break;
            }
            case "E7": {
                e7.setImage(new Image(stone));
                break;
            }
            case "F7": {
                f7.setImage(new Image(stone));
                break;
            }
            case "G7": {
                g7.setImage(new Image(stone));
                break;
            }
            case "H7": {
                h7.setImage(new Image(stone));
                break;
            }
            case "I7": {
                i7.setImage(new Image(stone));
                break;
            }
            case "A8": {
                a8.setImage(new Image(stone));
                break;
            }
            case "B8": {
                b8.setImage(new Image(stone));
                break;
            }
            case "C8": {
                c8.setImage(new Image(stone));
                break;
            }
            case "D8": {
                d8.setImage(new Image(stone));
                break;
            }
            case "E8": {
                e8.setImage(new Image(stone));
                break;
            }
            case "F8": {
                f8.setImage(new Image(stone));
                break;
            }
            case "G8": {
                g8.setImage(new Image(stone));
                break;
            }
            case "H8": {
                h8.setImage(new Image(stone));
                break;
            }
            case "I8": {
                i8.setImage(new Image(stone));
                break;
            }
            case "A9": {
                a9.setImage(new Image(stone));
                break;
            }
            case "B9": {
                b9.setImage(new Image(stone));
                break;
            }
            case "C9": {
                c9.setImage(new Image(stone));
                break;
            }
            case "D9": {
                d9.setImage(new Image(stone));
                break;
            }
            case "E9": {
                e9.setImage(new Image(stone));
                break;
            }
            case "F9": {
                f9.setImage(new Image(stone));
                break;
            }
            case "G9": {
                g9.setImage(new Image(stone));
                break;
            }
            case "H9": {
                h9.setImage(new Image(stone));
                break;
            }
            case "I9": {
                i9.setImage(new Image(stone));
                break;
            }
        }
    }

    public void clickA1( ) {
        clickOn("A1");
    }
    public void clickB1( ) {
        clickOn("B1");
    }
    public void clickC1( ) {
        clickOn("C1");
    }
    public void clickD1( ) {
        clickOn("D1");
    }
    public void clickE1( ) {
        clickOn("E1");
    }
    public void clickF1( ) {
        clickOn("F1");
    }
    public void clickG1( ) {
        clickOn("G1");
    }
    public void clickH1( ) {
		clickOn("H1");		
	}
    public void clickI1( ) {
		clickOn("I1"); 
	}
	
    public void clickA2( ) {
        clickOn("A2");
    }
    public void clickB2( ) {
        clickOn("B2");
    }
    public void clickC2( ) {
        clickOn("C2");
    }
    public void clickD2( ) {
        clickOn("D2");
    }
    public void clickE2( ) {
        clickOn("E2");
    }
    public void clickF2( ) {
        clickOn("F2");
    }
    public void clickG2( ) {
        clickOn("G2");
    }
    public void clickH2( ) {
        clickOn("H2");
    }
    public void clickI2( ) {
		clickOn("I2"); 
	}

    public void clickA3( ) {
        clickOn("A3");
    }
    public void clickB3( ) {
        clickOn("B3");
    }
    public void clickC3( ) {
        clickOn("C3");
    }
    public void clickD3( ) {
        clickOn("D3");
    }
    public void clickE3( ) {
        clickOn("E3");
    }
    public void clickF3( ) {
        clickOn("F3");
    }
    public void clickG3( ) {
        clickOn("G3");
    }
    public void clickH3( ) {
        clickOn("H3");
    }
    public void clickI3( ) {
		clickOn("I3"); 
	}

    public void clickA4( ) {
        clickOn("A4");
    }
    public void clickB4( ) {
        clickOn("B4");
    }
    public void clickC4( ) {
        clickOn("C4");
    }
    public void clickD4( ) {
        clickOn("D4");
    }
    public void clickE4( ) {
        clickOn("E4");
    }
    public void clickF4( ) {
        clickOn("F4");
    }
    public void clickG4( ) {
        clickOn("G4");
    }
    public void clickH4( ) {
        clickOn("H4");
    }
    public void clickI4( ) {
		clickOn("I4"); 
	}

    public void clickA5( ) {
        clickOn("A5");
    }
    public void clickB5( ) {
        clickOn("B5");
    }
    public void clickC5( ) {
        clickOn("C5");
    }
    public void clickD5( ) {
        clickOn("D5");
    }
    public void clickE5( ) {
        clickOn("E5");
    }
    public void clickF5( ) {
        clickOn("F5");
    }
    public void clickG5( ) {
        clickOn("G5");
    }
    public void clickH5( ) {
        clickOn("H5");
    }
    public void clickI5( ) {
		clickOn("I5"); 
	}

    public void clickA6( ) {
        clickOn("A6");
    }
    public void clickB6( ) {
        clickOn("B6");
    }
    public void clickC6( ) {
        clickOn("C6");
    }
    public void clickD6( ) {
        clickOn("D6");
    }
    public void clickE6( ) {
        clickOn("E6");
    }
    public void clickF6( ) {
        clickOn("F6");
    }
    public void clickG6( ) {
        clickOn("G6");
    }
    public void clickH6( ) {
        clickOn("H6");
    }
    public void clickI6( ) {
		clickOn("I6"); 
	}

    public void clickA7( ){
        clickOn("A7");
    }
    public void clickB7( ){
        clickOn("B7");
    }
    public void clickC7( ) {
        clickOn("C7");
    }
    public void clickD7( ) {
        clickOn("D7");
    }
    public void clickE7( ) {
        clickOn("E7");
    }
    public void clickF7( ) {
        clickOn("F7");
    }
    public void clickG7( ) {
        clickOn("G7");
    }
    public void clickH7( ) {
        clickOn("H7");
    }
    public void clickI7( ) {
		clickOn("I7");		
	}

    public void clickA8( ) {
        clickOn("A8");
    }
    public void clickB8( ) {
        clickOn("B8");
    }
    public void clickC8( ) {
        clickOn("C8");
    }
    public void clickD8( ) {
        clickOn("D8");
    }
    public void clickE8( ) {
        clickOn("E8");
    }
    public void clickF8( ) {
        clickOn("F8");
    }
    public void clickG8( ) {
        clickOn("G8");
    }
    public void clickH8( ) {
        clickOn("H8");
    }
    public void clickI8( ) {
		clickOn("I8"); 
	}

    public void clickA9( ) {
        clickOn("A9");
    }
    public void clickB9( ) {
        clickOn("B9");
    }
    public void clickC9( ) {
        clickOn("C9");
    }
    public void clickD9( ) {
        clickOn("D9");
    }
    public void clickE9( ) {
        clickOn("E9");
    }
    public void clickF9( ) {
        clickOn("F9");
    }
    public void clickG9( ) {
        clickOn("G9");
    }
    public void clickH9( ) {
        clickOn("H9");
    }
    public void clickI9( ) {
		clickOn("I9");
	}
}
