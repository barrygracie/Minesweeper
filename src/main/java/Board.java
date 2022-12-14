import java.util.Random;
import java.util.Scanner;

public class Board {
    Tile [][] grid;
    int noOfMines;
    int boardSize;

    Random random;

    //Constructor Method
    public Board (int boardSize, int noOfMines){
        this.grid = new Tile [boardSize][boardSize];
        this.noOfMines = noOfMines;
        this.boardSize = boardSize;
        this.random = new Random();

        //fill array with tiles
        for(int i = 0;i<boardSize;i++){
            for(int j = 0;j<boardSize;j++){
                grid[i][j] = new Tile();
            }
        }
    }

    public Random getRandom(){
        return random;
    }

    //Method to set up mines in the tile grid, assigns mines randomly

    public void setupMines(Random random)
    {
        int a=0;

        while(a!=noOfMines)
        {
            int i = random.nextInt(noOfMines);
            int j = random.nextInt(noOfMines);
            while(grid[i][j].isMine){
                i = random.nextInt(noOfMines);
                j = random.nextInt(noOfMines);
            }
            grid[i][j].setMine();
            a++;
        }
    }

    //Method to set the mine counters on the tile grid after mines are set

    public void setMineCounters() {
        //simplify this
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int count = 0;
                if (!grid[i][j].isMine) {
                    if (i != 0) {
                        if (grid[i - 1][j].isMine) count++;
                        if (j != 0) {
                            if (grid[i - 1][j - 1].isMine) count++;
                        }
                    }
                    if (i != (boardSize - 1)) {
                        if (grid[i + 1][j].isMine) count++;
                        if (j != boardSize-1) {
                            if (grid[i + 1][j + 1].isMine) count++;
                        }
                    }
                    if (j != 0) {
                        if (grid[i][j - 1].isMine) count++;
                        if (i != boardSize-1) {
                            if (grid[i + 1][j - 1].isMine) count++;
                        }
                    }
                    if (j != (boardSize - 1)) {
                        if (grid[i][j + 1].isMine) count++;
                        if (i != 0) {
                            if (grid[i - 1][j + 1].isMine) count++;
                        }
                    }
                    grid[i][j].setMineCounter(count);
                }
            }
        }
    }

    //Method to display the current game board to user
    public void displayBoard()
    {
        System.out.print("\t ");
        for(int i=0; i<boardSize; i++)
        {
            if(i<10){System.out.print(" " + i + "  ");}
            else if(i<100){System.out.print(i + "  ");}
            else{System.out.print(i+" ");}
        }
        System.out.print("\n");
        for(int i=0; i<boardSize; i++)
        {
            System.out.print(i + "\t| ");
            for(int j=0; j<boardSize; j++)
            {
                if(grid[i][j].isFlag){
                    System.out.print("F");
                }
                else if(grid[i][j].isGuessed)
                {
                    if (grid[i][j].getMineCounter()>0){
                        System.out.print(grid[i][j].getMineCounter());
                    }
                    else{
                        System.out.print(" ");
                    }
                }
                else{
                    System.out.print("?");
                }

                System.out.print(" | ");
            }
            System.out.print("\n");
        }
    }

    //Method to display all mines at end of game

    public void displayMines(){
        System.out.print("\t ");
        for(int i=0; i<boardSize; i++)
        {
            System.out.print(" " + i + "  ");
        }
        System.out.print("\n");
        for(int i=0; i<boardSize; i++)
        {
            System.out.print(i + "\t| ");
            for(int j=0; j<boardSize; j++)
            {
                if(grid[i][j].isMine)
                {
                    System.out.print("M");
                }
                else{
                    System.out.print(" ");
                }
                System.out.print(" | ");
            }
            System.out.print("\n");
        }
    }

    //Method to update the board, takes in a coordinate

    public void updateBoard(int y, int x){
        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                if((i+y)<boardSize && (j+x)<boardSize && (i+y)>=0 && (j+x)>=0){

                        if(grid[i+y][j+x].getMineCounter()==0 && !grid[i+y][j+x].isMine && !grid[i+y][j+x].isGuessed)
                        {
                            grid[i+y][j+x].setGuessed();
                            updateBoard(i+y,j+x);
                        }
                        else if(grid[i+y][j+x].getMineCounter()>0 && !grid[i+y][j+x].isMine && !grid[i+y][j+x].isGuessed)
                        {
                            grid[i+y][j+x].setGuessed();
                        }
                }
            }
        }
    }

    //Method to play a move

    public boolean playMove()
    {
        Scanner sc= new Scanner(System.in);
        int choice;
        do{
            System.out.println("Press 1 to take a guess, Press 2 to place or remove a flag");
            choice = sc.nextInt();
        }
        while(choice<1 || choice>2);

        if(choice==1){
            System.out.print("Guessing");
            System.out.print("\nEnter Row Number: ");
            int y= sc.nextInt();
            System.out.print("Enter Column Number: ");
            int x= sc.nextInt();

            if(y<0 || y>(boardSize-1) || x<0 || x>(boardSize-1) || grid[y][x].isGuessed)
            {
                System.out.print("\nIncorrect Input!!");
                return true;
            }

            if(grid[y][x].isMine)
            {
                displayMines();
                System.out.print("You have detonated a Mine\n============GAME OVER============");
                return false;
            }
            else
            {
                grid[y][x].setGuessed();
                updateBoard(y,x);
            }
            return true;

        }
        else if (choice==2) {
            System.out.print("Setting Flag");
            System.out.print("\nEnter Row Number: ");
            int y= sc.nextInt();
            System.out.print("Enter Column Number: ");
            int x= sc.nextInt();

            if(y<0 || y>(boardSize-1) || x<0 || x>(boardSize-1) || grid[y][x].isGuessed)
            {
                System.out.print("\nIncorrect Input!!");
                return true;
            }
            grid[y][x].setFlag();
        }
        return true;
    }

    //Method to check if the game is won
    public boolean checkWin(){
        int guessCounter = 0;
        int correctFlags = 0;
        for(int i=0; i<boardSize; i++)
        {
            for(int j=0; j<boardSize; j++)
            {
                if(grid[i][j].isGuessed)
                {
                    guessCounter++;
                }
                else if(grid[i][j].isFlag && grid[i][j].isMine){
                    correctFlags++;
                }
            }
        }
        return guessCounter == (boardSize * boardSize) - noOfMines || correctFlags == noOfMines;

    }


}


