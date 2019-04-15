package it.dstech.googleCalendar3;

import java.util.Date;

public class EventDTO {

	private String idCalendar;
	
	private String email;
	
	private String summary;
	
	private String location;
	
	private String description;
	
	private Date startingAt;
	
	private Date EndingAt;

	public String getIdCalendar() {
		return idCalendar;
	}

	public void setIdCalendar(String idCalendar) {
		this.idCalendar = idCalendar;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartingAt() {
		return startingAt;
	}

	public void setStartingAt(Date startingAt) {
		this.startingAt = startingAt;
	}

	public Date getEndingAt() {
		return EndingAt;
	}

	public void setEndingAt(Date endingAt) {
		EndingAt = endingAt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
}
