package fox.glass.com.shared.responses;

import java.util.List;

import fox.glass.com.shared.database.Person;

/**
 * Modles a JSON response for the PeopleService
 */
public class PersonsResponse implements Response {

    private List<Person> data;

    /**
     * Models a JSON response for the PeopleService
     *
     * @param data a list of Person objects
     */
    public PersonsResponse(List<Person> data) {
        this.data = data;
    }
    public PersonsResponse() {
        data = null;
    }

    public List<Person> getData() {
        return data;
    }
}
