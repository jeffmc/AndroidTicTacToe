package com.jeff.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

// Jeff McMillan
// Java Programming 3/10/22
// This is an Android TicTacToe app.
// You play against a (unintelligent) CPU that will try to make moves to hinder your ability to succeed.

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private TicTacToeGame game;

    private Button[][] boardBtns;

    private TicTacToeGame.State playerState = TicTacToeGame.State.X, computerState = TicTacToeGame.State.O;

    // Setup program!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);
        game = new TicTacToeGame();
        boardBtns = new Button[][]{
            new Button[]{
                findViewById(R.id.button00),
                findViewById(R.id.button10),
                findViewById(R.id.button20)},
            new Button[]{
                findViewById(R.id.button01),
                findViewById(R.id.button11),
                findViewById(R.id.button21)},
            new Button[]{
                findViewById(R.id.button02),
                findViewById(R.id.button12),
                findViewById(R.id.button22)}
        };
//        for (Button btn : boardBtns) System.out.println(btn.getText());
        for (Button[] col : boardBtns)
            for (Button btn : col) {
                btn.setOnClickListener(this); // add listeners
                btn.setText("");
        }
//        try {
//            game.makeMove(0,0, playerState);
//        } catch (TicTacToeGame.IllegalMoveException e) {
////            e.printStackTrace();
//            System.err.println(e.getMessage());
//        }
        game.print();
        setButtonsToGame();
    }

    // Listens for all clicks on button grid
    @Override
    public void onClick(View view) {
//        Thread thr = Thread.currentThread();
//        System.out.println("Click: " + thr.getName() + ", " + thr.getId());
        Button btn = (Button) view;
        tryMoveWithButton(btn);
    }

    // Reset visual state of the board
    public void setButtonsToGame() {
        for (int i=0;i < boardBtns.length; i++) {
            Button[] col = boardBtns[i];
            for (int j=0;j < col.length; j++) {
                TicTacToeGame.State s = game.getStateAtPos(i,j);
                col[j].setText(s.str());
                col[j].setEnabled(s == TicTacToeGame.State.Empty);
            }
        }
    }

    // Try a move on the ttt board from this button.
    public void tryMoveWithButton(Button btn) {
        int[] pos = findInBoard(btn);
        try {
            game.makeMove(pos[0], pos[1], playerState);
            game.computerMove(computerState);
        } catch (TicTacToeGame.IllegalMoveException e) {
            e.printStackTrace();
//            btn.setText("E");
        }
        TicTacToeGame.State winner = game.gameWon();
        if (winner != null) {
            restartGame();
            String msg = (winner == TicTacToeGame.State.Empty) ? "CAT Game!" : winner.str() + " won the game!";
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }
        setButtonsToGame();
    }

    // Find button's position in the TTT 2d array
    public int[] findInBoard(View view) {
        for (int i=0;i<boardBtns.length;i++) {
            Button[] col = boardBtns[i];
            for (int j = 0; j <col.length;j++)
                if (col[j] == view)
                    return new int[]{i, j};
        }
        return new int[]{-1,-1};
    }

    // Reset state of board DOESN'T REFRESH UI!
    public void restartGame() {
        game = new TicTacToeGame();
    }
}