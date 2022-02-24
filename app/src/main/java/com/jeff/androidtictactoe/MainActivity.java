package com.jeff.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer, View.OnClickListener {

    private TicTacToeGame game;

    private Button[] boardBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);
        game = new TicTacToeGame();
        game.addObserver(this);
        boardBtns = new Button[]{
            findViewById(R.id.button00),
            findViewById(R.id.button10),
            findViewById(R.id.button20),
            findViewById(R.id.button01),
            findViewById(R.id.button11),
            findViewById(R.id.button21),
            findViewById(R.id.button02),
            findViewById(R.id.button12),
            findViewById(R.id.button22)
        };
        for (Button btn : boardBtns) btn.setOnClickListener(this); // add listeners

        try {
            game.makeMove(0,0, TicTacToeGame.State.X);
        } catch (TicTacToeGame.IllegalMoveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object arg) {
        System.out.println("OBSERVED!");
        TicTacToeGame game = TicTacToeGame.class.cast(observable);
        for (int i=0;i < boardBtns.length; i++) {
            boardBtns[i].setText("X");
        }
    }

    @Override
    public void onClick(View view) {
        int[] pos = findInBoard(view);
        System.out.println(pos[0] + ", " + pos[1]);
        try {
            game.makeMove(pos[0], pos[1], TicTacToeGame.State.O);
        } catch (TicTacToeGame.IllegalMoveException e) {
            e.printStackTrace();
        }
    }

    public int[] findInBoard(View view) {
        for (int i=0;i<boardBtns.length;i++) {
            if (boardBtns[i]==view) return new int[]{i%TicTacToeGame.bsize, i/TicTacToeGame.bsize};
        }
        return new int[]{-1,-1};
    }
}