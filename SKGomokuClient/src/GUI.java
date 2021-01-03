import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class GUI extends JFrame {

    private final Container pane ;
    private JButton[][] board;
    private String player = "x";
    private String enemy = "o";
    private JMenuBar menuBar;
    private JMenu menuStart;
    private JMenuItem newGame;
    private JMenu menuMore;
    private JMenuItem quit;


    public GUI() {
        pane = getContentPane();
        pane.setLayout(new GridLayout(19,19));
        setTitle("Gomoku");
        setSize(800, 800);
        setResizable(false);
        initBoard();
        initMenu();

    }

    private void initMenu() {
        menuBar = new JMenuBar();
        menuStart = new JMenu("Start");
        newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                resetBoard();
            }
        });
        menuMore = new JMenu("More");
        quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        menuStart.add(newGame);
        menuMore.add(quit);
        menuBar.add(menuStart);
        menuBar.add(menuMore);
        setJMenuBar(menuBar);
    }

    private void initBoard(){
        board = new JButton[19][19];
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                JButton field = new JButton();
                field.setForeground(Color.RED);
                board[i][j] = field;
                field.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(((JButton)e.getSource()).getText().equals("")){
                            field.setText(player);
                            //board[0][0].setText(enemy); //enemy move
                        }
                    }
                });
                pane.add(field);
            }
        }
    }
    private void resetBoard(){
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                board[i][j].setText("");
            }
        }
    }
}
