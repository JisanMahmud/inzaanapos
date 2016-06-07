package com.inzaana.pos.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil
{
	/**
	 *
	 * @param b
	 * @return
	 */
	public static BufferedImage readImage(byte[] b)
	{
		if (b == null)
		{
			return null;
		}
		else
		{
			try
			{
				return ImageIO.read(new ByteArrayInputStream(b));
			}
			catch (IOException e)
			{
				return null;
			}
		}
	}

	/**
	 *
	 * @param img
	 * @return
	 */
	public static byte[] writeImage(BufferedImage img)
	{
		if (img == null)
		{
			return null;
		}
		else
		{
			try
			{
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				ImageIO.write(img, "png", b);
				b.flush();
				b.close();
				return b.toByteArray();
			}
			catch (IOException e)
			{
				return null;
			}
		}
	}
}
