package com.chess.engine.board;
import com.chess.engine.BoardUtils;
import com.chess.engine.PieceColor;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

import java.util.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, PieceColor.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, PieceColor.BLACK);
        final Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, blackLegalMoves, whiteLegalMoves);
        this.currentPlayer = null;
    }

    private static List<Tile> createGameBoard(Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];

        for(int i=0;i<BoardUtils.NUM_TILES;i++)
        {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return Collections.unmodifiableList(Arrays.asList(tiles));
    }

    private Collection<Piece> calculateActivePieces(List<Tile> gameBoard, PieceColor pieceColor) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile: gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceColor() == pieceColor)
                    activePieces.add(piece);
            }
        }
        return Collections.unmodifiableList(activePieces);
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    @Override
    public String toString()
    {
        final StringBuilder stringBuilder= new StringBuilder();
        for(int i=0; i<BoardUtils.NUM_TILES; i++){
            stringBuilder.append(String.format("%3s",gameBoard.get(i).toString()));
            if((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0)
                stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece : pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }



    public Tile getTile(final int tilePosition)
    {
        return gameBoard.get(tilePosition);
    }

    public static Board createStandardBoard() {
        //Creating instance of a static class?
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(PieceColor.BLACK, 0));
        builder.setPiece(new Knight(PieceColor.BLACK, 1));
        builder.setPiece(new Bishop(PieceColor.BLACK, 2));
        builder.setPiece(new Queen(PieceColor.BLACK, 3));
        builder.setPiece(new King(PieceColor.BLACK, 4));
        builder.setPiece(new Bishop(PieceColor.BLACK, 5));
        builder.setPiece(new Knight(PieceColor.BLACK, 6));
        builder.setPiece(new Rook(PieceColor.BLACK, 7));
        builder.setPiece(new Pawn(PieceColor.BLACK, 8));
        builder.setPiece(new Pawn(PieceColor.BLACK, 9));
        builder.setPiece(new Pawn(PieceColor.BLACK, 10));
        builder.setPiece(new Pawn(PieceColor.BLACK, 11));
        builder.setPiece(new Pawn(PieceColor.BLACK, 12));
        builder.setPiece(new Pawn(PieceColor.BLACK, 13));
        builder.setPiece(new Pawn(PieceColor.BLACK, 14));
        builder.setPiece(new Pawn(PieceColor.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(PieceColor.WHITE, 48));
        builder.setPiece(new Pawn(PieceColor.WHITE, 49));
        builder.setPiece(new Pawn(PieceColor.WHITE, 50));
        builder.setPiece(new Pawn(PieceColor.WHITE, 51));
        builder.setPiece(new Pawn(PieceColor.WHITE, 52));
        builder.setPiece(new Pawn(PieceColor.WHITE, 53));
        builder.setPiece(new Pawn(PieceColor.WHITE, 54));
        builder.setPiece(new Pawn(PieceColor.WHITE, 55));
        builder.setPiece(new Rook(PieceColor.WHITE, 56));
        builder.setPiece(new Knight(PieceColor.WHITE, 57));
        builder.setPiece(new Bishop(PieceColor.WHITE, 58));
        builder.setPiece(new Queen(PieceColor.WHITE, 59));
        builder.setPiece(new King(PieceColor.WHITE, 60));
        builder.setPiece(new Bishop(PieceColor.WHITE, 61));
        builder.setPiece(new Knight(PieceColor.WHITE, 62));
        builder.setPiece(new Rook(PieceColor.WHITE, 63));
        //white to move
        builder.setMoveMaker(PieceColor.WHITE);
        //build the board
        return builder.build();
    }

    public static class Builder{
        Map<Integer, Piece> boardConfig;
        PieceColor nextMoveMaker;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece)
        {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final PieceColor nextMoveMaker)
        {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build()
        {
            return new Board(this);
        }
    }
}
