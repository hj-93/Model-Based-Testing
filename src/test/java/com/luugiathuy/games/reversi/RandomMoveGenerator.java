package com.luugiathuy.games.reversi;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class RandomMoveGenerator extends Generator<Agent.MoveCoord> {


    public RandomMoveGenerator() {
        super(Agent.MoveCoord.class);
    }
    @Override
    public Agent.MoveCoord generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        return new Agent.MoveCoord(sourceOfRandomness.nextInt(8), sourceOfRandomness.nextInt(8));
    }
}
