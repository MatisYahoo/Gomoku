package logic;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectionController {
    public TextField nickField;
    public TextField addressField;
    public TextField portField;
    public Label infoLabel;
    public boolean threadExists = false;
    private Main main;

    public void setMain (Main m) {
        this.main = m;
    }

    public void joinGame() {
        if (!threadExists) {
            String nick = nickField.getText();
            String address = addressField.getText();
            String port = portField.getText();
            int state;
            if ( !validNick(nick) ) {
                state = -3;
            } else {
                if (validIP(address) & validPort(port)) {
                    state = main.connect(nick, address, port);
                } else state = -2;
            }
            switch (state) {
                case 0: {
                    threadExists = true;
                    infoLabel.setText("Oczekiwanie na gracza");
                    infoLabel.setVisible(true);
                    break;
                }
                case -1: {
                    infoLabel.setText("Nie można połączyć z serwerem");
                    infoLabel.setVisible(true);
                    break;
                }
                case -2: {
                    infoLabel.setText("Błędny adres lub port");
                    infoLabel.setVisible(true);
                    break;
                }
                case -3: {
                    infoLabel.setText("Nick musi mieć od 1 do 8 znaków");
                    infoLabel.setVisible(true);
                    break;
                }
            }

        }
    }

    private boolean validNick(String nick) {
        if (nick.isEmpty() || nick.length() > 8 ) return false;
        return true;
    }

	public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
	
    private boolean validPort(String port) {
        if (port == null) {
            return false;
        }
        try {
            Integer.parseInt(port);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
