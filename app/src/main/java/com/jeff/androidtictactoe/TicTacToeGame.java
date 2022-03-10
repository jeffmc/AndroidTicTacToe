package com.jeff.androidtictactoe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.TreeMap;

public class TicTacToeGame {

    private State[] board;
    public static final int bsize = 3;

    public TicTacToeGame() {
        board = new State[bsize*bsize];
        Arrays.fill(board, State.Empty);
    }

    public TicTacToeGame(TicTacToeGame ref) {
        board = ref.board.clone();
    }

    // Affect the board using this player
    public void makeMove(int x, int y, State s) throws IllegalMoveException {
        State winner = gameWon();
        if (winner != null) throw new IllegalMoveException(winner, this);
        if (s == null || s == State.Empty || x < 0 || y < 0 || x >= bsize || y >= bsize) throw new IllegalMoveException(x, y, s,this);
        int i = bsize*y + x;
        if (board[i] != State.Empty) throw new IllegalMoveException(x,y,s,this);
        board[i] = s;
    }

    public void classicComputerMove(State s) throws IllegalMoveException {
        State winner = gameWon();
        if (winner != null) throw new IllegalMoveException(winner, this);
        for (int x=0;x<bsize;x++) {
            for (int y=0;y<bsize;y++) {
                try {
                    makeMove(x, y, s);
                    return;
                } catch (IllegalMoveException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    // Goal with computer move is to make the most impactful move by placing the next move where the most adjacent cells are filled.
    public void computerMove(State s) throws IllegalMoveException {
        State winner = gameWon();
        if (winner != null) throw new IllegalMoveException(winner, this);
        TreeMap<Integer, int[]> moves = new TreeMap<>();
        for (int x=0;x<bsize;x++) {
            for (int y=0;y<bsize;y++) {
                if (getStateAtPos(x,y) == State.Empty) {
                    moves.put(countAdjacency(x,y), new int[]{x,y});
                }
            }
        }
        int[] move = moves.descendingMap().firstEntry().getValue();
        makeMove(move[0], move[1], s);
    }


    public int countAdjacency(int x, int y) { //Returns number of cells filled by computer or player adjacently (diagonals included)
        final int[] offsets = new int[]{-bsize-1,-bsize, -bsize+1,-1,1,bsize-1,bsize,bsize+1}; // ALL 8 spots around chosen
        int oi = y*bsize + x; // original index
        int a = 0;
        for (int o=0;o< offsets.length;o++) {
            int i = oi + offsets[o];
            if (i > -1 && i < board.length) {
                if (getStateAtPos(i) != State.Empty) a++;
            }
        }
        return a;
    }

    public State getStateAtPos(int i) {
        return board[i];
    }

    // Print to console
    public void print() {
        for (int o=0;o<bsize*bsize;o+=bsize) {
            System.out.println("[ " + board[o].str() + " " + board[o+1].str() + " " + board[o+1].str() + " ]");
        }
    }

    public State getStateAtPos(int x, int y) {
        return board[y * bsize + x];
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

    // Used to describe state of TTT grid.
    public enum State {
        X("X"), O("O"), Empty("-");
        private String str;
        State(String s) { str = s; }
        public String str() { return str; }


    }
    // Exceptions for making moves, to be handled by view
    public class IllegalMoveException extends Exception {
        public IllegalMoveException(int x, int y, State s, TicTacToeGame game) {
            super("INVALID MOVE: " + x + ", " + y + ": " + s.str());
        }
        public IllegalMoveException(State s, TicTacToeGame game) {
            super("GAME OVER: " + s.str() + " already won the game!");
        }
    }
}
