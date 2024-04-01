package fr.maxlego08.essentials.api.storage;

public enum Folder {

	USERS,

	;
	

	public String toFolder(){
		return name().toLowerCase();
	}
	
}
