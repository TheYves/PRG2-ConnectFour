package ch.hslu.prg2.hs14.team7.gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 12.12.2014.
 */
public class BufferedImageLoader {
	private BufferedImage image;

	public BufferedImage load(String path) throws IOException {
		image = ImageIO.read(getClass().getResource(path));
		return image;
	}
}
