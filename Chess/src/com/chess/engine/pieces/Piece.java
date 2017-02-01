package com.chess.engine.pieces;

import com.chess.engine.PieceColor;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.*;

public abstract class Piece {

    protected final int piecePosition;
    protected final PieceColor pieceColor;
    protected final boolean isFirstMove;

    Piece(final int piecePosition, final PieceColor pieceColor){
        this.piecePosition = piecePosition;
        this.pieceColor = pieceColor;
        this.isFirstMove = false;
    }

    public int getPiecePosition()
    {
        return this.piecePosition;
    }

    public PieceColor getPieceColor(){
        return this.pieceColor;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);
/*
    public enum PieceType{
        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        QUEEN("Q"),
        KING("K");

        private String pieceName;

        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }
    }*/
}
