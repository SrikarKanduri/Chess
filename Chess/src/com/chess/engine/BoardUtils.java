package com.chess.engine;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(2);
    public static final boolean[] EIGHTH_COLUMN = initColumn(3);

    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    private static final boolean[] initColumn(int columnNumber)
    {
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }while(columnNumber<NUM_TILES);
        return column;
    }
    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do{
            row[rowNumber++] = true;
        }while(rowNumber % NUM_TILES_PER_ROW!=0);
        return row;
    }
    private BoardUtils(){
        throw new RuntimeException("Instantiation prohibited!");
    }
    public static boolean validDestinationPosition(int destinationPosition) {
        return destinationPosition >=0 && destinationPosition < NUM_TILES;
    }
}
