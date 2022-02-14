package com.mygdx.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] birds;
    Texture toptube, bottomtube;
    Random rand;
    Circle birdcircle;
//    ShapeRenderer shapeRenderer;
    Rectangle[] toptuberectangle;
    Rectangle[] bottomtuberectangle;
    BitmapFont font;
    Texture gameover;


    int score = 0;
    int scoringtube =0;
    int BIRD_ACTION = 0;
    float birdY;
    float valocity = 0;
    float gamestate = 0;
    float gravity = 2;
    float gap = 400;
    float max_gap = 0;
    int numberofTube = 4;
    float[] tubeX = new float[numberofTube];
    float[] tubeOffset = new float[numberofTube];
    float tubevalocity = 4;
    float distancebetweentubes;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        toptube = new Texture("toptube.png");
        bottomtube = new Texture("bottomtube.png");
        gameover = new Texture("gameover.png");
        rand = new Random();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        distancebetweentubes = Gdx.graphics.getWidth() * 3 / 5;
        birdcircle = new Circle();
//        shapeRenderer = new ShapeRenderer();
        toptuberectangle = new Rectangle[numberofTube];
        bottomtuberectangle = new Rectangle[numberofTube];
        gamestart();
    }

    public void gamestart(){
        birdY = (float) Gdx.graphics.getHeight() / 2 - birds[BIRD_ACTION].getHeight() / 2;
        for (int i = 0; i < numberofTube; i++) {

            tubeOffset[i] = (rand.nextFloat() - 0.5f) * (toptube.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 +Gdx.graphics.getWidth()+ i * distancebetweentubes;
            toptuberectangle[i]= new Rectangle();
            bottomtuberectangle[i] = new Rectangle();
        }
    }


    @Override
    public void render() {
        batch.begin();
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        shapeRenderer.setColor(Color.RED);
        if (gamestate == 1) {

            if (Gdx.input.isTouched()) {
                valocity = -20;
                Gdx.app.log("max_gap ", Float.toString(max_gap));


            }

            if(tubeX[scoringtube] < Gdx.graphics.getWidth()/2){
                score ++;
                Gdx.app.log("score ","score: "+Integer.toString(score)+"tubex: "+Float.toString(tubeX[scoringtube])+"width: "+Float.toString(Gdx.graphics.getWidth()/2));

                if(scoringtube < numberofTube -1){
                    scoringtube ++;
                }else {
                    scoringtube = 0;
                }
            }


            for (int i = 0; i < numberofTube; i++) {
                if (tubeX[i] < -toptube.getWidth()) {
                    tubeX[i] = (numberofTube * distancebetweentubes) - toptube.getWidth();
                    tubeOffset[i] = (rand.nextFloat() - 0.5f) * (toptube.getHeight() - gap - 200);
                } else {
                    tubeX[i] -= tubevalocity;
                }
                Gdx.app.log("tubex"," i "+Integer.toString(i)+" "+ Float.toString(tubeX[i]));
                toptuberectangle[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
                bottomtuberectangle[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 - bottomtube.getHeight() - gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
//                shapeRenderer.rect(toptuberectangle[i].x,toptuberectangle[i].y,toptuberectangle[i].getWidth(),toptuberectangle[i].getHeight());
//                shapeRenderer.rect(bottomtuberectangle[i].x,bottomtuberectangle[i].y,bottomtuberectangle[i].getWidth(),bottomtuberectangle[i].getHeight());

                batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - bottomtube.getHeight() - gap / 2 + tubeOffset[i]);
                batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                Gdx.app.log("tube offset ", Float.toString(tubeOffset[i]));

            }


            if (birdY > 0 && birdY < Gdx.graphics.getHeight() +5) {
                valocity = valocity + gravity;
                birdY -= valocity;
            }else {
                gamestate = 2;
            }
        } else if(gamestate == 0){

            if (Gdx.input.isTouched()) {
                gamestate = 1;
            }
        }else if(gamestate ==2){
//                birdY -= (+valocity);
                batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight() /2 - gameover.getHeight() /2);
            if(Gdx.input.isTouched()) {
                gamestart();
                gamestate = 1;
                scoringtube = 0;
                score = 0;
            }


        }


        if (BIRD_ACTION == 1) {
            BIRD_ACTION = 0;
        } else {
            BIRD_ACTION = 1;
        }

        font.draw(batch,Integer.toString(score),100,200);
        batch.draw(birds[BIRD_ACTION], Gdx.graphics.getWidth() / 2 - birds[BIRD_ACTION].getWidth() / 2, birdY);
        batch.end();

        birdcircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[BIRD_ACTION].getHeight() / 2, birds[BIRD_ACTION].getWidth() / 2);
//        shapeRenderer.circle(birdcircle.x, birdcircle.y, birdcircle.radius);
//        shapeRenderer.end();
        for(int i = 0; i < numberofTube ; i++) {
            if (Intersector.overlaps(birdcircle, toptuberectangle[i]) || Intersector.overlaps(birdcircle, bottomtuberectangle[i])) {
                gamestate = 2;
            }
        }


        Gdx.app.log("bird Y", Float.toString(birdY));

    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
