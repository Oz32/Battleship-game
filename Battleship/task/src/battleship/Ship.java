package battleship;

public abstract class Ship {

    public String[] userCoordinates(String userInput) {
        String[] coordinates = userInput.split("");
        String ay = coordinates[0];
        String ax = coordinates[1].equals("1") && coordinates[2].equals("0") ? "10" : coordinates[1];
        String by = ax.equals("10") ? coordinates[4] : coordinates[3];
        String bx = ax.equals("10") && userInput.length() == 7 ? "10" : userInput.length() == 6 && !ax.equals("10") ? "10" : ax.equals("10") ? coordinates[5] : coordinates[4];
        return new String[]{ay, ax, by, bx};
    }

    public int shipLength(String[] userCoordinates) {
        int tempLength = 0;
        if (userCoordinates[0].equals(userCoordinates[2])) {
            tempLength = 1 + Math.abs(Integer.parseInt(userCoordinates[1]) - Integer.parseInt(userCoordinates[3]));
        } else if (userCoordinates[1].equals(userCoordinates[3])) {
            tempLength = 1 + Math.abs((int) userCoordinates[2].charAt(0) - (int) userCoordinates[0].charAt(0));
        }
        return tempLength;
    }
}
