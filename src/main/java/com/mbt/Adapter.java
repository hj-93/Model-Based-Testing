package com.mbt;

import com.luugiathuy.games.reversi.Agent;

import java.util.ArrayList;
import java.util.Random;

public class Adapter {
    private com.luugiathuy.games.reversi.Reversi reversiGame = com.luugiathuy.games.reversi.Reversi.getInstance();
    private char playerPiece;
    private char AIPiece;

    public final Agent.MoveCoord invalidCoord = new Agent.MoveCoord(reversiGame.sBOARD_SIZE, reversiGame.sBOARD_SIZE);
    public int piecesCountBeforePlay;

    public void init() {
        System.out.println("Adapter: init SUT");
    }

    public void reset() {
        System.out.println("Adapter: reset SUT");
    }

    public void newGame() {
        System.out.println("Adapter: new game");
        reversiGame.newGame();
        piecesCountBeforePlay = 4; // game start with 4 pieces on board
    }

    public void letPlayerStartFirst(boolean isPlayerStart) {
        System.out.println("Adapter: player start first: " + (isPlayerStart ? "yes":"no"));
        // white piece starts first
        playerPiece = isPlayerStart ? reversiGame.sWHITE_PIECE : reversiGame.sBLACK_PIECE;
        AIPiece = isPlayerStart ? reversiGame.sBLACK_PIECE : reversiGame.sWHITE_PIECE;
        reversiGame.setIsCompTurn(!isPlayerStart);
    }

    public boolean isCurrentTurnPlayer() {
        boolean isAITurn = reversiGame.getIsCompTurn();
        System.out.println("Adapter: Current turn is: " + (isAITurn ? "AI":"Player"));
        return !isAITurn;
    }

    public boolean isPlayerMovePossible() {
        boolean isPlayerMovePossible = !reversiGame.findValidMove(reversiGame.getBoard(), playerPiece, true).isEmpty();
        System.out.println("Adapter: Is player move possible: " + (isPlayerMovePossible ? "Yes":"No"));
        return isPlayerMovePossible;
    }

    public boolean isAIMovePossible() {
        boolean isAIMovePossible = !reversiGame.findValidMove(reversiGame.getBoard(), AIPiece, false).isEmpty();
        System.out.println("Adapter: Is AI move possible: " + (isAIMovePossible ? "Yes":"No"));
        return isAIMovePossible;
    }

    public boolean isMoveValid(Agent.MoveCoord coord) {
        // populate the suggested moves
        reversiGame.findValidMove(reversiGame.getBoard(), playerPiece, true);
        return reversiGame.getBoard()[coord.getRow()][coord.getCol()] ==
                (playerPiece == reversiGame.sWHITE_PIECE ? reversiGame.sSUGGEST_WHITE_PIECE : reversiGame.sSUGGEST_BLACK_PIECE);
    }

    public Agent.MoveCoord proposeValidPlayerMove() {
        System.out.println("Adapter: Propose a valid move");

        // populate the suggested moves
        ArrayList<Agent.MoveCoord> suggestedMoves = reversiGame.findValidMove(reversiGame.getBoard(), playerPiece, false);

        if (!suggestedMoves.isEmpty()) {
            Agent.MoveCoord suggestedMove = suggestedMoves.get(new Random().nextInt(suggestedMoves.size()));
            System.out.println("Adapter: valid Move: " + suggestedMove.getRow() + " " + suggestedMove.getCol());

            return suggestedMove;
        }
        else {
            return invalidCoord;
        }
    }

    public Agent.MoveCoord proposeInvalidPlayerMove() {
        System.out.println("Adapter: Propose an invalid move");
        char[][] board = reversiGame.getBoard();

        // populate the suggested moves
        reversiGame.findValidMove(board, playerPiece, true);

        // some randomness might be better since it now might use the (0,0) most of the time.
        for (int i = 0; i < reversiGame.sBOARD_SIZE; ++i) {
            for (int j = 0; j < reversiGame.sBOARD_SIZE; ++j) {
                if (board[i][j] != (playerPiece == reversiGame.sWHITE_PIECE ?
                        reversiGame.sSUGGEST_WHITE_PIECE : reversiGame.sSUGGEST_BLACK_PIECE)) {
                    System.out.println("Adapter: Invalid Move: " + i + " " + j);
                    return new Agent.MoveCoord(i, j);
                }
            }
        }
        return invalidCoord;
    }

    public int getTotalScores() {
        return reversiGame.getBlackScore() + reversiGame.getWhiteScore();
    }

    public int getTotalPieceCount() {
        // remove the suggested moves on the board
        int totalPieces = 0;
        char[][] board = reversiGame.getBoard();
        for (int i = 0; i < reversiGame.sBOARD_SIZE; ++i) {
            for (int j = 0; j < reversiGame.sBOARD_SIZE; ++j) {
                if(board[i][j] == reversiGame.sBLACK_PIECE || board[i][j] == reversiGame.sWHITE_PIECE) {
                    ++totalPieces;
                }
            }
        }
        return totalPieces;
    }

    public void effectMove(Agent.MoveCoord coord) {
        updatePiecesCounter();
        reversiGame.movePiece(coord.getRow(), coord.getCol());
    }

    public void updatePiecesCounter() {
        piecesCountBeforePlay = getTotalPieceCount();
    }
    public boolean isGameEnded() {
        return reversiGame.getGameState() == reversiGame.ENDED;
    }

    public void printBoard() {
        for (int i = 0; i < reversiGame.sBOARD_SIZE; ++i) {
            System.out.println(reversiGame.getBoard()[i]);
        }
    }
}