import java.util.Scanner;

public class TicTacToe {

    private static final char[][] board = new char[3][3];

    private static char currentPlayer = 'X';

    private static int xScore = 0;
    private static int oScore = 0;
    private static int drawCount = 0;

    private static String playerX;
    private static String playerO;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("        TIC - TAC - TOE");
        System.out.println("=================================");

        playerX = readName(scanner, "Enter name for Player X: ", "Player X");
        playerO = readName(scanner, "Enter name for Player O: ", "Player O");

        boolean playAgain = true;

        while (playAgain) {

            initializeBoard();
            currentPlayer = 'X';

            boolean quit = playGame(scanner);

            if (quit) {
                System.out.println("\nGame ended by the player.");
                break;
            }

            System.out.print("\nDo you want to play again? (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            playAgain = choice.equals("yes") || choice.equals("y");
        }

        System.out.println("\n=================================");
        System.out.println("          FINAL SCORE");
        System.out.println("=================================");

        System.out.println(playerX + " (X): " + xScore);
        System.out.println(playerO + " (O): " + oScore);
        System.out.println("Draws: " + drawCount);

        if (xScore > oScore) {
            System.out.println("Overall Winner: " + playerX);
        } else if (oScore > xScore) {
            System.out.println("Overall Winner: " + playerO);
        } else {
            System.out.println("Overall Result: Draw");
        }

        System.out.println("\nThank you for playing!");
        scanner.close();
    }

    // Read a player name, re-prompting if left blank
    private static String readName(Scanner scanner, String prompt, String defaultName) {

        while (true) {

            System.out.print(prompt);
            String name = scanner.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println("Name cannot be empty. Using default: " + defaultName);
            return defaultName;
        }
    }

    // Initialize the board
    private static void initializeBoard() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    // Display the board
    private static void printBoard() {

        System.out.println();

        System.out.println("       1     2     3");
        System.out.println("    +-----+-----+-----+");

        for (int i = 0; i < 3; i++) {

            System.out.print(" " + (i + 1) + "  |");

            for (int j = 0; j < 3; j++) {

                System.out.print("  " + board[i][j] + "  |");
            }

            System.out.println();
            System.out.println("    +-----+-----+-----+");
        }

        System.out.println();
    }

    // Main game loop. Returns true if the player chose to quit entirely.
    private static boolean playGame(Scanner scanner) {

        boolean gameWon = false;
        boolean draw = false;
        int movesPlayed = 0;

        while (!gameWon && !draw) {

            printBoard();
            printScore();

            String playerName = getPlayerName();

            System.out.println(playerName + " (" + currentPlayer + "), it's your turn.");

            int row;
            int col;

            while (true) {

                System.out.print("Enter row and column (1-3), or 0 0 to quit: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input! Please enter numbers.");
                    scanner.next();
                    continue;
                }

                row = scanner.nextInt();

                if (!scanner.hasNextInt()) {
                    System.out.println("Please enter both row and column.");
                    // consume whatever non-integer token is sitting there
                    if (scanner.hasNext()) {
                        scanner.next();
                    }
                    continue;
                }

                col = scanner.nextInt();

                // Quit option
                if (row == 0 && col == 0) {
                    scanner.nextLine(); // consume trailing newline before returning to line-based reads
                    return true;
                }

                row--;
                col--;

                if (!isValidMove(row, col)) {
                    System.out.println("Invalid move! Try another position.");
                } else {
                    break;
                }
            }

            scanner.nextLine(); // consume trailing newline left after nextInt() calls

            // Make the move
            board[row][col] = currentPlayer;
            movesPlayed++;

            // Check winner
            gameWon = checkWin();

            if (gameWon) {

                printBoard();

                System.out.println("=================================");
                System.out.println("        GAME OVER");
                System.out.println("=================================");

                System.out.println(
                        "Winner: " + getPlayerName() +
                        " (" + currentPlayer + ")"
                );

                updateScore();

            } else {

                // Only worth checking for a draw once the board could possibly be full
                draw = movesPlayed >= 9 && checkDraw();

                if (draw) {

                    printBoard();

                    System.out.println("=================================");
                    System.out.println("          GAME DRAW!");
                    System.out.println("=================================");

                    drawCount++;

                } else {

                    switchPlayer();
                }
            }
        }

        return false;
    }

    // Check whether move is valid
    private static boolean isValidMove(int row, int col) {

        return row >= 0 &&
               row < 3 &&
               col >= 0 &&
               col < 3 &&
               board[row][col] == ' ';
    }

    // Check winner
    private static boolean checkWin() {

        // Check rows
        for (int i = 0; i < 3; i++) {

            if (board[i][0] == currentPlayer &&
                board[i][1] == currentPlayer &&
                board[i][2] == currentPlayer) {

                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {

            if (board[0][i] == currentPlayer &&
                board[1][i] == currentPlayer &&
                board[2][i] == currentPlayer) {

                return true;
            }
        }

        // Check main diagonal
        if (board[0][0] == currentPlayer &&
            board[1][1] == currentPlayer &&
            board[2][2] == currentPlayer) {

            return true;
        }

        // Check other diagonal
        if (board[0][2] == currentPlayer &&
            board[1][1] == currentPlayer &&
            board[2][0] == currentPlayer) {

            return true;
        }

        return false;
    }

    // Check whether board is full
    private static boolean checkDraw() {

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    // Switch player
    private static void switchPlayer() {

        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        } else {
            currentPlayer = 'X';
        }
    }

    // Get current player's name
    private static String getPlayerName() {

        if (currentPlayer == 'X') {
            return playerX;
        }

        return playerO;
    }

    // Update score
    private static void updateScore() {

        if (currentPlayer == 'X') {
            xScore++;
        } else {
            oScore++;
        }
    }

    // Display score
    private static void printScore() {

        System.out.println(
                "Score: " + playerX + " (X) = " + xScore +
                " | " + playerO + " (O) = " + oScore +
                " | Draws = " + drawCount
        );

        System.out.println();
    }
}