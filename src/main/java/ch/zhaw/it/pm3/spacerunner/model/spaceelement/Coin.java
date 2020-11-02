package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends SpaceElement {

    private static BufferedImage[] coinVisuals = null;
    private static long animationTimeStamp;
    private static int animationStepTime = 250;
    private static int animationPointer;
    private static BufferedImage visual;


    public Coin(Point startPosition, int width, int height) {
        super(startPosition, width, height);
    }

    public static void setCoinAnimationVisuals(BufferedImage[] coinVisuals, int animationStepTime){
        Coin.coinVisuals = coinVisuals;
        animationPointer = 0;
        animationTimeStamp = System.currentTimeMillis();
        Coin.animationStepTime = animationStepTime;
    }

    public static void setVisual(BufferedImage visual){
        Coin.visual = visual;
    }

    @Override
    public BufferedImage getVisual() throws VisualNotSetException {
        if (visual == null && coinVisuals == null) {
            throw new VisualNotSetException("Visual for Coin was not set!");
        }

        if(coinVisuals != null){
            long currentTime = System.currentTimeMillis();

            if(currentTime - animationTimeStamp > animationStepTime){
                if(animationPointer < coinVisuals.length - 1){
                    animationPointer++;
                }else{
                    animationPointer = 0;
                }
                animationTimeStamp = System.currentTimeMillis();
            }

            return coinVisuals[animationPointer];
        }else{
            return Coin.visual;
        }

    }
}
