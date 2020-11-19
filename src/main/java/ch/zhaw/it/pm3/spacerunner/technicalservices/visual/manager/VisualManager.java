package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup.DoubleCoinsPowerUp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup.ShieldPowerUp;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGAnimationFiles;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.ShopContent;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class VisualManager{

    private final VisualUtil visualUtil = VisualUtil.getInstance();
    private final PersistenceUtil persistenceUtil = PersistenceUtil.getUtil();

    private final static VisualManager visualManager = new VisualManager();

    private int height = 500;
    private int width = 500;
    private Map<Class<? extends VisualElement>, Visual> visualList = new HashMap<>();
    private Map<Class<? extends VisualElement>, AnimatedVisual> animatedVisualList = new HashMap<>();

    private final Set<VisualManagerListener> visualManagerListeners = new HashSet<>();

    public static VisualManager getManager(){
        return visualManager;
    }


    private VisualManager(){
    }

    public void loadGameElementVisuals(){
        visualManagerListeners.forEach(VisualManagerListener::clear);


        PlayerProfile playerProfile = persistenceUtil.loadProfile();
        Set<ShopContent> activeShopContents = playerProfile.getActiveShopContent();

        visualManager.loadAndSetVisual(SpaceShip.class, new Visual(VisualSVGFile.SPACE_SHIP_1, VisualScaling.SPACE_SHIP, true, false));

        for(ShopContent activeShopContent : activeShopContents){
            if(activeShopContent.getItemType() == ItemType.PLAYER_MODEL){
                visualManager.loadAndSetVisual(SpaceShip.class, new Visual(activeShopContent.getImageId(), VisualScaling.SPACE_SHIP, false, false));
            }
        }

        visualManager.loadAndSetVisual(UFO.class, new Visual(VisualSVGFile.UFO_1, VisualScaling.UFO));
        visualManager.loadAndSetVisual(Asteroid.class, new Visual(VisualSVGFile.ASTEROID, VisualScaling.ASTEROID));
        visualManager.loadAndSetVisual(DoubleCoinsPowerUp.class, new Visual(VisualSVGFile.DOUBLE_COIN_POWER_UP, VisualScaling.POWER_UP));
        visualManager.loadAndSetVisual(ShieldPowerUp.class, new Visual(VisualSVGFile.SHIELD_POWER_UP, VisualScaling.POWER_UP));
        visualManager.loadAndSetVisual(Rocket.class, new Visual(VisualSVGFile.ROCKET_1, VisualScaling.ROCKET));

        Visual background = new Visual(VisualFile.BACKGROUND_STARS);
        background.setIsBackground(true);
        visualManager.loadAndSetVisual(SpaceWorld.class, background);
        visualManager.loadAndSetVisual(Coin.class, new Visual(VisualSVGFile.SHINEY_COIN_1, VisualScaling.COIN));

        AnimatedVisual coinAnimation = new AnimatedVisual(VisualSVGAnimationFiles.COIN_ANIMATION, VisualScaling.COIN);
        visualManager.loadAndSetAnimatedVisual(Coin.class, coinAnimation);
    }

    public double getElementRelativeHeight(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        return getVisual(elementClass).getVisualScaling().getScaling();
    }

    public double getElementRelativeWidth(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        return getImage(elementClass).getWidth() / ((double)width);
    }

    public void loadAndSetVisual(Class<? extends VisualElement> elementClass, Visual visual){
        BufferedImage image;
        if(visual.getVisualFile() == null){
            //load SVG
            image = getSVGBufferedImage(visual.getVisualSVGFile(), visual.getVisualScaling());
        }else{
            //load image file
            image = getBufferedImage(visual.getVisualFile());
        }

        image = flipVisual(visual.isFlipHorizontally(), visual.isFlipVertically(), image);
        if(visual.shouldResize()){
            if(visual.isBackground()){
                image = visualUtil.generateBackground(image, width, height);
            }else{
                image = visualUtil.resizeImage(image, visual.getResizeWidth(), visual.getResizeHeight());
            }
        }


        visual.setBufferedImage(image);

        BufferedImage changedImage = image;
        visualManagerListeners.forEach((visualManagerListener)->{
            visualManagerListener.bufferedImageChanged(changedImage);
        });

        synchronized (this){
            visualList.put(elementClass, visual);
        }
    }

    private BufferedImage flipVisual(boolean flipHorizontally, boolean flipVertically, BufferedImage image) {
        if (flipHorizontally) {
            image = visualUtil.flipImage(image, true);
        }
        if (flipVertically) {
            image = visualUtil.flipImage(image, false);
        }
        return image;
    }

    private BufferedImage getSVGBufferedImage(VisualSVGFile imagePath, VisualScaling visualScaling) {
        URL imageURL = SpaceRunnerApp.class.getResource(imagePath.getFileName());
        return visualUtil.loadSVGImage(imageURL, (float) (height * visualScaling.getScaling()));
    }

    private BufferedImage getBufferedImage(VisualFile imagePath) {
        URL imageURL = SpaceRunnerApp.class.getResource(imagePath.getFileName());
        return visualUtil.loadImage(imageURL);
    }

    public void loadAndSetAnimatedVisual(Class<? extends VisualElement> elementClass, AnimatedVisual animatedVisual){
        VisualSVGFile[] svgFiles = animatedVisual.getVisualSVGFiles().getAnimationVisuals();

        List<Visual> visuals = new ArrayList<>();
        for(VisualSVGFile svgFile : svgFiles){
            Visual currentVisual = new Visual(svgFile, animatedVisual.getVisualScaling());
            currentVisual.setBufferedImage(getSVGBufferedImage(svgFile, animatedVisual.getVisualScaling()));
            BufferedImage changedImage = currentVisual.getBufferedImage();
            visualManagerListeners.forEach((visualManagerListener)->{
                visualManagerListener.bufferedImageChanged(changedImage);
            });
            visuals.add(currentVisual);
        }
        animatedVisual.setVisuals(visuals.toArray(Visual[]::new));
        synchronized (this) {
            animatedVisualList.put(elementClass, animatedVisual);
        }
    }

    public BufferedImage getImage(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        return getVisual(elementClass).getBufferedImage();
    }

    private Visual getVisual(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        Visual animatedVisual = getAnimatedVisual(elementClass);
        if(animatedVisual != null){
            return animatedVisual;
        }else{
            synchronized (this) {
                Visual visual = visualList.get(elementClass);

                if(visual == null){
                    throw new VisualNotSetException("Visual for " + elementClass.toString() + " was not set!");
                }
                return visual;
            }
        }
    }

    private synchronized Visual getAnimatedVisual(Class<? extends VisualElement> elementClass) {
        AnimatedVisual visualsForAnimation = animatedVisualList.get(elementClass);

        if(visualsForAnimation == null){
            return null;
        }else{
            return visualsForAnimation.getCurrentVisual();
        }
    }

    public synchronized void clear(){
        visualList = new HashMap<>();
        animatedVisualList = new HashMap<>();
    };

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public synchronized void setViewport(int width, int height) {
        this.width = width;
        this.height = height;
        for (Map.Entry<Class<? extends VisualElement>, Visual> classVisualEntry : visualList.entrySet()){
            loadAndSetVisual(classVisualEntry.getKey(), classVisualEntry.getValue());
        }

        for (Map.Entry<Class<? extends VisualElement>, AnimatedVisual> classVisualEntry : animatedVisualList.entrySet()){
            loadAndSetAnimatedVisual(classVisualEntry.getKey(), classVisualEntry.getValue());
        }
    }

    public void addListener(VisualManagerListener visualManagerListener) {
        visualManagerListeners.add(visualManagerListener);
    }

    public void removeListener(VisualManagerListener visualManagerListener) {
        visualManagerListeners.remove(visualManagerListener);
    }


}
