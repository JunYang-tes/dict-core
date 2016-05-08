package me.l.puppy.dict.model;

public class DictInfo {
	String author;
	String dictName;
	String description;
	String version;
	String email;

	public DictInfo(String author, String dictName, String description,
			String version, String email) {
		this.author = author;
		this.dictName = dictName;
		this.description = description;
		this.version = version;
		this.email = email;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
