package logic;

public class Decode {

    public static int[] decodeMove(String move) {
        int[] tab = new int[2];
        switch (move.charAt(0)) {
            case 'A':
                tab[0] = 1;
                break;
            case 'B':
                tab[0] = 2;
                break;
            case 'C':
                tab[0] = 3;
                break;
            case 'D':
                tab[0] = 4;
                break;
            case 'E':
                tab[0] = 5;
                break;
            case 'F':
                tab[0] = 6;
                break;
            case 'G':
                tab[0] = 7;
                break;
            case 'H':
                tab[0] = 8;
                break;
            case 'I':
                tab[0] = 9;
                break;
        }
        tab[1] = Integer.parseInt(move.substring(1, 2));
        return tab;
    }
}
