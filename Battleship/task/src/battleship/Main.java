package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Write your code here
        Scanner scanner = new Scanner(System.in);
        String[][] battleMapP1 = new String[11][11];
        String[][] battleMapP2 = new String[11][11];
        String[][] pseudoMapP1 = new String[11][11];
        String[][] pseudoMapP2 = new String[11][11];
        String[][] foggedMapP1 = new String[11][11];
        String[][] foggedMapP2 = new String[11][11];
        initialFillMap(battleMapP1);
        initialFillMap(battleMapP2);
        initialFillMap(pseudoMapP1);
        initialFillMap(pseudoMapP2);
        String userInput = "";

        System.out.println("Player 1, place your ships on the game field\n");
        vizualize(battleMapP1);
        preparationalStage(battleMapP1, pseudoMapP1, foggedMapP1, scanner);
        System.out.println("Press Enter and pass the move to another player");
        userInput = scanner.nextLine();
        System.out.println("Player 2, place your ships on the game field\n");
        vizualize(battleMapP2);
        preparationalStage(battleMapP2, pseudoMapP2, foggedMapP2, scanner);
        System.out.println("Press Enter and pass the move to another player");
        userInput = scanner.nextLine();

        int turn = 3;

        while (true) {
            if (turn % 2 != 0) {
                vizualize(foggedMapP1);
                System.out.println("---------------------");
                vizualize(battleMapP1);
                System.out.println("Player 1, it's your turn:");
                userInput = scanner.nextLine();
                System.out.println(" ");
                int a = shot(battleMapP2, foggedMapP1, pseudoMapP2, userInput);
                if (a == 2) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    break;
                }
            } else if (turn % 2 == 0) {
                vizualize(foggedMapP2);
                System.out.println("---------------------");
                vizualize(battleMapP2);
                System.out.println("Player 2, it's your turn:");
                userInput = scanner.nextLine();
                System.out.println(" ");
                int a = shot(battleMapP1, foggedMapP2, pseudoMapP1, userInput);
                if (a == 2) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    break;
                }
            }
            userInput = scanner.nextLine();
            turn++;
        }
    }

    public static void vizualize(String[][] battleMap) {
        for (String[] rows : battleMap) {
            for (String columns : rows) {
                System.out.print(columns + " ");
            }
            System.out.println();
        }
        System.out.println(" ");
    }

    public static String[][] initialFillMap(String[][] battleMap) {
        for (int i = 0; i < battleMap.length; i++) {
            for (int j = 0; j < battleMap[i].length; j++) {
                if (i == 0) {
                    battleMap[0][j] = Integer.toString(j);
                } else if (i != 0 && j == 0) {
                    battleMap[i][j] = Character.toString(i + 64);
                } else if (i != 0) {
                    battleMap[i][j] = "~";
                }
            }
            battleMap[0][0] = " ";
        }
        return battleMap;
    }

    public static String[][] placeShip(String[][] battleMap, String[] coordinates, String[][] pseudoMap, String tokenForPseudoMap) {
        int lengthShipHorizontal = Math.abs(Integer.parseInt(coordinates[1]) - Integer.parseInt(coordinates[3]));
        int lengthShipVertical = Math.abs((int) coordinates[2].charAt(0) - (int) coordinates[0].charAt(0));
        int startingPointHorizontal = Math.min(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[3]));
        int startingPointVertical = Math.min((coordinates[0].charAt(0) - 64), (coordinates[2].charAt(0) - 64));

        for (int i = 1; i < battleMap.length; i++) {
            if (coordinates[0].equals(coordinates[2])) {
                if (coordinates[0].equals(battleMap[i][0])) {
                    for (int j = startingPointHorizontal; j <= lengthShipHorizontal + startingPointHorizontal; j++) {
                        battleMap[i][j] = "O";
                        pseudoMap[i][j] = tokenForPseudoMap;
                    }
                }
            } else {
                if (coordinates[0].equals(battleMap[i][0])) {
                    for (int k = startingPointVertical; k <= lengthShipVertical + startingPointVertical; k++) {
                        battleMap[k][Integer.parseInt(coordinates[1])] = "O";
                        pseudoMap[k][Integer.parseInt(coordinates[1])] = tokenForPseudoMap;
                    }
                }
            }
        }
        return battleMap;
    }

    public static boolean correctPosition(String[][] battleMap, String[] coordinates) {
        boolean correct = true;
        int startX = Math.min(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[3]));
        int startY = Math.min(coordinates[0].charAt(0) - 64, coordinates[2].charAt(0) - 64);
        int endX = Math.max(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[3]));
        int endY = Math.max(coordinates[0].charAt(0) - 64, coordinates[2].charAt(0) - 64);
        if (coordinates[1].equals(coordinates[3])) {
            for (int i = startY; i <= endY; i++) {
                if (battleMap[i][startX].equals("O")) {
                    correct = false;
                    break;
                }
            }
            if (startX > 1) {
                for (int i = startY; i <= endY; i++) {
                    if (battleMap[i][startX - 1].equals("O")) {
                        correct = false;
                        break;
                    }
                }
            }
            if (startX < 10) {
                for (int i = startY; i <= endY; i++) {
                    if (battleMap[i][startX + 1].equals("O")) {
                        correct = false;
                        break;
                    }
                }
            }

            if (startY > 1 && battleMap[startY - 1][startX].equals("O")) correct = false;
            if (startY > 1 && startX > 1 && battleMap[startY - 1][startX - 1].equals("O")) correct = false;
            if (startY > 1 && startX < 10 && battleMap[startY - 1][startX + 1].equals("O")) correct = false;

            if (endY < 10 && battleMap[startY - 1][startX].equals("O")) correct = false;
            if (endY < 10 && startX > 1 && battleMap[startY - 1][startX].equals("O")) correct = false;
            if (endY < 10 && startX < 10 && battleMap[endY + 1][startX - 1].equals("O")) correct = false;
        }

        if (coordinates[0].equals(coordinates[2])) {
            for (int i = startX; i <= endX; i++) {
                if (battleMap[startY][i].equals("O")) {
                    correct = false;
                    break;
                }
            }
            if (startY > 1) {
                for (int i = startX; i <= endX; i++) {
                    if (battleMap[startY - 1][i].equals("O")) {
                        correct = false;
                        break;
                    }
                }
            }
            if (startY < 10) {
                for (int i = startX; i <= endX; i++) {
                    if (battleMap[startY + 1][i].equals("O")) {
                        correct = false;
                        break;
                    }
                }
            }

            if (startX > 1 && battleMap[startY][startX - 1].equals("O")) correct = false;
            if (startX > 1 && startY > 1 && battleMap[startY - 1][startX - 1].equals("O")) correct = false;
            if (startX > 1 && startY < 10 && battleMap[startY + 1][startX - 1].equals("O")) correct = false;

            if (endX < 10 && battleMap[endY][startX + 1].equals("O")) correct = false;
            if (endX < 10 && endY > 1 && battleMap[endY - 1][startX + 1].equals("O")) correct = false;
            if (endX < 10 && endY < 10 && battleMap[endY + 1][startX + 1].equals("O")) correct = false;
        }
        return correct;
    }

    public static boolean wrongLocation(String[] userCoordinates) {
        return userCoordinates[0].equals(userCoordinates[2]) || userCoordinates[1].equals(userCoordinates[3]);
    }

    public static int shot(String[][] battleMap, String[][] foggedMap, String[][] pseudoMap, String shotCoordinates) {
        String[] shotCoordinatesArray = shotCoordinates.split("");
        String ay = shotCoordinatesArray[0];
        String ax = shotCoordinates.length() < 3 ? shotCoordinatesArray[1] : shotCoordinatesArray[1].equals("1") && shotCoordinatesArray[2].equals("0") ? "10" : "99";
        int shotX = Integer.parseInt(ax);
        int shotY = ay.charAt(0) - 64;
        if (shotY > 10 || shotX > 10) {
            System.out.println("Error! You entered the wrong coordinates! Try again:\n");
            return 0;
        } else if (battleMap[shotY][shotX].equals("O") || battleMap[shotY][shotX].equals("X")) {
            String hitToken = pseudoMap[shotY][shotX];
            battleMap[shotY][shotX] = "X";
            foggedMap[shotY][shotX] = "X";
            pseudoMap[shotY][shotX] = "X";
            if (win(pseudoMap)) {
                return 2;
            }
            if (sankShip(pseudoMap, shotX, shotY, hitToken)) {
                System.out.println("You sank a ship!\n Press Enter and pass the move to another player");
            } else System.out.println("You hit a ship!\nPress Enter and pass the move to another player");
            return 1;
        } else {
            battleMap[shotY][shotX] = "M";
            foggedMap[shotY][shotX] = "M";
            System.out.println("You missed!\nPress Enter and pass the move to another player");
            return 1;
        }
    }

    public static boolean sankShip(String[][] pseudoMap, int hitX, int hitY, String hitToken) {
        boolean sank = true;
        for (int i = 0; i < pseudoMap.length; i++) {
            for (int j = 0; j < pseudoMap[i].length; j++) {
                if (pseudoMap[i][j].equals(hitToken)) {
                    sank = false;
                    break;
                }
            }
        }
        return sank;
    }

    public static boolean win(String[][] pseudoMap) {
        for (int i = 0; i < pseudoMap.length; i++) {
            for (int j = 0; j < pseudoMap[i].length; j++) {
                if (pseudoMap[i][j].equals("P") || pseudoMap[i][j].equals("Q") || pseudoMap[i][j].equals("R")
                        || pseudoMap[i][j].equals("S") || pseudoMap[i][j].equals("T")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void preparationalStage(String[][] battleMap, String[][] pseudoMap, String[][] foggedMap, Scanner scanner) {
        ShipAirCarrier airCarrier = new ShipAirCarrier();
        ShipBattleship battleship = new ShipBattleship();
        ShipCruiser cruiser = new ShipCruiser();
        ShipSubmarine submarine = new ShipSubmarine();
        ShipDestroyer destroyer = new ShipDestroyer();
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        String userInput = "";
        while (true) {
            userInput = scanner.nextLine();
            if (!wrongLocation(airCarrier.userCoordinates(userInput))) {
                System.out.println("Error! Wrong ship location! Try again:");
            } else if (airCarrier.shipLength(airCarrier.userCoordinates(userInput)) != 5) {
                System.out.println("Error! Wrong length of the Submarine! Try again:");
            } else break;
        }
        placeShip(battleMap, airCarrier.userCoordinates(userInput), pseudoMap, "P");
        vizualize(battleMap);

        System.out.println("Enter the coordinates of the Battleship (4 cells):");
        while (true) {
            userInput = scanner.nextLine();
            if (!correctPosition(battleMap, battleship.userCoordinates(userInput))) {
                System.out.println("Error! You placed it too close to another one. Try again:");
            } else if (!wrongLocation(battleship.userCoordinates(userInput))) {
                System.out.println("Error! Wrong ship location! Try again:");
            } else if (battleship.shipLength(battleship.userCoordinates(userInput)) != 4) {
                System.out.println("Error! Wrong length of the Submarine! Try again:");
            } else break;
        }
        placeShip(battleMap, battleship.userCoordinates(userInput), pseudoMap, "Q");
        vizualize(battleMap);

        System.out.println("Enter the coordinates of the Submarine (3 cells):");
        while (true) {
            userInput = scanner.nextLine();
            if (!correctPosition(battleMap, submarine.userCoordinates(userInput))) {
                System.out.println("Error! You placed it too close to another one. Try again:");
            } else if (!wrongLocation(submarine.userCoordinates(userInput))) {
                System.out.println("Error! Wrong ship location! Try again:");
            } else if (submarine.shipLength(submarine.userCoordinates(userInput)) != 3) {
                System.out.println("Error! Wrong length of the Submarine! Try again:");
            } else break;
        }
        placeShip(battleMap, submarine.userCoordinates(userInput), pseudoMap, "R");
        vizualize(battleMap);

        System.out.println("Enter the coordinates of the Cruiser (3 cells):");
        while (true) {
            userInput = scanner.nextLine();
            if (!correctPosition(battleMap, cruiser.userCoordinates(userInput))) {
                System.out.println("Error! You placed it too close to another one. Try again:");
            } else if (!wrongLocation(cruiser.userCoordinates(userInput))) {
                System.out.println("Error! Wrong ship location! Try again:");
            } else if (cruiser.shipLength(cruiser.userCoordinates(userInput)) != 3) {
                System.out.println("Error! Wrong length of the Submarine! Try again:");
            } else break;
        }
        placeShip(battleMap, cruiser.userCoordinates(userInput), pseudoMap, "S");
        vizualize(battleMap);

        System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        while (true) {
            userInput = scanner.nextLine();
            if (!correctPosition(battleMap, destroyer.userCoordinates(userInput))) {
                System.out.println("Error! You placed it too close to another one. Try again:");
            } else if (!wrongLocation(destroyer.userCoordinates(userInput))) {
                System.out.println("Error! Wrong ship location! Try again:");
            } else if (destroyer.shipLength(destroyer.userCoordinates(userInput)) != 2) {
                System.out.println("Error! Wrong length of the Submarine! Try again:");
            } else break;
        }
        placeShip(battleMap, destroyer.userCoordinates(userInput), pseudoMap, "T");
        vizualize(battleMap);
        initialFillMap(foggedMap);
    }
}