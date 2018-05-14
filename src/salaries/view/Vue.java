package salaries.view;

public class Vue {

	private int width;
	private int height;
	private String location;

	public Vue(String location, int width, int height) {

		this.location = location;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}

	public String getLocation() {

		return location;
	}
}
