package server.world;

public class WorldObject {

	public int x, y, height, id, type;
	public static int face;
	public WorldObject() {

	}

	public WorldObject(int x, int y, int h, int id, int type, int face) {
		this.x = x;
		this.y = y;
		this.height = h;
		this.id = id;
		this.type = type;
		this.face = face;
	}

}