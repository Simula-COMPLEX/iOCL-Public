package no.simule.models;

import java.io.*;


/**
 * This class contains UML models as input stream and return copy of stream while EMF close stream after reading uml model.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */

public class ModelFile {
    private InputStream model;

    public ModelFile(InputStream model) {

        this.model = model;
    }

    public static ModelFile clone(ModelFile inputFile) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputFile.getModel().read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        inputFile.setModel(new BufferedInputStream(new ByteArrayInputStream(
                baos.toByteArray())));

        return new ModelFile((new BufferedInputStream(new ByteArrayInputStream(
                baos.toByteArray()))));
    }

    public InputStream getModel() {
        return model;
    }

    public void setModel(InputStream model) {
        this.model = model;
    }
}
