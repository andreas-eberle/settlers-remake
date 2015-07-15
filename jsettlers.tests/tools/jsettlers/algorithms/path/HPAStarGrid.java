package jsettlers.algorithms.path;

import java.util.Random;

import jsettlers.algorithms.path.astar.IAStarPathMap;
import jsettlers.common.Color;
import jsettlers.common.CommonConstants;
import jsettlers.common.landscape.ELandscapeType;
import jsettlers.common.map.EDebugColorModes;
import jsettlers.common.map.IGraphicsBackgroundListener;
import jsettlers.common.map.IGraphicsGrid;
import jsettlers.common.map.partition.IPartitionData;
import jsettlers.common.mapobject.IMapObject;
import jsettlers.common.movable.IMovable;

public class HPAStarGrid {
	private final short width;
	private final short height;
	private final boolean[][] blocked;
	private final int[][] debugColors;

	public HPAStarGrid(final int width, final int height, float blockedPercentage) {
		this.width = (short) width;
		this.height = (short) height;
		blocked = new boolean[width][height];
		debugColors = new int[width][height];

		Random r = new Random(1234);
		for (int i = 0; i < width * height * blockedPercentage; i++) {
			int x = r.nextInt(width);
			int y = r.nextInt(height);
			blocked[x][y] = true;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	float getCost(IPathCalculatable requester, int sx, int sy, int tx, int ty) {
		return isBlocked(requester, tx, ty) ? Float.MAX_VALUE : 1;
	}

	void setDebugColor(int x, int y, Color color) {
		debugColors[x][y] = color == null ? 0 : color.getARGB();
	}

	short getBlockedPartition(int x, int y) {
		return 1;
	}

	boolean isBlocked(IPathCalculatable requester, int x, int y) {
		return blocked[x][y];
	}

	public void clearDebugColors() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				debugColors[x][y] = 0;
			}
		}
	}

	public IAStarPathMap getAStarMap() {
		return new IAStarPathMap() {

			@Override
			public void setDebugColor(int x, int y, Color color) {
				HPAStarGrid.this.setDebugColor(x, y, color);
			}

			@Override
			public void markAsOpen(int x, int y) {
				HPAStarGrid.this.setDebugColor(x, y, Color.ORANGE.colorWithAlpha(0.2f));
			}

			@Override
			public void markAsClosed(int x, int y) {
				HPAStarGrid.this.setDebugColor(x, y, Color.RED.colorWithAlpha(0.2f));
			}

			@Override
			public boolean isBlocked(IPathCalculatable requester, int x, int y) {
				return HPAStarGrid.this.isBlocked(requester, x, y);
			}

			@Override
			public float getCost(int sx, int sy, int tx, int ty) {
				return HPAStarGrid.this.getCost(null, sx, sy, tx, ty);
			}

			@Override
			public short getBlockedPartition(int x, int y) {
				return 1;
			}
		};
	}

	public IGraphicsGrid getGraphicsGrid() {
		return new IGraphicsGrid() {

			@Override
			public void setBackgroundListener(IGraphicsBackgroundListener backgroundListener) {
			}

			@Override
			public int nextDrawableX(int x, int y, int maxX) {
				return x + 1;
			}

			@Override
			public boolean isBorder(int x, int y) {
				return false;
			}

			@Override
			public short getWidth() {
				return width;
			}

			@Override
			public byte getVisibleStatus(int x, int y) {
				return CommonConstants.FOG_OF_WAR_VISIBLE;
			}

			@Override
			public byte getPlayerIdAt(int x, int y) {
				return 1;
			}

			@Override
			public IPartitionData getPartitionData(int x, int y) {
				return null;
			}

			@Override
			public IMovable getMovableAt(int x, int y) {
				return null;
			}

			@Override
			public IMapObject getMapObjectsAt(int x, int y) {
				return null;
			}

			@Override
			public ELandscapeType getLandscapeTypeAt(int x, int y) {
				return ELandscapeType.GRASS;
			}

			@Override
			public byte getHeightAt(int x, int y) {
				return 0;
			}

			@Override
			public short getHeight() {
				return height;
			}

			@Override
			public int getDebugColorAt(int x, int y, EDebugColorModes debugColorMode) {
				int debugColor = debugColors[x][y];
				return debugColor == 0 ? blocked[x][y] ? Color.BLACK.getARGB() : 0 : debugColor;
			}
		};
	}

}
