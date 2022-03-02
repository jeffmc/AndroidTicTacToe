package com.jeff.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer, OnClickListener {

    private TicTacToeGame game;

    private Button[][] boardBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);
        game = new TicTacToeGame();
        game.addObserver(this);
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
        try {
            game.makeMove(0,0, TicTacToeGame.State.X);
        } catch (TicTacToeGame.IllegalMoveException e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        game.print();
        setButtonsToGame();
    }

    @Override
    public void update(Observable observable, Object arg) {
        System.out.println("OBSERVED!");
        setButtonsToGame();
    }

    @Override
    public void onClick(View view) {
//        Thread thr = Thread.currentThread();
//        System.out.println("Click: " + thr.getName() + ", " + thr.getId());
        Button btn = (Button) view;
        tryMoveWithButton(btn);
    }

    public void setButtonsToGame() {
        for (int i=0;i < boardBtns.length; i++) {
            Button[] col = boardBtns[i];
            for (int j=0;j < col.length; j++) {
                col[j].setText(game.getStateAtPos(i,j).str());
            }
        }
    }

    public void tryMoveWithButton(Button btn) {
        int[] pos = findInBoard(btn);
        System.out.println(pos[0] + ", " + pos[1]);
        try {
            game.makeMove(pos[0], pos[1], TicTacToeGame.State.O);
            btn.setText("O");
        } catch (TicTacToeGame.IllegalMoveException e) {
            e.printStackTrace();
            btn.setText("E");
        }
    }

    public int[] findInBoard(View view) {
        for (int i=0;i<boardBtns.length;i++) {
            Button[] col = boardBtns[i];
            for (int j = 0; j <col.length;j++)
                if (col[j] == view)
                    return new int[]{i, j};
        }
        return new int[]{-1,-1};
    }
}