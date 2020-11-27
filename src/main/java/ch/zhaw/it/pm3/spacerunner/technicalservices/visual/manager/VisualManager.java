package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.DoubleCoinsPowerUp;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.ShieldPowerUp;
import ch.zhaw.it.pm3.spacerunner.domain.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.domain.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.domain.ShopContent;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGAnimationFiles;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualUtil;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;

/**
 * This is a Manager for the Visuals of the different VisualElement's.
 * It is implemented with the singleton-pattern.
 * The Manager was implemented because for example all Asteroids share the same image. {@literal =>} So it would make no sense to have the image in every element itself.
 * The image is set per Class of VisualElement (? extends VisualElement)
 *
 * @author islermic
 */
public class VisualManager {

    private final VisualUtil visualUtil = VisualUtil.getUtil();
    private final Persistence persistenceUtil = JsonPersistenceUtil.getUtil();

    private final static VisualManager VISUAL_MANAGER = new VisualManager();

    private int height = 500;
    private int width = 500;
    private Map<Class<? extends VisualElement>, Visual> visualList = new HashMap<>();
    private Map<Class<? extends VisualElement>, AnimatedVisual> animatedVisualList = new HashMap<>();

    private final Set<VisualManagerListener> visualManagerListeners = new HashSet<>();

    public static VisualManager getManager() {
        return VISUAL_MANAGER;
    }


    private VisualManager() {
    }


    /**
     * Clears the data of the manager. (reset)
     */
    public synchronized void clear() {
        visualList = new HashMap<>();
        animatedVisualList = new HashMap<>();
    }

    /**
     * Setup visuals for the game.
     * If a new element is implemented, add code for the element in this function so it has an image at the start.
     */
    public void loadGameElementVisuals() {
        visualManagerListeners.forEach(VisualManagerListener::clear);


        PlayerProfile playerProfile = persistenceUtil.loadProfile();
        Set<ShopContent> activeShopContents = playerProfile.getActiveShopContent();

        VISUAL_MANAGER.loadAndSetVisual(SpaceShip.class, new Visual(VisualSVGFile.SPACE_SHIP_1, VisualScaling.SPACE_SHIP, true, false));

        for (ShopContent activeShopContent : activeShopContents) {
            if (activeShopContent.getItemType() == ItemType.PLAYER_MODEL) {
                VISUAL_MANAGER.loadAndSetVisual(SpaceShip.class, new Visual(activeShopContent.getImageId(), VisualScaling.SPACE_SHIP, false, false));
            }
        }

        VISUAL_MANAGER.loadAndSetVisual(UFO.class, new Visual(VisualSVGFile.UFO_1, VisualScaling.UFO));
        VISUAL_MANAGER.loadAndSetVisual(Asteroid.class, new Visual(VisualSVGFile.ASTEROID, VisualScaling.ASTEROID));
        VISUAL_MANAGER.loadAndSetVisual(DoubleCoinsPowerUp.class, new Visual(VisualSVGFile.DOUBLE_COIN_POWER_UP, VisualScaling.POWER_UP));
        VISUAL_MANAGER.loadAndSetVisual(ShieldPowerUp.class, new Visual(VisualSVGFile.SHIELD_POWER_UP, VisualScaling.POWER_UP));
        VISUAL_MANAGER.loadAndSetVisual(Rocket.class, new Visual(VisualSVGFile.ROCKET_1, VisualScaling.ROCKET));

        Visual background = new Visual(VisualFile.BACKGROUND_STARS);
        background.setIsBackground(true);
        VISUAL_MANAGER.loadAndSetVisual(SpaceWorld.class, background);
        VISUAL_MANAGER.loadAndSetVisual(Coin.class, new Visual(VisualSVGFile.SHINY_COIN_1, VisualScaling.COIN));

        AnimatedVisual coinAnimation = new AnimatedVisual(VisualSVGAnimationFiles.COIN_ANIMATION, VisualScaling.COIN);
        VISUAL_MANAGER.loadAndSetAnimatedVisual(Coin.class, coinAnimation);
    }

    /**
     * Returns the relative height of the specified class
     * @param elementClass class to get height
     * @return relative height
     * @throws VisualNotSetException if no visual set
     */
    public double getElementRelativeHeight(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        return getVisual(elementClass).getVisualScaling().getScaling();
    }

    /**
     * Returns the relative width of the specified class
     * @param elementClass class to get width
     * @return relative width
     * @throws VisualNotSetException if no visual set
     */
    public double getElementRelativeWidth(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        return getImage(elementClass).getWidth() / ((double) width);
    }

    /**
     * Loads the specified visual for the class.
     * @param elementClass class to add visual
     * @param visual visual to load and set
     */
    public void loadAndSetVisual(Class<? extends VisualElement> elementClass, Visual visual) {
        BufferedImage image;
        if (visual.getVisualFile() == null) {
            //load SVG
            image = getSVGBufferedImage(visual.getVisualSVGFile(), visual.getVisualScaling());
        } else {
            //load image file
            image = getBufferedImage(visual.getVisualFile());
        }

        image = flipVisual(visual.isFlipHorizontally(), visual.isFlipVertically(), image);
        if (visual.shouldResize()) {
            if (visual.isBackground()) {
                image = visualUtil.generateBackground(image, width, height);
            } else {
                image = visualUtil.resizeImage(image, visual.getResizeWidth(), visual.getResizeHeight());
            }
        }


        visual.setBufferedImage(image);

        BufferedImage changedImage = image;
        visualManagerListeners.forEach((visualManagerListener) -> {
            visualManagerListener.bufferedImageChanged(changedImage);
        });

        synchronized (this) {
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

    /**
     * Loads the specified animated visual for the class.
     * @param elementClass class to add visual animation
     * @param animatedVisual animation to load and set
     */
    public void loadAndSetAnimatedVisual(Class<? extends VisualElement> elementClass, AnimatedVisual animatedVisual) {
        VisualSVGFile[] svgFiles = animatedVisual.getVisualSVGFiles().getAnimationVisuals();

        List<Visual> visuals = new ArrayList<>();
        for (VisualSVGFile svgFile : svgFiles) {
            Visual currentVisual = new Visual(svgFile, animatedVisual.getVisualScaling());
            currentVisual.setBufferedImage(getSVGBufferedImage(svgFile, animatedVisual.getVisualScaling()));
            BufferedImage changedImage = currentVisual.getBufferedImage();
            visualManagerListeners.forEach((visualManagerListener) -> {
                visualManagerListener.bufferedImageChanged(changedImage);
            });
            visuals.add(currentVisual);
        }
        animatedVisual.setVisuals(visuals.toArray(Visual[]::new));
        synchronized (this) {
            animatedVisualList.put(elementClass, animatedVisual);
        }
    }

    /**
     * Gets the image of the class.
     * @param elementClass class to get image for
     * @return image for class
     * @throws VisualNotSetException if no visual set
     */
    public BufferedImage getImage(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        return getVisual(elementClass).getBufferedImage();
    }

    /**
     * Gets the visual of the class.
     * If there is an animation set, the animated visual is preferred. Else the visual is returned.
     *
     * @param elementClass class to get visual for
     * @return visual for class
     * @throws VisualNotSetException if no visual set
     */
    private Visual getVisual(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        Visual animatedVisual = getAnimatedVisual(elementClass);
        if (animatedVisual != null) {
            return animatedVisual;
        } else {
            synchronized (this) {
                Visual visual = visualList.get(elementClass);

                if (visual == null) {
                    throw new VisualNotSetException("Visual for " + elementClass.toString() + " was not set!");
                }
                return visual;
            }
        }
    }

    private synchronized Visual getAnimatedVisual(Class<? extends VisualElement> elementClass) {
        AnimatedVisual visualsForAnimation = animatedVisualList.get(elementClass);

        if (visualsForAnimation == null) {
            return null;
        } else {
            return visualsForAnimation.getCurrentVisual();
        }
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Sets the height and with managed by the manager.
     * All visuals will be reloaded with the new sizes.
     * @param width width in px
     * @param height height in px
     */
    public synchronized void setViewport(int width, int height) {
        this.width = width;
        this.height = height;
        for (Map.Entry<Class<? extends VisualElement>, Visual> classVisualEntry : visualList.entrySet()) {
            loadAndSetVisual(classVisualEntry.getKey(), classVisualEntry.getValue());
        }

        for (Map.Entry<Class<? extends VisualElement>, AnimatedVisual> classVisualEntry : animatedVisualList.entrySet()) {
            loadAndSetAnimatedVisual(classVisualEntry.getKey(), classVisualEntry.getValue());
        }
    }

    /**
     * Add Listener
     * @param visualManagerListener listener to add
     */
    public void addListener(VisualManagerListener visualManagerListener) {
        visualManagerListeners.add(visualManagerListener);
    }

    /**
     * Remove Listener
     * @param visualManagerListener listener to remove
     */
    public void removeListener(VisualManagerListener visualManagerListener) {
        visualManagerListeners.remove(visualManagerListener);
    }


}
