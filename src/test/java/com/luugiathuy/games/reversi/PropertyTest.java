package com.luugiathuy.games.reversi;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(JUnitQuickcheck.class)
public class PropertyTest {
    private int getTotalPieceCount(Reversi r) {
        int totalPieces = 0;
        char[][] board = r.getBoard();
        for (int i = 0; i < Reversi.sBOARD_SIZE; ++i) {
            for (int j = 0; j < Reversi.sBOARD_SIZE; ++j) {
                if(board[i][j] == Reversi.sBLACK_PIECE || board[i][j] == Reversi.sWHITE_PIECE) {
                    ++totalPieces;
                }
            }
        }
        return totalPieces;
    }

    @Property public void PlayerMoveAvailableGameNotEnded(@From(ReversiGenerator.class) Reversi r) {
        char piece = (r.mIsBlackTurn) ? Reversi.sBLACK_PIECE : Reversi.sWHITE_PIECE;
        r.getNextMove();
        boolean playerMoveNotAvailable = (Reversi.findValidMove(r.mBoard, piece, true)).isEmpty();
        if (!playerMoveNotAvailable) {
            assertEquals(r.mState, Reversi.PLAYING);
        }
    }

    //OneMovePlayedOnePieceAdded
    @Property public void OneMovePlayedOnePieceAdded(@From(ReversiGenerator.class) Reversi r) {
        char piece = (r.mIsBlackTurn) ? r.sBLACK_PIECE : r.sWHITE_PIECE;
        int prePieceCount = getTotalPieceCount(r);
        Agent.MoveCoord move = r.mAIAgent.findMove(r.getBoard(), piece);
        if (move != null) {
            Reversi.effectMove(r.mBoard, piece, move.getRow(), move.getCol());
            int newPieceCount = getTotalPieceCount(r);
            assertEquals(prePieceCount + 1, newPieceCount);
        }
    }

    //BothPlayersHaveAtLeastOnePointInPlayingState
    @Property public void BothPlayersHaveAtLeastOnePointInPlayingState(@From(ReversiGenerator.class) Reversi r) {
        if (r.mState == r.PLAYING) {
            assertTrue(r.getBlackScore() >= 1);
            assertTrue(r.getWhiteScore() >= 1);
        }
    }

//Magnus

    //TurnChangesWhenMoveIsPlayed
    @Property public void TurnChangesWhenMoveIsPlayed(@From(ReversiGenerator.class) Reversi r) {
        char piece = (r.mIsBlackTurn) ? Reversi.sBLACK_PIECE : Reversi.sWHITE_PIECE;
        ArrayList<Agent.MoveCoord> moves = Reversi.findValidMove(r.mBoard, piece, true);
        char suggestPiece = (r.mIsBlackTurn) ? r.sSUGGEST_BLACK_PIECE : r.sSUGGEST_WHITE_PIECE;
        if (!moves.isEmpty()) {
            final boolean currentTurn = r.mIsBlackTurn;
            int row = moves.get(0).getRow();
            int col = moves.get(0).getCol();

            //code taken from r.movePiece(), can't call actual method because it launches another game round where the AI makes a move is some cases
            Reversi.effectMove(r.mBoard, piece, row, col);
            r.mNewPieceRow = row;
            r.mNewPieceCol = col;

            // add to move list
            r.addToMoveList(piece, row, col);
            // notify the observer
            r.stateChange();

            // change turn
            r.changeTurn();

            assertNotEquals(currentTurn, r.mIsBlackTurn);
        }
    }
    private char[][] deepCopy(char[][] original) {
        if (original == null) {
            return null;
        }

        final char[][] result = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }

    //InvalidMoveShouldNotAffectBoard
    @Property public void InvalidMoveShouldNotAffectBoard(@From(ReversiGenerator.class) Reversi r, @From(RandomMoveGenerator.class) Agent.MoveCoord move) {
        char[][] previousBoard = deepCopy(r.mBoard);
        char piece = (r.mIsBlackTurn) ? Reversi.sBLACK_PIECE : Reversi.sWHITE_PIECE;
        if (!Reversi.isValidMove(r.mBoard, piece, move.getRow(), move.getCol())) {
            r.movePiece(move.getRow(), move.getCol());
            assertArrayEquals(previousBoard, r.getBoard());
        }
    }

    private void printBoard(char[][] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(String.valueOf(a[i]));
        }
    }
//John
    //TotalScoreShouldEqualPiecesOnTheBoard
    @Property
    public void scoreEqualTotalPieces(@From(ReversiGenerator.class) Reversi reversi) {
        reversi.calScore();
        assertEquals(reversi.getWhiteScore() + reversi.getBlackScore(), numberOfPieces(reversi.getBoard()));
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
    @Property public void WhenOnePlayerPlaysTheOthersScoreShouldDecrease(@From(ReversiGenerator.class) Reversi r) {
        char piece = (r.mIsBlackTurn) ? r.sBLACK_PIECE : r.sWHITE_PIECE;
        r.calScore();
        int blackScoreOld = r.getBlackScore();
        int whiteScoreOld = r.getWhiteScore();
        Agent.MoveCoord move = r.mAIAgent.findMove(r.getBoard(), piece);
        if (move != null) {
            Reversi.effectMove(r.mBoard, piece, move.getRow(), move.getCol());
            r.calScore();
            int blackScore = r.getBlackScore();
            int whiteScore = r.getWhiteScore();

            if(r.mIsBlackTurn) {
                assertTrue(blackScore > blackScoreOld && whiteScore < whiteScoreOld);
            }
            else {
                assertTrue(blackScore < blackScoreOld && whiteScore > whiteScoreOld);
            }
        }
    }
    //GameShouldEndWhenNeitherPlayerHasValidMoves

//Backups
}