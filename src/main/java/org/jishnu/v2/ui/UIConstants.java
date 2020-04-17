package org.jishnu.v2.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class UIConstants {
    public static final String trackFilePath = "src/main/resources/Track.png";
    public static BufferedImage track;
    public static boolean[][] trackLayout;
    private static Logger logger = Logger.getLogger(UIConstants.class.getName());

    static {
        try {
            track = ImageIO.read(new File(UIConstants.trackFilePath));
            trackLayout = new boolean[track.getWidth()][track.getHeight()];
            int red = 0;
            for(int i = 0; i < trackLayout.length; i++)
                for(int j = 0; j < trackLayout[i].length; j++) {
                    red = (track.getRGB(i, j) >> 16) & 0x000000FF;
                    if(red < 255)
                        trackLayout[i][j] = true;
                }

        } catch (IOException e) {
            logger.severe("Error reading track image file");
        }

    }
}
