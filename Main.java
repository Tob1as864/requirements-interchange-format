import de.uni_stuttgart.ils.reqif4j.reqif.ReqIF;
import de.uni_stuttgart.ils.reqif4j.reqif.ReqIFCoreContent;
import de.uni_stuttgart.ils.reqif4j.reqif.ReqIFDocument;
import de.uni_stuttgart.ils.reqif4j.reqif.ReqIFz;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        ReqIFz reqIFz;
        try {
            reqIFz = new ReqIFz("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String name = reqIFz.getName();
        ReqIFDocument reqIFDocument = reqIFz.getReqIFDocuments().get("Requirements.reqif");
        ReqIFCoreContent reqIFCoreContent = reqIFDocument.getCoreContent();
        //reqIFCoreContent.getSpecObjects()
    }
}
