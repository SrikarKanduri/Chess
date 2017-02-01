package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {

    private final Board board;
    private final Move move;
    private final MoveStatus moveStatus;
    MoveTransition(final Board board,
                   final Move move,
                   final MoveStatus moveStatus){
        this.board = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
