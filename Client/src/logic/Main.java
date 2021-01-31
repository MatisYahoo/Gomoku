package logic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main extends Application {

    private Stage stage;
    private Socket socket;
    ConnectionController connectionController;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gomoku");
        this.stage = stage;
        ConnectionView();
    }

    public void ConnectionView(){
        try {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../layout/ConnectionView.fxml"));
            BorderPane borderPane = loader.load();
            connectionController = loader.getController();
            connectionController.setMain(this);
            Scene scene = new Scene(borderPane);
            stage.setTitle("Gomoku");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GameView(String yourNick, String enemyNick, String color) {
        try {
            stage.close();
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../layout/GameView.fxml"));
            BorderPane layout = loader.load();
            Controller controller = loader.getController();
            controller.setConnectionThread(socket);
            controller.setNicks(yourNick, enemyNick);
            controller.startGame(color, this);
            Scene scene = new Scene(layout);
            stage.setTitle("Gomoku");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(e->{
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToConnectionView(String title, String content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(content);
                alert.showAndWait();
                stage.close();
                ConnectionView();
            }
        });


    }

    public int connect(String nick, String address, String port) {
        try {
            socket = new Socket(address, Integer.parseInt(port));
            final Thread newGameThread = new Thread() {
                @Override
                public void run() {
                    InputStream is;
                    OutputStream os ;
                    String info;
                    String response;
                    byte[] buffer = new byte[3];
                    byte[] nickBuffer = new byte[8];
                    try {
                        is = socket.getInputStream();
                        os = socket.getOutputStream();
                        is.read(buffer);
                        info = new String(buffer,0,3);
                        if (info.equals("NIC")) {
                            response = nick+"\n";
                            os.write(response.getBytes());
                        } else {
                            System.out.println("Błąd przy pobraniu nazwy");
                            connectionController.threadExists = false;
                            this.interrupt();
                        }

                        is.read(nickBuffer);
                        info = new String(nickBuffer,0,8);
                        final String enemyNick = info;

                        is.read(buffer);
                        info = new String(buffer,0,3);
                        final String color = info;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() { GameView(nick, enemyNick, color);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
            };
            newGameThread.start();

        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
