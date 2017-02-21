package com.chess.gui;

import com.chess.engine.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TakenPiecesPanel extends JPanel {
    private final JPanel northPanel;
    private final JPanel southPanel;

    private final Color PANEL_COLOR = Color.decode("0xFDFE6");
    private final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private final Dimension TAKEN_PIECES_DIMENSION = new Dimension(80, 20);

    TakenPiecesPanel(){
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog movelog){
        this.northPanel.removeAll();
        this.southPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move move: movelog.getMoves()){
            if(move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceColor().isWhite())
                    whiteTakenPieces.add(takenPiece);
                else
                    blackTakenPieces.add(takenPiece);
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for(final Piece takenPiece: whiteTakenPieces){
            try {
                final BufferedImage image = ImageIO.read(new File(BoardUtils.IMAGES_PATH+
                        takenPiece.getPieceColor().toString().substring(0,1)+
                        takenPiece.toString()));
                final ImageIcon icon = new ImageIcon(image);
                //WRONG? new JLabel() without "icon"
                this.southPanel.add(new JLabel(icon));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(final Piece takenPiece: blackTakenPieces){
            try {
                final BufferedImage image = ImageIO.read(new File(BoardUtils.IMAGES_PATH+
                        takenPiece.getPieceColor().toString().substring(0,1)+
                        takenPiece.toString()));
                final ImageIcon icon = new ImageIcon(image);
                this.southPanel.add(new JLabel(icon));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }
}
