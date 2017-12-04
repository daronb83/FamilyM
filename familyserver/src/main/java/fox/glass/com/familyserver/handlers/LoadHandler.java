package fox.glass.com.familyserver.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import fox.glass.com.familyserver.services.*;
import fox.glass.com.shared.requests.*;


/**
 * Handles all "/load" http requests
 */
public class LoadHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Entering LoadHandler");
        boolean success = false;

        // Only allow POST requests
        if (isPostRequest(httpExchange)) {
            Request request = getRequestData(httpExchange, new LoadRequest());
            success = processRequest(httpExchange, new LoadService(), request, null, null);

        }

        if (!success) {
            sendBadRequestResponse(httpExchange);
        }

        logger.info("Leaving LoadHandler\n");
    }
}
