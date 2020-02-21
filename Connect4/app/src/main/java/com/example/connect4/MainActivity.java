package com.example.connect4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int[][] board;
    boolean isPlayerOne;
    boolean gameOver;
    int screenWidth;
    int screenHeight;
    Button nextButton;
    TableLayout table;
    private static final int TABLE_WIDTH = 7;
    private static final int TABLE_HEIGHT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        for (int y = 0; y < TABLE_HEIGHT; y++) {
            final int r = y;
            TableRow row = new TableRow(this);
            table.addView(row);
            for (int x = 0; x < TABLE_WIDTH; x++) {
                final int c = x;
                final Button button = new Button(this);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValidMove(r, c)) {
                            if (isPlayerOne) {
                                button.setBackgroundColor(getColor(R.color.colorPrimary));
                                board[r][c] = 1;
                            } else {
                                button.setBackgroundColor(getColor(R.color.colorAccent));
                                board[r][c] = 2;
                            }

                            Log.i("board", boardToString());
                            if (win(r, c)) {
                                gameOver = true;
                                int currentPlayer = (isPlayerOne) ? 1 : 2;
                                Toast.makeText(getApplicationContext(), "WINNER: Congrats player " + currentPlayer + "!", Toast.LENGTH_LONG).show();
                            }
                            isPlayerOne = !isPlayerOne;
                        }
                    }
                });
                row.addView(button);
                button.getLayoutParams().width = screenWidth / 7 - 7;
                button.getLayoutParams().height = screenHeight / 10 - 10;
            }
        }
    }

    private void initialize() {
        board = new int[TABLE_HEIGHT][TABLE_WIDTH];
        table = findViewById(R.id.tableLayout);
        isPlayerOne = true;
        gameOver = false;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        nextButton = findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GridLayoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidMove(int row, int col) {
        if (gameOver) {
            return false;
        }
        for (int r = 0; r < board.length; r++) {
            if (r > row && board[r][col] == 0) {
                return false;
            } else if (r <= row && board[r][col] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean win(int row, int col) {
        return checkHorizontal(row, col) || checkVertical(row, col) || diagOne(row, col)
                || diagTwo(row, col);
    }

    private boolean checkHorizontal(int row, int col) {
        int currentPlayer = (isPlayerOne) ? 1 : 2;
        int count = 1;
        int currentRow = row - 1;
        while (currentRow >= 0 && board[currentRow][col] == currentPlayer) {
            count++;
            currentRow--;
        }
        if (count >= 4) {
            return true;
        }

        currentRow = row + 1;
        while (currentRow < board.length && board[currentRow][col] == currentPlayer) {
            count++;
            currentRow++;
        }
        return count >= 4;
    }

    private boolean checkVertical(int row, int col) {
        int currentPlayer = (isPlayerOne) ? 1 : 2;
        int count = 1;
        int currentCol = col - 1;
        while (currentCol >= 0 && board[row][currentCol] == currentPlayer) {
            count++;
            currentCol--;
        }
        if (count >= 4) {
            return true;
        }

        currentCol = col + 1;
        while (currentCol < board[0].length && board[row][currentCol] == currentPlayer) {
            count++;
            currentCol++;
        }
        return count >= 4;
    }

    private boolean diagOne(int row, int col) {
        int currentPlayer = (isPlayerOne) ? 1 : 2;
        int count = 1;
        int currentRow = row - 1;
        int currentCol = col + 1;
        while (currentRow >= 0 && currentCol < board[0].length
                && board[currentRow][currentCol] == currentPlayer) {
            count++;
            currentRow--;
            currentCol++;
        }
        if (count >= 4) {
            return true;
        }

        currentRow = row + 1;
        currentCol = col - 1;
        while (currentRow < board.length && currentCol >= 0
                && board[currentRow][currentCol] == currentPlayer) {
            count++;
            currentRow++;
            currentCol--;
        }
        return count >= 4;
    }

    private boolean diagTwo(int row, int col) {
        int currentPlayer = (isPlayerOne) ? 1 : 2;
        int count = 1;
        int currentRow = row - 1;
        int currentCol = col - 1;
        while (currentRow >= 0 && currentCol >= 0
                && board[currentRow][currentCol] == currentPlayer) {
            count++;
            currentRow--;
            currentCol--;
        }
        if (count >= 4) {
            return true;
        }

        currentRow = row + 1;
        currentCol = col + 1;
        while (currentRow < board.length && currentCol < board[0].length
                && board[currentRow][currentCol] == currentPlayer) {
            count++;
            currentRow++;
            currentCol++;
        }
        return count >= 4;
    }

    private String boardToString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < board.length; r++) {
            sb.append("\n");
            for (int c = 0; c < board[0].length; c++) {
                sb.append(board[r][c]).append(" ");
            }
        }
        return sb.toString();
    }
}
