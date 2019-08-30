package com.pram.puzzlegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pram.puzzlegame.Model.Grid;
import com.pram.puzzlegame.Model.Puzzle;
import com.pram.puzzlegame.Utility.Dimensions;
import com.pram.puzzlegame.Utility.DisplayConvert;
import com.pram.puzzlegame.Utility.LayoutWrapper;
import com.pram.puzzlegame.Utility.Position;
import com.pram.puzzlegame.Utility.Utils;

import java.util.Random;

public class PuzzleActivity extends AppCompatActivity {

    public static final int GRID_SIZE = 4;
    public static final int ANIMATION_DURATION = 1500;

    private Button againButton;
    private Button exitButton;
    private TextView scoreText;
    private TextView scoreLabel;
    private Chronometer chronometer;

    private LayoutWrapper layout;
    private Grid grid;
    private Puzzle[] puzzles;

    private Random random;
    private DisplayConvert displayConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        initViews();

        random = new Random();
        displayConvert = new DisplayConvert(this);

        initLayout();
        initSlotGrid();
        initPuzzles();

        attachPuzzles();
        setPuzzleListeners();

        chronometer.start();
    }

    private void initViews() {
        againButton = findViewById(R.id.puzzleAgain);
        exitButton = findViewById(R.id.puzzleExit);
        scoreText = findViewById(R.id.puzzleScoreText);
        scoreLabel = findViewById(R.id.puzzleScoreLabel);
        chronometer = findViewById(R.id.puzzleChronometer);

        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ImageActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
            }
        });
    }

    private void initLayout() {
        layout = new LayoutWrapper((RelativeLayout) findViewById(R.id.puzzleLayout), this);
        layout.setLayoutDimensions(Dimensions.LAYOUT_MARGIN);
    }

    private void initSlotGrid() {
        int slotSize = layout.height / 8;
        grid = new Grid(GRID_SIZE, slotSize, layout);
        grid.attachToLayout(layout);
    }

    private void initPuzzles() {
        Intent intent = getIntent();
        int imageIndex = intent.getIntExtra(ImageStore.IMAGE_INDEX_NAME, 0);
        Bitmap image = Utils.getBitmapFromDrawable(ImageStore.images[imageIndex], this);

        puzzles = makePuzzles(image, grid.slotSize, GRID_SIZE);

        Puzzle.setElevationForDisplay(displayConvert);
    }

    private void setPuzzleListeners() {
        for (Puzzle puzzle : puzzles) {
            setOnTouchListener(puzzle);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListener(final Puzzle puzzle) {
        final Position startPosition = new Position(puzzle.getX(), puzzle.getY());
        final Position deltaPosition = new Position();
        final Position newPosition = new Position();

        final GestureDetector tapDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                return true;
            }
        });

        puzzle.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View puzzleView, MotionEvent event) {
                puzzleView.getParent().bringChildToFront(puzzleView);

                if (tapDetector.onTouchEvent(event)) {
                    puzzle.lift();
                    puzzle.rotate(1);
                    puzzle.drop();
                }
                else {
                    final int rawX = (int) event.getRawX();
                    final int rawY = (int) event.getRawY();

                    newPosition.x = rawX - deltaPosition.x;
                    newPosition.y = rawY - deltaPosition.y;

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startPosition.x = puzzle.getX();
                            startPosition.y = puzzle.getY();

                            puzzle.lift();

                            deltaPosition.x = rawX - puzzle.getX();
                            deltaPosition.y = rawY - puzzle.getY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            puzzleTouchMove(puzzle, newPosition);
                            break;

                        case MotionEvent.ACTION_UP:
                            puzzleTouchUp(puzzle, startPosition, newPosition);
                            break;
                    }
                }

                return true;
            }
        });
    }

    private void puzzleTouchMove(Puzzle puzzle, Position newPosition) {
        if(layout.isInLayoutBounds(newPosition, grid.slotSize)) {
            puzzle.move(newPosition);

            if(grid.isInRange(newPosition)) {
                int nearestIndex = grid.getNearestIndex(newPosition);
                grid.unFocusAllSlots();
                grid.slots[nearestIndex].focus();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void puzzleTouchUp(final Puzzle puzzle, Position startPosition, Position newPosition) {
        final Position snapPosition;
        if(grid.isInRange(newPosition)) {
            int nearestIndex = grid.getNearestIndex(newPosition);
            if(nearestIndex == puzzle.index && puzzle.rotationLevel == 0) {
                snapPosition = grid.positions[nearestIndex];

                puzzle.view.setOnTouchListener(null);

                grid.matched++;
                if(grid.matched == grid.slots.length) {
                    finishPuzzles();
                }
            }
            else {
                snapPosition = startPosition;
            }
        }
        else {
            snapPosition = newPosition;
        }

        if(layout.isInLayoutBounds(snapPosition, grid.slotSize)) {
            puzzle.animateMove(snapPosition);
        }

        grid.unFocusAllSlots();

        puzzle.drop();
    }

    @SuppressLint("SetTextI18n")
    private void finishPuzzles() {
        chronometer.stop();
        chronometer.setAlpha(0.5f);

        String time = (String) chronometer.getText();
        String[] split = time.split(":");
        int min = Integer.parseInt(split[0]);
        int sec = Integer.parseInt(split[1]);
        scoreText.setText((min == 0 ? "" : min + "m ") + (sec == 0 ? "" : sec + "s"));

        againButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);
        scoreLabel.setVisibility(View.VISIBLE);
        scoreText.setVisibility(View.VISIBLE);

        againButton.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setInterpolator(new DecelerateInterpolator());
        exitButton.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setInterpolator(new DecelerateInterpolator());
        scoreLabel.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setInterpolator(new DecelerateInterpolator());
        scoreText.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).setInterpolator(new DecelerateInterpolator());
    }

    private void attachPuzzles() {
        for(Puzzle puzzle : puzzles) {
            puzzle.attachToLayout(layout);
        }
    }

    public Puzzle[] makePuzzles(Bitmap source, int puzzleSize, int gridSize) {
        Puzzle[] puzzles = new Puzzle[gridSize * gridSize];
        int snipSize = source.getHeight() / gridSize;

        Position randomPosition = new Position();
        int yOffset = grid.positions[grid.positions.length - 1].y + grid.slotSize + layout.margin;

        for(int y = 0; y < gridSize; y++) {
            for(int x = 0; x < gridSize; x++) {
                int i = gridSize * y + x;
                Bitmap image = Bitmap.createBitmap(source, x * snipSize, y * snipSize, snipSize, snipSize);

                Puzzle puzzle = new Puzzle(i, image, puzzleSize, this);
                puzzles[i] = puzzle;

                randomPosition.x = random.nextInt(layout.width - puzzleSize);
                randomPosition.y = yOffset + random.nextInt(layout.height - yOffset - puzzleSize);
                puzzle.move(randomPosition);

                puzzle.rotate(random.nextInt(4));
            }
        }

        return puzzles;
    }
}
