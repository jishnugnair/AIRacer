package org.jishnu.v2.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class UIConstants {
    public static final String TRACK_FILE_PATH = "src/main/resources/Track.png";
    public static BufferedImage track;
    public static boolean[][] trackLayout;
    private static Logger logger = Logger.getLogger(UIConstants.class.getName());

    static {
        try {
            track = ImageIO.read(new File(UIConstants.TRACK_FILE_PATH));
            trackLayout = new boolean[track.getWidth()][track.getHeight()];
            int color;
            for(int i = 0; i < trackLayout.length; i++)
                for(int j = 0; j < trackLayout[i].length; j++) {
                     color = track.getRGB(i, j) & 0x00FFFFFF;
                     if(color < 16777215) // check if the color is white
                         trackLayout[i][j] = true;
                }
        } catch (IOException e) {
            logger.severe("Error reading track image file");
        }

    }

    private UIConstants() {}
}
