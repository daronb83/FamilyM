package fox.glass.com.familyserver.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import fox.glass.com.familyserver.services.*;
import fox.glass.com.shared.requests.*;

/**
 * Handles "/user/register" http requests
 */
public class RegisterHandler extends Handler{

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Entering RegisterHandler");
        boolean success = false;

        // Only allow POST requests
        if (isPostRequest(httpExchange)) {
            Request request = getRequestData(httpExchange, new RegisterRequest());
            success = processRequest(httpExchange, new RegisterService(), request, null, null);
        }

        if (!success) {
            sendBadRequestResponse(httpExchange);
        }

        logger.info("Leaving RegisterHandler\n");

    }
}
