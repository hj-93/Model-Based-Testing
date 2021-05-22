package com.luugiathuy.games.reversi;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class PropertyTest {
    @Property public void PlayerMoveAvailableGameNotEnded(@From(ReversiGenerator.class) Reversi r) {
        char piece = (r.mIsBlackTurn) ? r.sBLACK_PIECE : r.sWHITE_PIECE;
        boolean playerMoveNotAvailable = (r.findValidMove(r.mBoard, piece, true)).isEmpty();
        if (!playerMoveNotAvailable) {
            assertEquals(r.mState, r.PLAYING);
        }
    }
//Hui
    //OneMovePlayedOnePieceAdded
    //DuringPLAYINGStateBothPlayersHaveAtLeastOnePoint
//Magnus
    //APieceIntersectingAnotherSameColoredPieceInStraightLineConvertsAllPiecesInBetween (wbw => www)
    //InvalidMoveShouldNotAffectBoard (reversi.isValidMove can be made use of here)
//John
    //TotalScoreShouldEqualPiecesOnTheBoard
    @Property
    public void scoreEqualTotalPieces(@From(ReversiGenerator.class) Reversi reversi) {
        reversi.calScore();
        assertEquals(reversi.getWhiteScore() + reversi.getBlackScore(), numberOfPieces(reversi.getBoard()));
    }

    @Property
    public void piecesIncreaseByOneAfterMove(@From(ReversiGenerator.class) Reversi reversi) {
        int currentPieces = numberOfPieces(reversi.getBoard());
        makeMove(reversi);
        assertEquals(currentPieces + 1, numberOfPieces(reversi.getBoard()));
    }

    //WhenPieceIsPlacedAtLeastOneEnemyPieceShouldChangeColor
    @Property
    public void atLeastOneColorShiftAfterMove(@From(ReversiGenerator.class) Reversi reversi) {
        char lastPlayer = reversi.mIsBlackTurn ? Reversi.sWHITE_PIECE : Reversi.sBLACK_PIECE;
        char currentPlayer = reversi.mIsBlackTurn ? Reversi.sBLACK_PIECE : Reversi.sWHITE_PIECE;
        char[][] preBoard = new char[8][8];
        for (int i = 0; i < Reversi.sBOARD_SIZE; i++) {
            for (int j = 0; j < Reversi.sBOARD_SIZE; j++) {
                preBoard[i][j] = reversi.getBoard()[i][j];
            }
        }
        makeMove(reversi);
        assertTrue(atLeastOneColorShift(preBoard, reversi.getBoard(), lastPlayer, currentPlayer));
    }

    private int numberOfPieces(char[][] board) {
        int pieces = 0;
        for (int i = 0; i < Reversi.sBOARD_SIZE; i++) {
            for (int j = 0; j < Reversi.sBOARD_SIZE; j++) {
                if (board[i][j] == Reversi.sBLACK_PIECE || board[i][j] == Reversi.sWHITE_PIECE) {
                    pieces++;
                }
            }
        }
        return pieces;
    }

    /**
     * Perform a move on the current board
     */
    private void makeMove(Reversi reversi) {
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
        reversi.mIsBlackTurn = !reversi.mIsBlackTurn;
    }

    private boolean atLeastOneColorShift(char[][] preBoard, char[][] postBoard, char lastPiece, char currentPiece) {
        for (int i = 0; i < Reversi.sBOARD_SIZE; i++) {
            for (int j = 0; j < Reversi.sBOARD_SIZE; j++) {
                if (preBoard[i][j] == lastPiece && postBoard [i][j] == currentPiece) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean effectedPiecesToBoard(Reversi reversi) {
        for (int i = 0; i < Reversi.sBOARD_SIZE; i++) {
            for (int j = 0; j < Reversi.sBOARD_SIZE; j++) {
                if (reversi.mIsEffectedPiece[i][j]
                        && !(reversi.getBoard()[i][j] == Reversi.sBLACK_PIECE
                        || reversi.getBoard()[i][j] == Reversi.sWHITE_PIECE)) {
                    return false;
                }
            }
        }
        return true;
    }

//Anders
    //WhenOnePlayerPlaysTheOthersScoreShouldDecrease
    //GameShouldEndWhenNeitherPlayerHasValidMoves

//Backups
    //TurnChangesWhenMoveIsPlayed
    @Property
    public void turnChangesAfterMove(@From(ReversiGenerator.class) Reversi reversi) {
        boolean currentBlackTurn = reversi.mIsBlackTurn;
        makeMove(reversi);
        assertNotEquals(currentBlackTurn, reversi.mIsBlackTurn);
    }

    //mIsEffectedPieceCorrelatesToActualBoard
    @Property
    public void effectedPiecesCorrelatesToBoard(@From(ReversiGenerator.class) Reversi reversi) {
        assertTrue(effectedPiecesToBoard(reversi));
    }
}