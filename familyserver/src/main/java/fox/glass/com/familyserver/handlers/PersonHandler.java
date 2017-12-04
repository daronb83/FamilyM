package fox.glass.com.familyserver.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import fox.glass.com.familyserver.services.*;

/**
 * Handles all "/person" http requests
 */
public class PersonHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Entering PersonHandler");
        boolean success = false;

        // Only allow GET requests
        if (isGetRequest(httpExchange)) {
            String token = getAuthTokenValue(httpExchange);

            if (token != null) {
                String[] uriParts = getUriParts(httpExchange);

                // PersonService requires a parameter, while PeopleService does not
                if (uriParts.length > 2) { // Person
                    String pid = uriParts[2];
                    success = processRequest(httpExchange, new PersonService(), null, token, pid);
                }
                else { // People
                    success = processRequest(httpExchange, new PeopleService(), null, token, null);
                }
            }
        } // isGetRequest()

        if (!success) {
            sendBadRequestResponse(httpExchange);
        }

        logger.info("Leaving EventHandler\n");

    } // handle
}
