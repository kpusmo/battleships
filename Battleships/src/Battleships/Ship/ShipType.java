package Battleships.Ship;

public enum ShipType {
    ONE_DECKER_SHIP(1, "Jednomasztowiec"),
    TWO_DECKER_SHIP(2, "Dwumasztowiec"),
    THREE_DECKER_SHIP(3, "Trójmasztowiec"),
    FOUR_DECKER_SHIP(4, "Czteromasztowiec");

    private int value;
    private String name;

    ShipType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
