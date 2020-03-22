package com.example.covid19_game;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int current_position;
    int count;
    int dim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;
        current_position = -1;

        init_gui();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.finish();
    }

    private void init_gui(){

        drawGrid();
        init_restartButton();
    }

    /* function to draw the grid and initialize the cells */
    private void drawGrid(){
        LinearLayout container = findViewById(R.id.container);

        dim = 5;
        final int size = 5;

        for(int i=0; i < size; i++){
            LinearLayout riga = new LinearLayout(this);

            for(int j=0; j<size; j++){
                Button btn = new Button(this);
                btn.setId(i * size + j);
                btn.setText("");
                btn.setLayoutParams (new LinearLayout.LayoutParams(210, 210));
                btn.setOnClickListener(new View.OnClickListener(){
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {

                        if(current_position == -1) {
                            findViewById(v.getId()).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }

                        Button b = findViewById(v.getId());
                        if(check_position(current_position, v.getId()) == 0 && b.getText() == ""){

                            if(current_position != -1) {
                                findViewById(current_position).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            }

                            current_position = v.getId();
                            findViewById(current_position).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            b.setText(Integer.toString(count));
                            count++;

                            if(count==25){
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Wiiiin!");
                                alertDialog.setMessage("You won this match");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                return;
                            }
                            ArrayList temp = getPosition(current_position);
                            if(temp.isEmpty()) {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Loooooser!");
                                alertDialog.setMessage("You lost this match");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    }
                });
                riga.addView(btn);
            }
            container.addView(riga);
        }
    }

    private void init_restartButton(){
        Button btn_restart = findViewById(R.id.btn_restart);

        btn_restart.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                LinearLayout container = findViewById(R.id.container);
                container.removeAllViews();
                drawGrid();
                count = 0;
                current_position = -1;

            }
        });
    }


    /* function used to check if the gamer can move from possition
    'current_position' to the position index.
    return value:
     0 -> valid position
     1 -> invalid position
     2 -> doesn't exist valid position
    */
    private int  check_position(int current, int next){

        // case to handle the first click
        if(current == -1){
            //current_position = next;
            return 0;
        }

        //
        else{
            ArrayList possible_position = getPosition(current);
            if(possible_position.isEmpty())
                return 2;
            if(possible_position.contains(next))
                return 0;
        }

        return 1;
    }


    private ArrayList getPosition(int index){
        ArrayList vet = new ArrayList();

            // shift up
            if (index >= dim * 3)
                if(((Button)findViewById(index - dim * 3)).getText() == "")
                    vet.add(index - dim * 3);

            // shift right
            if ((index + 3) / dim == index / dim)
                if(((Button)findViewById(index + 3)).getText() == "")
                    vet.add(index + 3);

            // shift botton
            if (index < dim * dim - dim * 3)
                if(((Button)findViewById(index + dim * 3)).getText() == "")
                    vet.add(index + dim * 3);

            // shift left
            if ( index != 0 && index != 1 && index != 2 && (index - 3) / dim == index / dim)
                if(((Button)findViewById(index - 3)).getText() == "")
                    vet.add(index - 3);

            // shift up-right
            if (((index + 2) / dim == index / dim) && index < dim * dim - dim * 2)
                if(((Button)findViewById(index + dim * 2 + 2)).getText() == "")
                    vet.add(index + dim * 2 + 2);

            // shift down-right
            if (((index + 2) / dim == index / dim) && index >= dim * 2)
                if(((Button)findViewById(index - dim * 2 + 2)).getText() == "")
                    vet.add(index - dim * 2 + 2);

            // shift up-left
            if (index != 0 && index != 1 && ((index - 2) / dim == index / dim) && index < dim * dim - dim * 2)
                if(((Button)findViewById(index + dim * 2 - 2)).getText() == "")
                    vet.add(index + dim * 2 - 2);

            // shift down-left
            if (((index - 2) / dim == index / dim) && index >= dim * 2)
                if(((Button)findViewById(index - dim * 2 - 2)).getText() == "")
                    vet.add(index - dim * 2 - 2);


        return vet;
    }
}
