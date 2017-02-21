package com.chess.engine.pieces;

import com.chess.engine.BoardUtils;
import com.chess.engine.PieceColor;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {
    private final static int[] MOVE_OFFSETS = {7, 8, 9, 16};

    public Pawn(final PieceColor pieceColor, final int piecePosition) {
        super(PieceType.PAWN,piecePosition, pieceColor,true);
    }

    public Pawn(final PieceColor pieceColor, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN,piecePosition, pieceColor,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentOffset : MOVE_OFFSETS) {
            int destinationPosition = this.piecePosition + (this.pieceColor.getDirection() * currentOffset);
            if(!BoardUtils.validDestinationPosition(destinationPosition)){
                continue;
            }
            if(currentOffset == 8 && !board.getTile(destinationPosition).isTileOccupied()) {
                //TODO
                legalMoves.add(new MajorMove(board, this, destinationPosition));
            }
            else if(currentOffset == 16 && isFirstMove &&
                    ((BoardUtils.SEVENTH_RANK[piecePosition] && this.getPieceColor().isBlack()) ||
                    (BoardUtils.SECOND_RANK[piecePosition] && this.getPieceColor().isWhite()))){
                final int behindDestinationPosition = destinationPosition - (this.pieceColor.getDirection() * 8);
                if(!board.getTile(destinationPosition).isTileOccupied() &&
                        !board.getTile(behindDestinationPosition).isTileOccupied())
                    legalMoves.add(new PawnJump(board, this, destinationPosition));
            }
            else if (currentOffset == 7 &&
                    (!BoardUtils.FIRST_COLUMN[destinationPosition] && this.pieceColor.isBlack()) &&
                    (!BoardUtils.EIGHTH_COLUMN[destinationPosition] && this.pieceColor.isWhite())){
                if(board.getTile(destinationPosition).isTileOccupied()){
                    final Piece destinationPiece = board.getTile(destinationPosition).getPiece();
                    if(this.pieceColor != destinationPiece.getPieceColor()){
                        //TODO
                        legalMoves.add(new PawnAttackingMove(board, this, destinationPosition, destinationPiece));
                    }
                }
            }
            else if (currentOffset == 9 &&
                    (!BoardUtils.FIRST_COLUMN[destinationPosition] && this.pieceColor.isWhite()) &&
                    (!BoardUtils.EIGHTH_COLUMN[destinationPosition] && this.pieceColor.isBlack())){
                if(board.getTile(destinationPosition).isTileOccupied()){
                    final Piece destinationPiece = board.getTile(destinationPosition).getPiece();
                    if(this.pieceColor != destinationPiece.getPieceColor()){
                        //TODO
                        legalMoves.add(new PawnAttackingMove(board, this, destinationPosition, destinationPiece));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public String toString(){
        return this.pieceType.toString();
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovedPiece().getPieceColor(), move.getDestinationCoordinate());
    }
}

