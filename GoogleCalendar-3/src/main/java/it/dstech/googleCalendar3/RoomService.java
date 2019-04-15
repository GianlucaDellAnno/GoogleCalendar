package it.dstech.googleCalendar3;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.security.GeneralSecurityException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.Calendar;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

public class RoomService {

	private static final String APPLICATION_NAME = "DSTech Events";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = RoomService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	public static void createEvent(EventDTO eventDTO) throws GeneralSecurityException, IOException {

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();

		Event event = new Event().setSummary(eventDTO.getSummary()).setLocation(eventDTO.getLocation())
				.setDescription(eventDTO.getDescription());

		DateTime startDateTime = new DateTime(eventDTO.getStartingAt());
		EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Rome");
		event.setStart(start);

		DateTime endDateTime = new DateTime(eventDTO.getEndingAt());
		EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Rome");
		event.setEnd(end);

//			String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
//			event.setRecurrence(Arrays.asList(recurrence));

		ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();
//		List<EventAttendee> participant = eventDTO.getInvitated();
//		for (EventAttendee eventAttendee : participant) {
		EventAttendee newAttendee = new EventAttendee().setEmail(eventDTO.getEmail());
		attendees.add(newAttendee);		
//		}

		event.setAttendees(attendees);
		
		EventReminder[] reminderOverrides = new EventReminder[] {
				new EventReminder().setMethod("email").setMinutes(25 * 60),
				new EventReminder().setMethod("popup").setMinutes(10), };
				
		Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
				.setOverrides(Arrays.asList(reminderOverrides));
		event.setReminders(reminders);

		String calendarId = "primary";
		event = service.events().insert(calendarId, event).setSendNotifications(true).execute() ;
		System.out.printf("Event created: %s\n", event.getHtmlLink());

	}
	
	public static void newCalendar(CalendarDTO calendarDTO) throws GeneralSecurityException, IOException {
		
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();
		com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
		calendar.setSummary(calendarDTO.getSummary());
		calendar.setDescription(calendarDTO.getDescription());
		calendar.setTimeZone(calendarDTO.getTimeZone());
		calendar.setLocation(calendarDTO.getLocation());
		service.calendars().insert(calendar).execute();
	}

}
