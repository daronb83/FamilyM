package fox.glass.com.family.model;

import java.util.ArrayList;
import java.util.List;

import fox.glass.com.shared.database.Event;
import fox.glass.com.shared.database.Person;

/**
 * Singleton containing all people and events associated with the current user
 */
public class DataSet {

    private static DataSet _inst;
    private static List<Person> persons;
    private static List<Event> events;

    private DataSet() {
        persons = new ArrayList<>();
        events = new ArrayList<>();
    }

    public static DataSet instance() {

        if (_inst == null) {
            _inst = new DataSet();
        }

        return _inst;
    }

    public static Person getPersonById(String id) {

        if (persons != null) {
            for (Person person : persons) {

                if (person.getPersonID().equals(id)) {
                    return person;
                }
            }
        }

        return null;
    }

    public static List<Person> getPersons() {
        return persons;
    }

    public static void setPersons(List<Person> persons) {
        DataSet.persons = persons;
    }

    public static List<Event> getEvents() {
        return events;
    }

    public static void setEvents(List<Event> events) {
        DataSet.events = events;
    }
}
