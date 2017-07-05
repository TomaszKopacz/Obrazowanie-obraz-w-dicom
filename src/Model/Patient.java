package Model;

/**
 * Patient class storing real patient data.
 * 
 * @author Tomasz Kopacz
 *
 */
public class Patient {

	private String pesel;
	private String name;
	private String surname;
	private String sex;
	private String insurance;
	
	/**
	 * Constructor.
	 */
	public Patient(){
		
	}

	public String getPesel() {
		return pesel;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getSex() {
		return sex;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pesel == null) ? 0 : pesel.hashCode());
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
		Patient other = (Patient) obj;
		if (pesel == null) {
			if (other.pesel != null)
				return false;
		} else if (!pesel.equals(other.pesel))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Patient [pesel=" + pesel + ", name=" + name + ", surname=" + surname + "]";
	}
	
	
}
