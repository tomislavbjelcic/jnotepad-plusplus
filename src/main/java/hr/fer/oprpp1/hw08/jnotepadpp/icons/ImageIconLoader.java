package hr.fer.oprpp1.hw08.jnotepadpp.icons;

import java.io.InputStream;

import javax.swing.ImageIcon;

class ImageIconLoader {
	
	static ImageIcon load(String iconName) {
		InputStream is = ImageIconLoader.class.getResourceAsStream(iconName);
		if (is==null)
			throw new RuntimeException();
		byte[] bytes = null;
		try (is) {
			bytes = is.readAllBytes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageIcon icon = new ImageIcon(bytes);
		return icon;
	}
	
}
