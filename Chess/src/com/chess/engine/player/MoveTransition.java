package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {

    private final Board fromBoard;
    private final Board toBoard;
    private final Move move;
    private final MoveStatus moveStatus;
    MoveTransition(final Board fromBoard,
                   final Board toBoard,
                   final Move move,
                   final MoveStatus moveStatus){
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getFromBoard(){
        return this.fromBoard;
    }

    public Board getToBoard(){
        return this.toBoard;
    }
}
