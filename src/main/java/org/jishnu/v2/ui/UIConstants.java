package org.jishnu.v2.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class UIConstants {
    private static final Logger logger = Logger.getLogger(UIConstants.class.getName());

    public static final String TRACK_FILE_PATH = "src/main/resources/Track.png";
    public static final BufferedImage track = initializeTrack();
    public static final boolean[][] trackLayout = createTrackLayout();

    private UIConstants() {}

    private static BufferedImage initializeTrack() {
        try {
            return ImageIO.read(new File(TRACK_FILE_PATH));
        } catch (IOException e) {
            logger.severe("Error reading track image file " + e.getLocalizedMessage());
            logger.severe("Closing application");
            System.exit(1);
        }
        return null;
    }

    private static boolean[][] createTrackLayout() {
            boolean[][] layout = new boolean[track.getWidth()][track.getHeight()];
            int color;
            for(int i = 0; i < layout.length; i++)
                for(int j = 0; j < layout[i].length; j++) {
                     color = track.getRGB(i, j) & 0x00FFFFFF;
                     if(color < 16777215) // check if the color is white
                         layout[i][j] = true;
                }
            return layout;
    }
}
