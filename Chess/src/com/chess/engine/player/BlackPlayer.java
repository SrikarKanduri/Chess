package com.chess.engine.player;

import com.chess.engine.PieceColor;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public class BlackPlayer extends Player{
    public BlackPlayer(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
        super(board, legalMoves, opponentMoves);
    }

    @Override
    protected Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    protected PieceColor getPieceColor() {
        return PieceColor.BLACK;
    }

    @Override
    protected Player getOpponent() {
        return this.board.whitePlayer();
    }
}
