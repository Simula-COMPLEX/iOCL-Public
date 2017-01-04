package no.simule.dataobjects;

import java.io.*;

public class ModelFile {
    private InputStream model;


    public ModelFile() {
        super();
    }

    public ModelFile(InputStream model) {
        super();
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
