

//MODELS ARE CLASSES THAT ARE BEING DEISGNED TO BE THE PLACEHOLDERS OF THE ACTUAL DATA 

package com.revature.models;

import java.util.Objects;

public class Role {
	
	private int id;
	private String role;
	
	public Role(int id, String role) {
		super();
		this.id = id;
		this.role = role;
				
	}
	public Role() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return id;
	}
	public String getRole() {
		return role;
	}
	public int getRoleId(int id) {
		return id;
	}
	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Role)) {
			return false;
		}
		Role other = (Role) obj;
		return id == other.id && Objects.equals(role, other.role);
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + "]";
	}
	
	

	
	
}
