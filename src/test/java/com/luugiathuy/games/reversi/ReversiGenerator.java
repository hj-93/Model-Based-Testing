package com.luugiathuy.games.reversi;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class ReversiGenerator extends Generator<Reversi> {

    public ReversiGenerator() {
        super(Reversi.class);
    }

    @Override
    public Reversi generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        Reversi reversi = new Reversi();
        reversi.mIsCompTurn = sourceOfRandomness.nextBoolean();
        return reversi;
    }
}
