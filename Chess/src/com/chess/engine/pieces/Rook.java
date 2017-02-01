package com.chess.engine.pieces;

import com.chess.engine.BoardUtils;
import com.chess.engine.PieceColor;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Rook extends Piece{
    private final static int[] MOVE_OFFSETS= {-8, -1, 1, 8};

    public Rook(PieceColor pieceColor, int piecePosition) {
        super(piecePosition, pieceColor);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentOffset : MOVE_OFFSETS) {
            int destinationPosition = this.piecePosition;
            while (BoardUtils.validDestinationPosition(destinationPosition)) {
                if(isFirstColumnExclusion(destinationPosition,currentOffset)||
                        isEighthColumnExclusion(destinationPosition,currentOffset))
                    break;
                destinationPosition += currentOffset;
                if (BoardUtils.validDestinationPosition(destinationPosition)) {
                    final Tile destinationTile = board.getTile(destinationPosition);
                    if (!destinationTile.isTileOccupied()) {
                        legalMoves.add(new NonattackingMove(board, this, destinationPosition));
                    } else {
                        final Piece destinationPiece = destinationTile.getPiece();
                        final PieceColor destinationPieceColor = destinationPiece.getPieceColor();

                        if (destinationPieceColor != this.pieceColor) {
                            legalMoves.add(new AttackingMove(board, this, destinationPosition, destinationPiece));
                        }
                        //Any obstacle=>rook has to stop
                        break;
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }
    public static boolean isFirstColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (currentOffset == -1);
    }
    public static boolean isEighthColumnExclusion(final int currentPosition, final int currentOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (currentOffset == 1);
    }
    @Override
    public String toString(){
        return "R";
    }
}
