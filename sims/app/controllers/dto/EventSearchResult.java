package controllers.dto;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import models.App;
import models.User;

public class EventSearchResult {

	public List<Event> events = new LinkedList<EventSearchResult.Event>();
	public List<String> errors = new LinkedList<String>();

	public static class Event {
		
		public String date;
		public String type;
		public String responsible;
		public List<String> relatedApps = new LinkedList<String>();
		public List<String> relatedUsers = new LinkedList<String>();
		public String description;
		
	}
	
	public void addEvent(models.Event event) {
		Event dto = new controllers.dto.EventSearchResult.Event();
		dto.date = new SimpleDateFormat("dd-MM-yyyy").format(event.date);
		dto.description = event.description;
		for (App app : event.relatedApps) {
			dto.relatedApps.add(app.name);
		}
		for (User usr : event.relatedUsers) {
			dto.relatedUsers.add(usr.username);
		}
		dto.responsible = event.responsible.username;
		dto.type = event.type.getDescription();
		events.add(dto);
	}
}