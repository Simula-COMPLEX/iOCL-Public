package no.simule.utils.api;


import no.simule.exception.EsOCLException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EsOCLEndPoint {
    private transient static final Logger logger = Logger.getLogger(EsOCLEndPoint.class);

    public boolean checkConstraint(String constraint, byte[] model) throws EsOCLException {

        String url = "http://zen.simula.no:8080/EsOCL/EsOCLService";


        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream("config.properties");
            prop.load(input);

            url = prop.getProperty("EsOCL");

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        logger.debug("ESOCL URL : " + url);

        try {

            WebServiceEndPointService service = new WebServiceEndPointServiceLocator(url);
            WebServiceEndPoint port = service.getWebServiceEndPointPort();
            boolean response = port.getResult(constraint, model);
            return response;
        } catch (Exception e) {
            throw new EsOCLException();
        }

    }

}
