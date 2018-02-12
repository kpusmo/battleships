package Battleships.Ship;

public enum ShipType {
    ONE_DECKER_SHIP(1, "jednomasztowiec"),
    TWO_DECKER_SHIP(2, "dwumasztowiec"),
    THREE_DECKER_SHIP(3, "trójmasztowiec"),
    FOUR_DECKER_SHIP(4, "czteromasztowiec");

    private int length;
    private String name;

    ShipType(int length, String name) {
        this.length = length;
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }
}
