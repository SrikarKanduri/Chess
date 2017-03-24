package com.chess.gui;

import com.chess.engine.BoardUtils;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.chess.engine.board.Move.MoveFactory.*;
import static javax.swing.SwingUtilities.*;

public class Table extends Observable{
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private Move computerMove;

    private final static Dimension OUTER_FRAME_DIMENSION=new Dimension(750,600);
    private final static Dimension BOARD_PANEL_DIMENSION=new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION=new Dimension(10,10);

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    private boolean highlightLegalMoves;

    private static final Table INSTANCE = new Table();

    private Table(){
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar menuBar= populateMenuBar();
        this.gameFrame.setJMenuBar(menuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);

        this.gameFrame.add(takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.setVisible(true);

        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = true;
    }

    public static Table get(){
        return INSTANCE;
    }

    public void show() {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    private GameSetup getGameSetup(){
        return this.gameSetup;
    }

    private Board getGameBoard(){
        return this.chessBoard;
    }

    private JMenuBar populateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createPreferencesMenu());
        menuBar.add(createOptionsMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Open PGN");
        openPGN.addActionListener(e ->
                System.out.print("You opened PGN!!"));
        fileMenu.add(openPGN);
        final JMenuItem quitGame = new JMenuItem("Quit");
        quitGame.addActionListener(e ->
                System.exit(0));
        fileMenu.add(quitGame);
        return fileMenu;
    }

    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoard = new JMenuItem("Flip Board");
        flipBoard.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        preferencesMenu.add(flipBoard);
        preferencesMenu.addSeparator(); //Line after menu item
        final JCheckBox legalsHighlighterCheckbox = new JCheckBox("Highlight Legal Moves", true);
        legalsHighlighterCheckbox.addActionListener(e ->
                highlightLegalMoves = legalsHighlighterCheckbox.isSelected());
        preferencesMenu.add(legalsHighlighterCheckbox);
        return preferencesMenu;
    }

    private JMenu createOptionsMenu(){
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem setupGameOption = new JMenuItem("Setup Game");
        setupGameOption.addActionListener(e -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
        });
        optionsMenu.add(setupGameOption);
        return optionsMenu;
    }

    private void setupUpdate(final GameSetup gameSetup){
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher implements Observer{
        @Override
        public void update(final Observable o, final Object arg) {
            //If current player is AI, think.
            if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) &&
                    !Table.get().getGameBoard().currentPlayer().isInCheckMate() &&
                    !Table.get().getGameBoard().currentPlayer().isInStaleMate()){
                final AIThinkTank aiThinkTank = new AIThinkTank();
                aiThinkTank.execute();
            }

            if(Table.get().getGameBoard().currentPlayer().isInCheckMate()){
                //JOptionPane.showMessageDialog(Table.get().getBoard);
                System.out.print("GameOver!" + "In Checkmate:" + Table.get().getGameBoard().currentPlayer().isInCheckMate());
            }

            if(Table.get().getGameBoard().currentPlayer().isInStaleMate()){
                //JOptionPane.showMessageDialog(Table.get().getBoard);
                System.out.print("GameOver!" + "In Stalemate:" + Table.get().getGameBoard().currentPlayer().isInStaleMate());
            }
        }
    }

    public void updateGameBoard(final Board board){
        this.chessBoard = board;
    }

    public void updateComputerMove(final Move move){
        this.computerMove = move;
    }

    private MoveLog getMoveLog(){
        return this.moveLog;
    }

    private GameHistoryPanel getGameHistoryPanel(){
        return this.gameHistoryPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel(){
        return this.takenPiecesPanel;
    }

    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }

    private void moveMadeUpdate(final PlayerType playerType){
        setChanged();
        notifyObservers(playerType);
    }

    private static class AIThinkTank extends SwingWorker<Move, String>{

        private AIThinkTank(){

        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new MiniMax(Table.get().getGameSetup().getSearchDepth());
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }

        @Override
        public void done(){
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    public enum PlayerType{
        HUMAN,
        COMPUTER
    }

    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };
        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();

    }

    public class BoardPanel extends JPanel{
        List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            boardTiles = new ArrayList<>();
            for(int i=0; i< BoardUtils.NUM_TILES; i++){
                TilePanel tilePanel = new TilePanel(this, i);
                boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel tilePanel: boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public class TilePanel extends JPanel{
        final int tileId;

        TilePanel(final BoardPanel boardPanel,
                  final int tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTileIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(isRightMouseButton(e)){
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    else if(isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = createMove(chessBoard,
                                    sourceTile.getTileCoordinate(),
                                    destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                    }
                            invokeLater(() -> {
                                gameHistoryPanel.redo(chessBoard, moveLog);
                                takenPiecesPanel.redo(moveLog);
                                if(gameSetup.isAIPlayer(chessBoard.currentPlayer())){
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);
                            });
                    }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

            });
            validate();
        }

        public void drawTile(final Board board){
            assignTileColor();
            assignTileIcon(board);
            highlightLegals(chessBoard);
            validate();
            repaint();
        }

        private void assignTileColor() {
            if(BoardUtils.EIGHTH_RANK[this.tileId] ||
                    BoardUtils.SIXTH_RANK[this.tileId] ||
                    BoardUtils.FOURTH_RANK[this.tileId] ||
                    BoardUtils.SECOND_RANK[this.tileId]){
                setBackground(this.tileId % 2 == 0? lightTileColor : darkTileColor);
            }
            else{
                setBackground(this.tileId % 2 != 0? lightTileColor : darkTileColor);
            }

        }

        private void assignTileIcon(final Board board) {
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                try {
                    final BufferedImage image = ImageIO.read(new File(
                            BoardUtils.IMAGES_PATH +
                                    board.getTile(this.tileId).getPiece().getPieceColor().toString().substring(0,1) +
                                    board.getTile(this.tileId).getPiece().toString() +
                                    ".gif"
                    ));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void highlightLegals(final Board board){
            if(highlightLegalMoves){
                if(humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.currentPlayer().getPieceColor()){
                    for(Move move: pieceLegalMoves(board)){
                        if(move.getDestinationCoordinate() == this.tileId){
                            try {
                                add(new JLabel(new ImageIcon(
                                    ImageIO.read(new File(
                                     "C:\\Users\\srika\\Desktop\\Srikar Stuff\\Major_Project\\Chess\\art\\misc\\green_dot.png")))));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private Collection<Move> pieceLegalMoves(Board board) {
        if(humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.currentPlayer().getPieceColor()) {
            //Castling highlight for King -- Change if needed. (Made calculateKingCastles() public in Player)
            if(humanMovedPiece.getPieceType().isKing()) {
                return  ImmutableList.copyOf(
                        Iterables.concat(humanMovedPiece.calculateLegalMoves(board),
                        board.currentPlayer().calculateKingCastles(board.currentPlayer().getOpponent().getLegalMoves())));
            }
            return humanMovedPiece.calculateLegalMoves(board);
        }
        return Collections.emptyList();
    }

    public static class MoveLog{
        private final List<Move> moves;

        MoveLog(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves(){
            return this.moves;
        }

        public void addMove(final Move move){
            this.moves.add(move);
        }

        public int size(){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }

        public boolean removeMove(Move move){
            return this.moves.remove(move);
        }

    }
}
