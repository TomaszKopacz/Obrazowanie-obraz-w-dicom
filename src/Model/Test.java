package Model;

/**
 * @author Tomasz Kopacz
 *
 */
public class Test {
	private int id;
	private String name;
	private Series series;
	private String imagePath;
	
	public Test(){
		
	}

	public String getName() {
		return name;
	}

	public Series getSeries() {
		return series;
	}
	public String getImagePath() {
		return imagePath;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public void setImage(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((series == null) ? 0 : series.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Test other = (Test) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (series == null) {
			if (other.series != null)
				return false;
		} else if (!series.equals(other.series))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Test [name=" + name + ", series=" + series + "]";
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
