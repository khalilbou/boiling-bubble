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

import android.os.Bundle;
import java.util.Vector;

public class LevelManager {
	private int currentLevel;
	private Vector levelList;

	public void saveState(Bundle map) {
		map.putInt("LevelManager-currentLevel", currentLevel);
	}

	public void restoreState(Bundle map) {
		currentLevel = map.getInt("LevelManager-currentLevel");
	}

	public LevelManager(byte[] levels, int startingLevel) {
		String allLevels = new String(levels);

		currentLevel = startingLevel;
		levelList = new Vector();

		int nextLevel = allLevels.indexOf("\n\n");
		if (nextLevel == -1 && allLevels.trim().length() != 0) {
			nextLevel = allLevels.length();
		}

		while (nextLevel != -1) {
			String currentLevel = allLevels.substring(0, nextLevel).trim();

			levelList.addElement(getLevel(currentLevel));

			allLevels = allLevels.substring(nextLevel).trim();

			if (allLevels.length() == 0) {
				nextLevel = -1;
			} else {
				nextLevel = allLevels.indexOf("\n\n");

				if (nextLevel == -1) {
					nextLevel = allLevels.length();
				}
			}
		}

		if (currentLevel >= levelList.size()) {
			currentLevel = 0;
		}
	}

	private byte[][] getLevel(String data) {
		byte[][] temp = new byte[8][12];

		for (int j = 0; j < 12; j++) {
			for (int i = 0; i < 8; i++) {
				temp[i][j] = -1;
			}
		}

		int tempX = 0;
		int tempY = 0;

		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) >= 48 && data.charAt(i) <= 55) {
				temp[tempX][tempY] = (byte) (data.charAt(i) - 48);
				tempX++;
			} else if (data.charAt(i) == 45) {
				temp[tempX][tempY] = -1;
				tempX++;
			}

			if (tempX == 8) {
				tempY++;

				if (tempY == 12) {
					return temp;
				}

				tempX = tempY % 2;
			}
		}

		return temp;
	}

	public byte[][] getCurrentLevel() {
		if (currentLevel < levelList.size()) {
			return (byte[][]) levelList.elementAt(currentLevel);
		}

		return null;
	}

	// fl3r: se finiti i livelli? sembra di sì!
	public void goToNextLevel() {
		currentLevel++;
		if (currentLevel >= levelList.size()) {

			currentLevel = 0;

		}

	}

	public void goToFirstLevel() {
		currentLevel = 0;
	}

	public int getLevelIndex() {
		return currentLevel;
	}
}
