package fox.glass.com.familyserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import fox.glass.com.familyserver.database.access.*;
import fox.glass.com.shared.database.*;
import fox.glass.com.shared.requests.*;
import fox.glass.com.shared.responses.*;


/**
 * Serves all event requests with no specified ID
 */
public class EventsService extends Service {

    /**
     * Returns ALL events for ALL family members of the current user. The current
     *  user is determined from the provided auth token.
     *
     * @param tokenValue the user's AuthToken
     * @return a JSON response containing an array of event objects
     */
    public EventsResponse getEvents(String tokenValue) {
        logger.info("Entering EventService");

        if (openConnection()) {
            assert tokenValue != null : "null tokenValue";
            User user = getUserByTokenValue(tokenValue);

            if (user != null) {
                List<Event> events = getEvents(user);

                if (events != null) {
                    closeConnection(true);
                    logger.info("EventService completed successfully");
                    return new EventsResponse(events);
                }
            }
            closeConnection(false);
        }

        logger.log(Level.WARNING, "EventService failed: " + error.getMessage());
        return null;
    }

    private List<Event> getEvents(User user) {
        EventDAO eDao = db.getEventDAO();
        List<Event> events = null;

        try {
            assert user != null;
            events = eDao.getEventsForUser(user.getUserName());
        }
        catch (DatabaseError e) {
            e.printStackTrace();
        }

        if (events != null) {
            return events;
        }

        error = new MessageResponse("Internal server error");
        return null;
    }

    private List<EventResponse> getResponses(User user, List<Event> events) {
        List<EventResponse> output = new ArrayList<EventResponse>();

        for (Event event : events) {
            EventResponse response = new EventResponse(user.getUserName(), event.getEventID(),
                    event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(),
                    event.getCity(), event.getEventType(), event.getYear());

            output.add(response);
        }

        return output;
    }

    /**
     * The generic form of getEventList()
     *
     * @param request (not required)
     * @param user (required) an AuthToken value representing a user
     * @param parameter (not required)
     * @return an EventsResponse json object
     */
    @Override
    public Response getResponse(Request request, String user, String parameter) {
        return getEvents(user);
    }
}
