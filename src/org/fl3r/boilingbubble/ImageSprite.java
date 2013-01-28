/* Boiling Bubble
 * Copyright (c) 2012-2013 Armando "FL3R" Fiore
 * This code is distributed under the GNU General Public License
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.fl3r.boilingbubble;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import java.util.Vector;

public class ImageSprite extends Sprite {
	private BmpWrap displayedImage;

	public ImageSprite(Rect area, BmpWrap img) {
		super(area);

		this.displayedImage = img;
	}

	@Override
	public void saveState(Bundle map, Vector savedSprites) {
		if (getSavedId() != -1) {
			return;
		}
		super.saveState(map, savedSprites);
		map.putInt(String.format("%d-imageId", getSavedId()), displayedImage.id);
	}

	@Override
	public int getTypeId() {
		return Sprite.TYPE_IMAGE;
	}

	public void changeImage(BmpWrap img) {
		this.displayedImage = img;
	}

	@Override
	public final void paint(Canvas c, double scale, int dx, int dy) {
		Point p = super.getSpritePosition();
		drawImage(displayedImage, p.x, p.y, c, scale, dx, dy);
	}
}
