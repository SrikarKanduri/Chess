package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator
        implements BoardEvaluator {

    private final static int CHECK_MATE_BONUS = 10000;
    private final static int CHECK_BONUS = 25;
    private final static int CASTLED_BONUS = 65;
    private final static int MOBILITY_BONUS = 1;
    private final static int CASTLE_CAPABLE_BONUS = 50;

    @Override
    public int evaluate(final Board board,
                        final int depth) {
        return score(board.whitePlayer(), depth) - score(board.blackPlayer(), depth);
    }

    private static int score(final Player player,
                             final int depth) {
        return mobility(player) +
                checkmate(player, depth) +
                castle(player) +
                pieceValue(player);
    }

    private static int pieceValue(final Player player) {
        int pieceValuationScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValuationScore += piece.getPieceValue();
        }
        return pieceValuationScore;
    }

    private static int mobility(final Player player) {
        return MOBILITY_BONUS * player.getLegalMoves().size();
    }

    private static int checkmate(final Player player,
                                 final int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS  * depthBonus(depth) : check(player, depth);
    }

    private static int check(final Player player,
                             final int depth) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : 100 * depth;
    }

    private static int castleCapable(Player player) {
        return (player.isKingSideCastleCapable() || player.isQueenSideCastleCapable()? CASTLE_CAPABLE_BONUS : 0);
    }

    private static int castle(final Player player) {
        return player.isCastled() ? CASTLED_BONUS : castleCapable(player);
    }



}