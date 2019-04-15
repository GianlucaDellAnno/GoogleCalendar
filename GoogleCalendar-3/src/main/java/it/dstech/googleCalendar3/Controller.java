package it.dstech.googleCalendar3;

import java.io.IOException;
import java.security.GeneralSecurityException;
import it.dstech.googleCalendar3.RoomService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
public class Controller {

	@RequestMapping(value = "/newevent", method = RequestMethod.POST)
	public void createEvent(@RequestBody EventDTO eventDTO) throws GeneralSecurityException, IOException {
		RoomService.createEvent(eventDTO);
	}

	@RequestMapping(value = "/newcalendar", method = RequestMethod.POST)
	public void sendMail(@RequestBody CalendarDTO calendarDTO) throws GeneralSecurityException, IOException {
		RoomService.newCalendar(calendarDTO);
	}
	
}
