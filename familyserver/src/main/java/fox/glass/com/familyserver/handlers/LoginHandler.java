package fox.glass.com.familyserver.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import fox.glass.com.familyserver.services.*;
import fox.glass.com.shared.requests.*;


/**
 * Handles all "/user/login" HTTP requests
 */
public class LoginHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Entering LoginHandler");
        boolean success = false;

        // require POST request
        if (isPostRequest(httpExchange)) {
            Request request = getRequestData(httpExchange, new LoginRequest());
            success = processRequest(httpExchange, new LoginService(), request, null, null);
        }

        if (!success) {
            sendBadRequestResponse(httpExchange);
        }

        logger.info("Leaving LoginHandler\n");
    }
}
