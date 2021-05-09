package com.mbt;

import com.luugiathuy.games.reversi.Agent;
import org.graphwalker.core.condition.EdgeCoverage;
//import org.graphwalker.core.condition.TimeDuration;
import org.graphwalker.core.condition.VertexCoverage;
import org.graphwalker.core.generator.RandomPath;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.*;
import org.graphwalker.java.test.TestBuilder;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@GraphWalker(value = "random(time_duration(1))", start = "e_Start")
public class ReversiModel extends ExecutionContext implements Reversi {
    public final static Path MODEL_PATH = Paths.get("com/mbt/Reversi.graphml");
    private Adapter reversiAdapter = new Adapter();

    @BeforeExecution
    public void setup() {
        System.out.println("Model: setup"); // http://graphwalker.github.io/fixtures/
        reversiAdapter.init();
    }

    @AfterExecution
    public void teardown() {
        System.out.println("Model: teardown"); // http://graphwalker.github.io/fixtures/
        reversiAdapter.reset();
    }

    @Override
    public void v_Start() {
        System.out.println("Model: v_Start");
    }

    @Override
    public void e_Start() {
        System.out.println("Model: e_Start");
    }

    @Override
    public void v_WhoStartFirst() {
        System.out.println("Model: v_WhoStartFirst");
    }

    @Override
    public void e_PlayerStart() {
        System.out.println("Model: e_PlayerStart");
        reversiAdapter.letPlayerStartFirst(true);
        reversiAdapter.newGame();
    }

    @Override
    public void e_AIStart() {
        System.out.println("Model: e_AIStart");
        reversiAdapter.letPlayerStartFirst(false);
        reversiAdapter.newGame();
    }

    @Override
    public void v_PlayerTurn() {
        System.out.println("Model: v_PlayerTurn");

        assertEquals(true, reversiAdapter.isCurrentTurnPlayer());

        setAttribute("isPlayerMovePossible", reversiAdapter.isPlayerMovePossible());
        setAttribute("isAIMovePossible", reversiAdapter.isAIMovePossible());
    }

    @Override
    public void e_PlayerMove() {
        System.out.println("Model: e_PlayerMove");

        Agent.MoveCoord validMove = reversiAdapter.proposeValidPlayerMove();

        // assert that at least one valid move is available when it reaches current edge
        assert ((reversiAdapter.invalidCoord.getRow() != validMove.getRow()) &&
                (reversiAdapter.invalidCoord.getCol() != validMove.getCol()));

        // 20% chance player goes for an invalid move
        if (Math.random() * 100 < 20) {
            System.out.println("Model: e_PlayerMove make an invalid move");
            Agent.MoveCoord invalidMove = reversiAdapter.proposeInvalidPlayerMove();
            setAttribute("playerMoveTrial", invalidMove);
        }
        // 80% chance player goes for a valid move
        else {
            System.out.println("Model: e_PlayerMove make a valid move");
            setAttribute("playerMoveTrial", validMove);
        }
    }

    @Override
    public void v_JudgePlayerMove() {
        System.out.println("Model: v_JudgePlayerMove");
        Agent.MoveCoord moveCoord = (Agent.MoveCoord)getAttribute("playerMoveTrial");
        setAttribute("isMoveValid", reversiAdapter.isMoveValid(moveCoord));
    }

    @Override
    public void e_MoveRejected() {
        System.out.println("Model: e_MoveRejected");
    }

    @Override
    public void e_MoveAccepted() {
        System.out.println("Model: e_MoveAccepted");
        reversiAdapter.effectMove((Agent.MoveCoord)getAttribute("playerMoveTrial"));
    }

    @Override
    public void e_TurnChangeNoMove() {
        System.out.println("Model: e_TurnChangeNoMove");
    }

    @Override
    public void v_AITurn() {
        System.out.println("Model: v_AITurn");

        // assert that game never end on AI turn.
        assertEquals(false, reversiAdapter.isGameEnded());
    }

    @Override
    public void e_AIMove() {
        System.out.println("Model: e_AIMove");
    }

    @Override
    public void e_EndGame() {
        System.out.println("Model: e_EndGame");
    }

    @Override
    public void v_GameEnd() {
        System.out.println("Model: v_GameEnd");

        // assert that game end
        assertEquals(true, reversiAdapter.isGameEnded());
        assertEquals(reversiAdapter.getTotalPieceCount(), reversiAdapter.getTotalPieceCount());
    }

    @Test
    public void functionalTest() {
        new TestBuilder()
                .addModel(MODEL_PATH, new RandomPath(new VertexCoverage(100)), "e_Start")
                .execute();
    }

    /*
    @Test
    public void stabilityTest() {
        new TestBuilder()
                .addModel(MODEL_PATH, new RandomPath(new TimeDuration(30, TimeUnit.SECONDS)), "e_Start")
                .execute();
    }
    */
}
