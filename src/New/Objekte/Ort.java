package New.Objekte;

public class Ort {

	// Attribute
	private int ortID;
	private String plz;
	private String name;

	// Konstruktor
	
	public Ort() {
		
	}
	
	public Ort(String plz, String name) {
		this.plz = plz;
		this.name = name;
	}

	// Konstruktor
	public Ort(int ortID, String plz, String name) {
		this.ortID = ortID;
		this.plz = plz;
		this.name = name;
	}

	// Setter & Getter
	public int getOrtID() {
		return ortID;
	}

	public void setOrtID(int ortID) {
		this.ortID = ortID;
	}

	public String getPLZ() {
		return plz;
	}

	public void setPLZ(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return name;
	}

	public void setOrt(String name) {
		this.name = name;
	}

	// Override
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Ort))
			return false;
		Ort ort = (Ort) o;
		return ortID == ort.ortID && java.util.Objects.equals(plz, ort.plz) && java.util.Objects.equals(name, ort.name);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(ortID, plz, name);
	}

	@Override
	public String toString() {
		return "Ort:\nOrtID: " + ortID + "\nPLZ: " + plz + "\nName: " + name + "\n";
	}
}
