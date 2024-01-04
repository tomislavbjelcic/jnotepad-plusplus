package hr.fer.oprpp1.hw08.jnotepadpp.icons;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class SaveIcons {
	
	private static final int w = 15;
	private static final int h = 15;
	public static final ImageIcon GREEN_SAVE_ICON = resize(ImageIconLoader.load("greenSaveIcon.png"), w, h);
	public static final ImageIcon RED_SAVE_ICON = resize(ImageIconLoader.load("redSaveIcon.png"), w, h);
	
	private static ImageIcon resize(ImageIcon original, int w, int h) {
		Image origImg = original.getImage();
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(origImg, 0, 0, w, h, null);
	    g2.dispose();
	    
	    original.setImage(resizedImg);
	    return original;
	}
	
}
