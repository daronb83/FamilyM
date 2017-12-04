package fox.glass.com.shared.json;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Encodes objects into json strings
 */
public class JsonEncoder {

    private static Logger logger;

    static {
        logger = Logger.getLogger("famServer");
    }

    /**
     * Encodes objects into json strings
     *
     * @param object the object to encode
     * @return a json string representing the object
     */
    public String encode(Object object) {
        logger.info("Encoding json string");

        Gson gson = new Gson();
        String output = gson.toJson(object);

        logger.info(output);
        return output;
    }

    /**
     * Encodes an object to json, then pushes the json to an output stream
     *
     * @param object the object to encode
     */
    public void encodeToStream(Object object, OutputStream stream) {
        logger.info("Encoding json stream");

        Gson gson = new Gson();

        try {
            Writer writer = new OutputStreamWriter(stream);
            gson.toJson(object, writer);
            writer.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        logger.info("Finished encoding stream");
    }
}
