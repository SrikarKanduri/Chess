package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.gui.Table.MoveLog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GameHistoryPanel extends JPanel {

    private final DataModel model;
    private final JScrollPane scrollPane;
    private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(200, 800);

    GameHistoryPanel(){
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    void redo(final Board board, final MoveLog moveHistory){
        int currentRow = 0;
        this.model.clear();
        for(final Move move: moveHistory.getMoves()){
            final String moveText = move.toString();
            if(move.getMovedPiece().getPieceColor().isWhite()) {
                this.model.setValueAt(moveText, currentRow, 0);
            }
            else if(move.getMovedPiece().getPieceColor().isBlack()){
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }
        if(moveHistory.size() > 0){
            final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
            final String moveText = lastMove.toString();
            if(lastMove.getMovedPiece().getPieceColor().isWhite())
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow , 0);
            else if(lastMove.getMovedPiece().getPieceColor().isBlack()){
                this.model.setValueAt(moveText+ calculateCheckAndCheckMateHash(board), currentRow-1, 1);
            }
        }
        final JScrollBar vertical= scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private String calculateCheckAndCheckMateHash(final Board board) {
        if(board.currentPlayer().isInCheckMate()){
            return "#";
        } else if(board.currentPlayer().isInCheck()){
            return "+";
        }
        return "";
    }

    private static class DataModel extends DefaultTableModel {
        private final List<Row> values;
        private static final String[] NAMES = {"White", "Black"};

        DataModel(){
            this.values = new ArrayList<>();
        }

        public void clear(){
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount(){
            if(this.values == null)
                return 0;
            return this.values.size();
        }

        @Override
        public int getColumnCount(){
            return this.NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int col){
            final Row currentRow = this.values.get(row);
            if(col == 0) {
                return currentRow.getWhiteMove();
            } else if (col == 1){
                return currentRow.getBlackMove();
            }
            return null;

        }

        @Override
        public void setValueAt(final Object value, final int row, final int col){
            final Row currentRow;
            if(this.values.size() <= row){
                currentRow = new Row();
                this.values.add(currentRow);
            } else {
                currentRow = this.values.get(row);
            }
            if(col == 0){
                currentRow.setWhiteMove((String)value);
                fireTableRowsUpdated(row, row);
            } else if(col == 1){
                currentRow.setBlackMove((String)value);
                fireTableCellUpdated(row, col);
            }
        }

        @Override
        public Class<?> getColumnClass(final int col){
            return Move.class;
        }

        @Override
        public String getColumnName(final int col){
            return NAMES[col];
        }
    }

    private static class Row{
        private String whiteMove;
        private String blackMove;
        Row(){

        }

        public String getWhiteMove(){
            return this.whiteMove;
        }

        public void setWhiteMove(final String whiteMove) {
            this.whiteMove = whiteMove;
        }

        public String getBlackMove() {
            return this.blackMove;
        }

        public void setBlackMove(final String blackMove) {
            this.blackMove = blackMove;
        }
    }
}
