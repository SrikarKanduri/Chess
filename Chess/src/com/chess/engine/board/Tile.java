package com.chess.engine.board;

import com.chess.engine.piece.Piece;

abstract class Tile
{
    int tilePosition;
    Tile(int tilePosition)
    {
        this.tilePosition = tilePosition;
    }
    abstract Piece getPiece(int tilePosition);
}

abstract class occupiedTile extends Tile{
    Piece piece;
    occupiedTile(int position, Piece piece)
    {
        super(position);
        this.tilePosition = position;
        this.piece = piece;
    }
    Piece getPiece(int position)
    {
        return piece;
    }
}

abstract class unoccupiedTile extends Tile{
    unoccupiedTile(int position)
    {
        super(position);
        this.tilePosition = position;
    }
    Piece getPiece(int position)
    {
        return null;
    }
}
