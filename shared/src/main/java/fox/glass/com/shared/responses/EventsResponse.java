package fox.glass.com.shared.responses;

import java.util.List;

import fox.glass.com.shared.database.Event;

/**
 * Models a JSON response for the EventsService
 */
public class EventsResponse implements Response {

    private List<Event> data;

    /**
     * Models a JSON response for the EventsService
     *
     * @param data a list of event objects
     */
    public EventsResponse(List<Event> data) {
        this.data = data;
    }
    public EventsResponse() {
        data = null;
    }

    public List<Event> getData() {
        return data;
    }
}
