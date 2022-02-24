package com.jeff.androidtictactoe;

import java.util.Arrays;
import java.util.Observable;

public class TicTacToeGame extends Observable {

    private State[] board;
    public static final int bsize = 3;

    public TicTacToeGame() {
        board = new State[bsize*bsize];
        Arrays.fill(board, State.Empty);
        notifyObservers();
    }

    public TicTacToeGame(TicTacToeGame ref) {
        board = ref.board.clone();
        notifyObservers();
    }

    public void makeMove(int x, int y, State s) throws IllegalMoveException {
        if (s == null || s == State.Empty || x < 0 || y < 0 || x >= bsize || y >= bsize) throw new IllegalMoveException(x, y, s,this);
        int i = bsize*y + x;
        if (board[i] != State.Empty) throw new IllegalMoveException(x,y,s,this);
        board[i] = s;
        notifyObservers();
    }

    public State getStateAtIdx(int i) {
        return board[i];
    }

    public State gameWon() { // Returns the winner, or Empty if CAT game, null if board not full.
        for (State s : new State[]{State.X, State.O}) {
            if (board[0] == s && board[5] == s && board[8] == s)
                return s; // top left to bottom right diag
            if (board[2] == s && board[5] == s && board[6] == s)
                return s; // top right to bottom left diag
            for (int i = 0; i < bsize; i++) {
                if (board[i + bsize * 0] == s && board[i + bsize * 1] == s && board[i + bsize * 2] == s)
                    return s; // column
                if (board[i * bsize + 0] == s && board[i * bsize + 1] == s && board[i * bsize + 2] == s)
                    return s; // row
            }
        }
        for (int i = 0; i < board.length; i++)
            if (board[i] == State.Empty) return null; // Game not finished
        return State.Empty; // CAT game
    }

    public enum State {
        X, O, Empty;
    }

    public class IllegalMoveException extends Exception {
        public IllegalMoveException(int x, int y, State s, TicTacToeGame game) {
            super(x + ", " + y + ": " + s.toString());
        }
    }
}
