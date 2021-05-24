package com.luugiathuy.games.reversi;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.generator.Fields;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        char piece = (r.mIsBlackTurn) ? r.sBLACK_PIECE : r.sWHITE_PIECE;
        boolean playerMoveNotAvailable = (r.findValidMove(r.mBoard, piece, true)).isEmpty();
        if (!playerMoveNotAvailable) {
            assertEquals(r.mState, r.PLAYING);
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
    //APieceIntersectingAnotherSameColoredPieceInStraightLineConvertsAllPiecesInBetween (wbw => www)
    //InvalidMoveShouldNotAffectBoard (reversi.isValidMove can be made use of here)
//John
    //TotalScoreShouldEqualPiecesOnTheBoard
    //WhenPieceIsPlacedAtLeastOneEnemyPieceShouldChangeColor
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
    //TurnChangesWhenMoveIsPlayed
    //mIsEffectedPieceCorrelatesToActualBoard
}