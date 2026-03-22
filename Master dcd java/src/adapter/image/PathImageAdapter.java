package adapter.image;

import javax.swing.ImageIcon;
import java.io.File;

/**
 * Image Adapter that converts a String path representing an image
 * into a javax.swing.ImageIcon that Swing components can directly use.
 */
public class PathImageAdapter implements ImageTarget {
    
    private String imagePath;

    public PathImageAdapter(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public ImageIcon getImageIcon() {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return new ImageIcon(); // Return empty icon if no path
        }
        
        File file = new File(imagePath);
        if (file.exists()) {
            return new ImageIcon(imagePath);
        } else {
            // Fallback empty icon if file doesn't exist just so it doesn't break
            return new ImageIcon();
        }
    }
}
