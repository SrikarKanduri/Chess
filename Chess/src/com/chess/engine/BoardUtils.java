package com.chess.engine;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = null;
    public static final boolean[] SECOND_COLUMN = null;
    public static final boolean[] SEVENTH_COLUMN = null;
    public static final boolean[] EIGHTH_COLUMN = null;

    private BoardUtils(){
        throw new RuntimeException("Instantiation prohibited!");
    }
    public static boolean validDestinationPosition(int destinationPosition) {
        return destinationPosition >=0 && destinationPosition < 64;
    }
}
