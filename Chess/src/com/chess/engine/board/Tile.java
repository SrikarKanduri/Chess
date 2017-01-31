package com.chess.engine.board;

import com.chess.engine.BoardUtils;
import com.chess.engine.pieces.Piece;

import java.util.*;

public abstract class Tile
{
    protected final int tilePosition;

    private static final Map<Integer,EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i,new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTileMap);
    }

    public static Tile createTile(final int tilePosition, final Piece piece){
        return piece != null? new OccupiedTile(tilePosition,piece) : EMPTY_TILES.get(tilePosition);
    }

    private Tile(int tilePosition)
    {
        this.tilePosition = tilePosition;
    }

    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile {
        private EmptyTile(final int position) {
            super(position);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {
    private final Piece pieceOnTile;

    private OccupiedTile(final int position, final Piece pieceOnTile) {
        super(position);
        this.pieceOnTile = pieceOnTile;
    }

    @Override
    public boolean isTileOccupied() {
        return true;
    }

    @Override
    public Piece getPiece() {
        return pieceOnTile;
    }
}
}
