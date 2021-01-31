#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <iostream>
#include <vector>
#include <queue>

#define QUEUE_SIZE 5
#define BUFF_SIZE 3
#define NICK_SIZE 8
pthread_mutex_t queueMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t atLeastTwoPlayers = PTHREAD_COND_INITIALIZER;

using namespace std;

//Struktura zawierająca kolejkę graczy
struct data_thread_join {
    queue<int> &playersQueue;
    data_thread_join(queue<int> &players) : playersQueue(players) {}
};

//struktura zawierająca deskryptory pary graczy
struct data_thread_game {
    int firstSocketDescriptor;
    int secondSocketDescriptor;
};

bool checkIfWin(int tab[][10]) {
    int check = 0;
    //Sprawdzenie w poziomie
    for( int y=1;y<10;y++){
        for( int x=1;x<6;x++){
            check = tab[x][y] + tab[x+1][y] + tab[x+2][y] + tab[x+3][y] + tab[x+4][y];
            if (check == 5 || check == 50)
                return true;
            //Sprawdzenie pod skosem w dól
            if (y<6){
                check = tab[x][y] + tab[x+1][y+1] + tab[x+2][y+2] + tab[x+3][y+3] + tab[x+4][y+4];
                if (check == 5 || check == 50)
                    return true;
            }
        }
    }
    //Sprawdzenie w pionie
    for( int x=1;x<10;x++){
        for( int y=1;y<6;y++){
            check = tab[x][y] + tab[x][y+1] + tab[x][y+2] + tab[x][y+3] + tab[x][y+4];
            if (check == 5 || check == 50)
                return true;
            //Sprawdzenie pod skosem w góre
            if (x>4){
                 check = tab[x][y] + tab[x-1][y-1] + tab[x-2][y-2] + tab[x-3][y-3] + tab[x-4][y-4];
                 if (check == 5 || check == 50)
                    return true;
            }
        }
    }
    return false;
}

//funkcja opisującą zachowanie wątku gry
void *ThreadBehavior(void *t_data) {
    pthread_detach(pthread_self());
    struct data_thread_game *th_data = (struct data_thread_game *) t_data;
	char *nickRequest = (char *) malloc(sizeof(char) * BUFF_SIZE);
    char *player1Nick = (char *) malloc(sizeof(char) * NICK_SIZE);
    char *player2Nick = (char *) malloc(sizeof(char) * NICK_SIZE);
    strcpy(nickRequest,"NIC");

    //Przypisanie krótszych nazw do deskryptorów graczy
    int whitePlayer = th_data->firstSocketDescriptor;
    int blackPlayer = th_data->secondSocketDescriptor;
  
    int readBytes = 0;
    int writeBytes = 0;
    int readResult;
    int writeResult;

    //Zapytanie białego gracza o jego nazwę
    while (writeBytes < BUFF_SIZE) {
        writeResult = write(whitePlayer, nickRequest + writeBytes, BUFF_SIZE - writeBytes);
        if (writeResult < 1) 
            break;
        writeBytes += writeResult;
    }
    if (writeResult < 1) {
        write(blackPlayer, "CON", BUFF_SIZE);
        printf("%d vs %d - koniec gry\n", whitePlayer, blackPlayer);
        free(nickRequest);
        free(player1Nick);
        free(player2Nick);
        free(th_data);
        close(whitePlayer);
        close(blackPlayer);
        pthread_exit(NULL);
    }
    readBytes = 0;
    //Odczytanie nazwy białego gracza
    while (readBytes < BUFF_SIZE) {
        readResult = read(whitePlayer, player1Nick + readBytes, NICK_SIZE - readBytes);
        if (readResult < 1) 
            break;
        readBytes += readResult;
    }
    if (readResult < 1) {
        write(blackPlayer, "CON", BUFF_SIZE);
        printf("%d vs %d - koniec gry\n", whitePlayer, blackPlayer);
        free(nickRequest);
        free(player1Nick);
        free(player2Nick);
        free(th_data);
        close(whitePlayer);
        close(blackPlayer);
        pthread_exit(NULL);
    }
    writeBytes = 0;
    //Zapytanie czarnego gracza o jego nazwę
    while (writeBytes < BUFF_SIZE) {
        writeResult = write(blackPlayer, nickRequest + writeBytes, BUFF_SIZE - writeBytes);
        if (writeResult < 1) 
            break;
        writeBytes += writeResult;
    }
    if (writeResult < 1) {
        write(whitePlayer, "CON", BUFF_SIZE);
        printf("%d vs %d - koniec gry\n", whitePlayer, blackPlayer);
        free(nickRequest);
        free(player1Nick);
        free(player2Nick);
        free(th_data);
        close(whitePlayer);
        close(blackPlayer);
        pthread_exit(NULL);
    }
    readBytes = 0;
    //Odczytanie nazwy czarnego gracza
    while (readBytes < BUFF_SIZE) {
        readResult = read(blackPlayer, player2Nick + readBytes, NICK_SIZE - readBytes);
        if (readResult < 1) 
            break;
        readBytes += readResult;
    }
    if (readResult < 1) {
        write(whitePlayer,"CON",BUFF_SIZE);
        printf("%d vs %d - koniec gry\n", whitePlayer,blackPlayer);
        free(nickRequest);
        free(player1Nick);
        free(player2Nick);
        free(th_data);
        close(whitePlayer);
        close(blackPlayer);
        pthread_exit(NULL);
    }
    free(nickRequest);

    writeBytes = 0;
    //Przesłanie białemu graczowi nazwy czarnego gracza
    while (writeBytes < NICK_SIZE) {
        writeResult = write(whitePlayer, player2Nick + writeBytes, NICK_SIZE - writeBytes);
        if (writeResult < 1) 
            break;
        writeBytes += writeResult;
    }
    if (writeResult < 1) {
        write(blackPlayer,"NICKINFO",NICK_SIZE);
        printf("%d vs %d - koniec gry\n", whitePlayer,blackPlayer);
        free(player1Nick);
        free(player2Nick);
		free(th_data);
        close(whitePlayer);
        close(blackPlayer);
        pthread_exit(NULL);
    }
    writeBytes = 0;
    //Przesłanie czarnemu graczowi nazwy białego gracza
    while (writeBytes < NICK_SIZE) {
        writeResult = write(blackPlayer, player1Nick + writeBytes, NICK_SIZE - writeBytes);
        if (writeResult < 1) 
            break;
        writeBytes += writeResult;
    }
    if (writeResult < 1) {
        write(whitePlayer,"NICKINFO",NICK_SIZE);
        printf("%d vs %d - koniec gry\n", whitePlayer,blackPlayer);
        free(player1Nick);
        free(player2Nick);
		free(th_data);
        close(whitePlayer);
        close(blackPlayer);
        pthread_exit(NULL);
    }
    free(player1Nick);
    free(player2Nick);
    //Stworzenie tablicy gry
    int board[10][10] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };


    char *whiteColor = (char *) malloc(sizeof(char) * BUFF_SIZE);
    strcpy(whiteColor,"WHI");
    char *blackColor = (char *) malloc(sizeof(char) * BUFF_SIZE);
    strcpy(blackColor,"BLA");

    writeBytes = 0;
    //Przesłanie białemu graczowi jego koloru
    while (writeBytes < BUFF_SIZE) {
        writeResult = write(whitePlayer, whiteColor + writeBytes, BUFF_SIZE - writeBytes);
        if (writeResult < 1) 
            break;
        writeBytes += writeResult;
    }
    if (writeResult < 1) {
        write(blackPlayer,"CON",BUFF_SIZE);
        printf("%d vs %d - koniec gry\n", whitePlayer,blackPlayer);
        free(whiteColor);
        free(blackColor);
        free(th_data);
        close(whitePlayer);
        close(blackPlayer);
        pthread_exit(NULL);
    }
    writeBytes = 0;
    //Przesłanie czarnemu graczowi jego koloru
    while (writeBytes < BUFF_SIZE) {
        writeResult = write(blackPlayer, blackColor + writeBytes, BUFF_SIZE - writeBytes);
        if (writeResult < 1)
            break;
        writeBytes += writeResult;
    }
    if (writeResult < 1) {
        write(whitePlayer,"CON",BUFF_SIZE);
        printf("%d vs %d - koniec gry\n", whitePlayer,blackPlayer);
        free(whiteColor);
        free(blackColor);
        free(th_data);
        close(whitePlayer);
        close(blackPlayer);
        pthread_exit(NULL);;
    }
    free(whiteColor);
    free(blackColor);

    bool whiteTurn = true;
    bool ingame = true;
    bool gameEnd = false;
    bool whiteDisconnected = false;
    bool blackDisconnected = false;
    char *buffor = (char *) malloc(sizeof(char) * BUFF_SIZE);
    //Pętla gry
    while (ingame) {
        //Tura białego gracza
        if(whiteTurn){
            readBytes = 0;
            //Czytanie ruchu białego gracza
            while (readBytes < BUFF_SIZE) {
                readResult = read(whitePlayer, buffor + readBytes, BUFF_SIZE - readBytes);
                if (readResult < 1) 
                    break;
                readBytes += readResult;
            }
            if (readResult < 1) {
                ingame = false;
                whiteDisconnected = true;
                break;
            }
            //Ustalenie współrzędnych
            int posX;
            int posY = buffor[2] - '0';
            switch (buffor[1]) {
            case 'A':
                posX = 1;
                break;
            case 'B':
                posX = 2;
                break;
            case 'C':
                posX = 3;
                break;
            case 'D':
                posX = 4;
                break;
            case 'E':
                posX = 5;
                break;
            case 'F':
                posX = 6;
                break;
            case 'G':
                posX = 7;
                break;
            case 'H':
                posX = 8;
                break;
            case 'I':
                posX = 9;
                break;
            }
            //Zaznaczenie ruchu gracza w tablicy gry
            board[posX][posY] = 1;
            whiteTurn = false;
            //Sprawdzenie czy gracz wygrał
            gameEnd = checkIfWin(board);
            if (gameEnd) {
                printf("%d vs %d - biały wygrywa\n", whitePlayer,blackPlayer);
                char * info = (char *) malloc(sizeof(char) * BUFF_SIZE);
                strcpy(info,"WWI");
                writeBytes = 0;
                //Wysłanie wiadomości obu graczom, że wygrał gracz biały
                while (writeBytes < BUFF_SIZE) {
                    writeResult = write(whitePlayer, info + writeBytes, BUFF_SIZE - writeBytes);
                    if (writeResult < 1) 
                        break;
                    writeBytes += writeResult;
                }
                writeBytes = 0;
                while (writeBytes < BUFF_SIZE) {
                    writeResult = write(blackPlayer, info + writeBytes, BUFF_SIZE - writeBytes);
                   if (writeResult < 1) 
                        break;
                    writeBytes += writeResult;
                }
                free(info);
                //Przesłanie czarnemu graczowi ruchu białego gracza
                writeBytes = 0;
                while (writeBytes < BUFF_SIZE) {
                    writeResult = write(blackPlayer, buffor + writeBytes, BUFF_SIZE - writeBytes);
                    if (writeResult < 1) 
                        break;
                    writeBytes += writeResult;
                }
                ingame = false;
            }
            //Gdy nie ma wygranej
            else {
                writeBytes = 0;
                //Przesłanie czarnemu graczowi ruchu białego gracza                
                while (writeBytes < BUFF_SIZE) {
                    writeResult = write(blackPlayer, buffor + writeBytes, BUFF_SIZE - writeBytes);
                    if (writeResult < 1) 
                        break;
                    writeBytes += writeResult;
                }
                if (writeResult < 1) {
                    ingame = false;
                    blackDisconnected = true;
                    break;
                }
            }
        }
        //Tura czarnego gracza
        else{
            readBytes = 0;
             //Czytanie ruchu czarnego gracza
            while (readBytes < BUFF_SIZE) {
                readResult = read(blackPlayer, buffor + readBytes, BUFF_SIZE - readBytes);
                if (readResult < 1) 
                    break;
                readBytes += readResult;
            }
            if (readResult < 1) {
                ingame = false;
                blackDisconnected = true;
                break;
            }
            //Ustalenie współrzędnych
            int posX;
            int posY = buffor[2] - '0';
            switch (buffor[1]) {
            case 'A':
                posX = 1;
                break;
            case 'B':
                posX = 2;
                break;
            case 'C':
                posX = 3;
                break;
            case 'D':
                posX = 4;
                break;
            case 'E':
                posX = 5;
                break;
            case 'F':
                posX = 6;
                break;
            case 'G':
                posX = 7;
                break;
            case 'H':
                posX = 8;
                break;
            case 'I':
                posX = 9;
                break;
            }
            //Zaznaczenie ruchu gracza w tablicy gry
            board[posX][posY] = 10;
            whiteTurn = true;
            //Sprawdzenie czy gracz wygrał
            gameEnd = checkIfWin(board);
            if (gameEnd == true) {
                printf("%d vs %d - czarny wygrywa\n", whitePlayer,blackPlayer);
                char * info = (char *) malloc(sizeof(char) * BUFF_SIZE);
                strcpy(info,"BWI");
                writeBytes = 0;
                //Wysłanie wiadomości obu graczom, że wygrał gracz czarny
                while (writeBytes < BUFF_SIZE) {
                    writeResult = write(whitePlayer, info + writeBytes, BUFF_SIZE - writeBytes);
                    if (writeResult < 1) 
                        break;
                    writeBytes += writeResult;
                }
                writeBytes = 0;
                while (writeBytes < BUFF_SIZE) {
                    writeResult = write(blackPlayer, info + writeBytes, BUFF_SIZE - writeBytes);
                    if (writeResult < 1) 
                        break;
                    writeBytes += writeResult;
                }
                free(info);
                //Przesłanie białemu graczowi ruchu czarnego gracza
                writeBytes = 0;
                while (writeBytes < BUFF_SIZE) {
                    writeResult = write(whitePlayer, buffor + writeBytes, BUFF_SIZE - writeBytes);
                    if (writeResult < 1) 
                        break;
                    writeBytes += writeResult;
                }
                ingame = false;
            }
            //Gdy nie ma wygranej
            else {
                writeBytes = 0;
                //Przesłanie białemu graczowi ruchu czarnego gracza  
                while (writeBytes < BUFF_SIZE) {
                    writeResult = write(whitePlayer, buffor + writeBytes, BUFF_SIZE - writeBytes);
                    if (writeResult < 1) 
                        break;
                    writeBytes += writeResult;
                }
                if (writeResult < 1) {
                    ingame = false;
                    whiteDisconnected = true;
                    break;
                }
            }
        }
    }
    //Po skończeniu gry  
    printf("%d vs %d - koniec gry\n", whitePlayer,blackPlayer);
    if (whiteDisconnected == true || blackDisconnected == true) {
        char * info = (char *) malloc(sizeof(char) * BUFF_SIZE);
        strcpy(info,"CON");
        writeBytes = 0;
        //Wysłanie przeciwnikowi informacji o rozłączonym przeciwniku
        while (writeBytes < BUFF_SIZE) {
            if (blackDisconnected)
                writeResult = write(whitePlayer, info + writeBytes, BUFF_SIZE - writeBytes);
            else
                writeResult = write(blackPlayer, info + writeBytes, BUFF_SIZE - writeBytes);
            if (writeResult < 1) 
                break;
            writeBytes += writeResult;
        }
        free(info);
    }
    free(buffor);
    free(th_data);
    close(whitePlayer);
    close(blackPlayer);
    pthread_exit(NULL);
}

//Funkcja wątku łączącego pary graczy do gry
void *ThreadJoin(void *t_data) {
    pthread_detach(pthread_self());
    struct data_thread_join *th_data = (struct data_thread_join *) t_data;
    while (1) {
        //Oczekiwanie na 2 graczy w kolejce
        pthread_mutex_lock(&queueMutex);
        while (th_data->playersQueue.size() < 2) {
            pthread_cond_wait(&atLeastTwoPlayers,&queueMutex);
        }
        //Uchwyt na wątek
        pthread_t thread1;
        //Przekazanie do wątku deskryptorów pierwszego i drugiego gracza
        struct data_thread_game *game_data = (struct data_thread_game *) malloc(sizeof(struct data_thread_game));
        game_data->firstSocketDescriptor = th_data->playersQueue.front();
        th_data->playersQueue.pop();
        game_data->secondSocketDescriptor = th_data->playersQueue.front();
        th_data->playersQueue.pop();
        pthread_mutex_unlock(&queueMutex);
        printf("NOWA GRA - %d vs %d\n", game_data->firstSocketDescriptor, game_data->secondSocketDescriptor);
        int createResult = 0;
        //Utworzenie wątku gry
        createResult = pthread_create(&thread1, NULL, ThreadBehavior, (void *) game_data);

        if (createResult) {
            printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", createResult);
        }
    }
}

//Funkcja tworząca wątek łączący graczy w pary
void createThreadJoin(queue<int> &newPlayersSockets) {
    int createResult = 0;
    //Uchwyt na wątek
    pthread_t thread1;
    //Przekazanie do wątku podłączonych i oczekujących na grę graczy
    struct data_thread_join *t_data = new data_thread_join(newPlayersSockets);
    createResult = pthread_create(&thread1, NULL, ThreadJoin, (void *) t_data);
    if (createResult) {
        printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", createResult);
        exit(-1);
    }
}

int main(int argc, char *argv[]) {
    if (argc != 2)
    {
        fprintf(stderr, "Sposób użycia: %s port_number\n", argv[0]);
        exit(1);
    }

    queue <int> newPlayersSockets;
    int server_socket_descriptor;
    int connection_socket_descriptor;
    int bind_result;
    int listen_result;
    char reuse_addr_val = 1;
    struct sockaddr_in server_address;

    //Inicjalizacja gniazda serwera
    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(atoi(argv[1]));

    server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket_descriptor < 0) {
        fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda..\n", argv[0]);
        exit(1);
    }
    setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char *) &reuse_addr_val, sizeof(reuse_addr_val));
    printf("Socket serwera: %d\n", server_socket_descriptor);
    bind_result = bind(server_socket_descriptor, (struct sockaddr *) &server_address, sizeof(struct sockaddr));
    if (bind_result < 0) {
        fprintf(stderr, "%s: Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda.\n", argv[0]);
        exit(1);
    }

    listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
    if (listen_result < 0) {
        fprintf(stderr, "%s: Błąd przy próbie ustawienia wielkości kolejki.\n", argv[0]);
        exit(1);
    }
    //Wywołanie funkcji tworzącej wątek łączący graczy w pary
    createThreadJoin(newPlayersSockets);
    while (1) {
        //
        //Dodawanie nowo podłączonych graczy do kolejki
        connection_socket_descriptor = accept(server_socket_descriptor, NULL, NULL);
        if (connection_socket_descriptor < 0) {
            fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda dla połączenia.\n", argv[0]);
        } else {
            printf("Nastąpiło połączenie na sockecie %d\n", connection_socket_descriptor);
            pthread_mutex_lock(&queueMutex);
            newPlayersSockets.push(connection_socket_descriptor);
            //Wysłanie sygnału, gdy w kolejce jest 2 lub więcej graczy
            if (newPlayersSockets.size() >= 2) pthread_cond_signal(&atLeastTwoPlayers);
            pthread_mutex_unlock(&queueMutex);
        }
    }
}
