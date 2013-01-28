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

import java.util.Random;
import android.os.Bundle;

public class BubbleManager {
	int bubblesLeft;
	BmpWrap[] bubbles;
	int[] countBubbles;

	public BubbleManager(BmpWrap[] bubbles) {
		this.bubbles = bubbles;
		this.countBubbles = new int[bubbles.length];
		this.bubblesLeft = 0;
	}

	public void saveState(Bundle map) {
		map.putInt("BubbleManager-bubblesLeft", bubblesLeft);
		map.putIntArray("BubbleManager-countBubbles", countBubbles);
	}

	public void restoreState(Bundle map) {
		bubblesLeft = map.getInt("BubbleManager-bubblesLeft");
		countBubbles = map.getIntArray("BubbleManager-countBubbles");
	}

	public void addBubble(BmpWrap bubble) {
		countBubbles[findBubble(bubble)]++;
		bubblesLeft++;
	}

	public void removeBubble(BmpWrap bubble) {
		countBubbles[findBubble(bubble)]--;
		bubblesLeft--;
	}

	public int countBubbles() {
		return bubblesLeft;
	}

	public int nextBubbleIndex(Random rand) {
		int select = rand.nextInt() % bubbles.length;

		if (select < 0) {
			select = -select;
		}

		int count = -1;
		int position = -1;

		while (count != select) {
			position++;

			if (position == bubbles.length) {
				position = 0;
			}

			if (countBubbles[position] != 0) {
				count++;
			}
		}

		return position;
	}

	public BmpWrap nextBubble(Random rand) {
		return bubbles[nextBubbleIndex(rand)];
	}

	private int findBubble(BmpWrap bubble) {
		for (int i = 0; i < bubbles.length; i++) {
			if (bubbles[i] == bubble) {
				return i;
			}
		}

		return -1;
	}
}
