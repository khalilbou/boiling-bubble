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
import android.os.Bundle;

public class Compressor {
	private BmpWrap compressorHead;
	private BmpWrap compressor;
	int steps;

	public Compressor(BmpWrap compressorHead, BmpWrap compressor) {
		this.compressorHead = compressorHead;
		this.compressor = compressor;
		this.steps = 0;
	}

	public void saveState(Bundle map) {
		map.putInt("compressor-steps", steps);
	}

	public void restoreState(Bundle map) {
		steps = map.getInt("compressor-steps");
	}

	public void moveDown() {
		steps++;
	}

	public void paint(Canvas c, double scale, int dx, int dy) {
		for (int i = 0; i < steps; i++) {
			c.drawBitmap(compressor.bmp, (float) (235 * scale + dx),
					(float) ((28 * i - 4) * scale + dy), null);
			c.drawBitmap(compressor.bmp, (float) (391 * scale + dx),
					(float) ((28 * i - 4) * scale + dy), null);
		}
		c.drawBitmap(compressorHead.bmp, (float) (160 * scale + dx),
				(float) ((-7 + 28 * steps) * scale + dy), null);
	}
};
