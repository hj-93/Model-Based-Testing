package com.luugiathuy.games.reversi;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.Arrays;

public class ReversiGenerator extends Generator<Reversi> {

    public ReversiGenerator() {
        super(Reversi.class);
    }

    @Override
    public Reversi generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        Reversi reversi = new Reversi();
        reversi.resetBoard();
        reversi.mIsCompTurn = sourceOfRandomness.nextBoolean();
        int iterations = sourceOfRandomness.nextInt(40);

        //System.out.println("Initial Board:");
        //printBoard(reversi);
        System.out.println("Iterations: " + iterations);


        for (int k = 0; k < iterations; k++) {
            //System.out.println(reversi.mIsBlackTurn);
            char piece = (reversi.mIsBlackTurn) ? Reversi.sBLACK_PIECE : Reversi.sWHITE_PIECE;
            // copy board to temp
            char[][] tempBoard = new char[8][8];
            for (int i = 0; i< Reversi.sBOARD_SIZE; ++i)
                System.arraycopy(reversi.mBoard[i], 0, tempBoard[i], 0, Reversi.sBOARD_SIZE);

            Agent.MoveCoord move = reversi.mAIAgent.findMove(tempBoard, piece);
            if (move != null)
            {
                //System.out.println(move.getRow() + " " + move.getCol());
                Reversi.effectMove(reversi.mBoard, piece, move.getRow(), move.getCol());
                reversi.addToMoveList(piece, move.getRow(), move.getCol());
                reversi.mNewPieceRow = move.getRow();
                reversi.mNewPieceCol = move.getCol();
            }
            //printBoard(reversi);
            reversi.mIsBlackTurn = !reversi.mIsBlackTurn;
        }

        return reversi;
    }

    private void printBoard(Reversi reversi) {
        for (int i = 0; i < reversi.mBoard.length; i++) {
            System.out.println(String.valueOf(reversi.mBoard[i]));
        }
    }
}
