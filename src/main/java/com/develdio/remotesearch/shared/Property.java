package com.develdio.remotesearch.shared;

import java.util.ArrayList;
import java.util.List;

public class Property
{
	private String hostname;
	private List<String> directories = new ArrayList<>();
	private String user;
	private String password;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public List<String> getDirectories() {
		return directories;
	}

	public void setDirectory(String directory) {
		this.directories.add( directory );
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
