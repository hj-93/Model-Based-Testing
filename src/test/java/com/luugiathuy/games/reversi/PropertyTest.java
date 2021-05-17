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
    @Property public void PlayerMoveAvailableGameNotEnded(@From(ReversiGenerator.class) Reversi r) {
        char piece = (r.mIsBlackTurn) ? r.sBLACK_PIECE : r.sWHITE_PIECE;
        boolean playerMoveNotAvailable = (r.findValidMove(r.mBoard, piece, true)).isEmpty();
        if (!playerMoveNotAvailable) {
            assertEquals(r.mState, r.PLAYING);
        }
    }
}