package adapter.image;

import javax.swing.ImageIcon;

/**
 * Target interface for the Image Adapter.
 * It defines what the client (Swing GUI) expects to receive.
 */
public interface ImageTarget {
    ImageIcon getImageIcon();
}
