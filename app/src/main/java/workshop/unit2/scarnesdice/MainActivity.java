package workshop.unit2.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private enum Turn {
        USER, COMPUTER
    }

    private int userScore = 0;
    private int userTurnScore = 0;
    private int computerScore = 0;
    private int computerTurnScore = 0;
    private Turn turn = Turn.USER;
    private final String scoreboardFormat = "Your score: %d Computer Score: %d\nYour turn score: %d Computer turn score: %d";
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            rollDice();
            if (turn == Turn.COMPUTER && computerTurnScore < 20) {
                timerHandler.postDelayed(this, 750);
            } else if (turn == Turn.COMPUTER) {
                handleHoldClick(null);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateScoreboard();
    }

    private void computerTurn() {
        turn = Turn.COMPUTER;
        findViewById(R.id.roll_button).setClickable(false);
        findViewById(R.id.hold_button).setClickable(false);
        timerHandler.postDelayed(timerRunnable,0);
    }

    private void userTurn() {
        turn = Turn.USER;
        findViewById(R.id.roll_button).setClickable(true);
        findViewById(R.id.hold_button).setClickable(true);
    }

    public void handleRollClick(View view) {
        rollDice();
    }

    private void rollDice() {
        Random random = new Random();
        int num = random.nextInt(6);
        int resId = 0;

        switch (num) {
            case 0:
                resId = R.drawable.dice1;
                break;
            case 1:
                resId = R.drawable.dice2;
                break;
            case 2:
                resId = R.drawable.dice3;
                break;
            case 3:
                resId = R.drawable.dice4;
                break;
            case 4:
                resId = R.drawable.dice5;
                break;
            case 5:
                resId = R.drawable.dice6;
                break;
        }
        ImageView imageView = (ImageView) findViewById(R.id.die_face);
        imageView.setImageDrawable(getResources().getDrawable(resId,null));

        if (num == 0) {
            switchTurn();
        } else {
            if (turn == Turn.USER) {
                userTurnScore += num + 1;
            } else {
                computerTurnScore += num + 1;
            }
            updateScoreboard();
        }
    }

    public void handleHoldClick(View view) {
        if (turn == Turn.USER) {
            userScore += userTurnScore;
        } else {
            computerScore += computerTurnScore;
        }
        switchTurn();
    }

    public void handleResetClick(View view) {
        userTurn();
        userScore = 0;
        userTurnScore = 0;
        computerScore = 0;
        computerTurnScore = 0;
        updateScoreboard();
    }

    private void updateScoreboard() {
        TextView textView = (TextView) findViewById(R.id.scoreboard);
        textView.setText(String.format(scoreboardFormat,userScore,computerScore,userTurnScore, computerTurnScore));
    }

    private void switchTurn() {
        userTurnScore = 0;
        computerTurnScore = 0;
        updateScoreboard();
        if (turn == Turn.USER) {
            computerTurn();
        } else {
            timerHandler.removeCallbacks(timerRunnable);
            userTurn();
        }
    }
}
