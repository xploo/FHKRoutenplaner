package de.damianbuecker.fhkroutenplaner.model;

// TODO: Auto-generated Javadoc
/**
 * The Class Vertex.
 */
public class Vertex extends Model {

	/** The id. */
	final private String id;

	/** The name. */
	final private String name;

	/** The floor. */
	final private Integer floor;

	/**
	 * Instantiates a new vertex.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param floor
	 *            the floor
	 */
	public Vertex(String id, String name, Integer floor) {
		this.id = id;
		this.name = name;
		this.floor = floor;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the floor.
	 * 
	 * @return the floor
	 */
	public Integer getFloor() {
		return floor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}