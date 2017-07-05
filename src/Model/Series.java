package Model;

/**
 * @author Tomasz Kopacz
 *
 */
public class Series {

	private int id;
	private String name;
	private String patientPesel;
	
	public Series(){
		
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPatientPesel() {
		return patientPesel;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setPatientPesel(String patientPesel) {
		this.patientPesel = patientPesel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((patientPesel == null) ? 0 : patientPesel.hashCode());
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
		Series other = (Series) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (patientPesel == null) {
			if (other.patientPesel != null)
				return false;
		} else if (!patientPesel.equals(other.patientPesel))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Series [name=" + name + ", patientPesel=" + patientPesel + "]";
	}		
}
