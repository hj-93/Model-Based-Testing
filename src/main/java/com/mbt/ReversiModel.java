package com.mbt;

import org.graphwalker.core.condition.EdgeCoverage;
//import org.graphwalker.core.condition.TimeDuration;
import org.graphwalker.core.generator.RandomPath;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.*;
import org.graphwalker.java.test.TestBuilder;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

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
    public void e_AIMove() {

    }

    @Override
    public void v_GameEnd() {

    }

    @Override
    public void e_TurnChangeNoMove() {

    }

    @Override
    public void e_PlayerStart() {

    }

    @Override
    public void e_EndGame() {

    }

    @Override
    public void e_PlayerMove() {

    }

    @Override
    public void v_JudgePlayerMove() {

    }

    @Override
    public void e_Start() {

    }

    @Override
    public void e_MoveRejected() {

    }

    @Override
    public void v_WhoStartFirst() {

    }

    @Override
    public void v_PlayerTurn() {

    }

    @Override
    public void v_Start() {

    }

    @Override
    public void v_AITurn() {

    }

    @Override
    public void e_MoveAccepted() {

    }

    @Override
    public void e_AIStart() {

    }

    @Test
    public void functionalTest() {
        new TestBuilder()
                .addModel(MODEL_PATH, new RandomPath(new EdgeCoverage(100)), "e_Start")
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
