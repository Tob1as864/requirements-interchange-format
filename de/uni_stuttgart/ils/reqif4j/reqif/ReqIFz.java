package de.uni_stuttgart.ils.reqif4j.reqif;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReqIFz extends ReqIFFile {

    public ReqIFz(String filePath) throws IOException {
        File destDir = new File(filePath + "_unzipped"); // Extract to a separate directory
        if (!destDir.exists() && !destDir.mkdirs()) {
            throw new IOException("Failed to create extraction directory " + destDir);
        }

        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(filePath))) {
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                File newFile = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // Fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // Write file content
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }

                    // Process reqif files and associated images
                    if (zipEntry.getName().endsWith("reqif")) {
                        this.numberOfReqIFDocuments++;
                        String reqifBaseName = zipEntry.getName().split("\\.")[0];
                        Map<String, InputStream> picturesIS = new HashMap<>();

                        // Iterate again for images within the same loop
                        try (ZipInputStream imageZis = new ZipInputStream(new FileInputStream(filePath))) {
                            ZipEntry imageEntry = imageZis.getNextEntry();
                            while (imageEntry != null) {
                                String pictureFileName = new File(imageEntry.getName()).getName();
                                if (pictureFileName.endsWith("png") || pictureFileName.endsWith("jpeg") || pictureFileName.endsWith("jpg")) {
                                    picturesIS.put(pictureFileName, new FileInputStream(new File(destDir, imageEntry.getName())));
                                }
                                imageEntry = imageZis.getNextEntry();
                            }
                        }

                        this.picturesInReqIFDocument.put(reqifBaseName, picturesIS);
                        this.reqifDocuments.put(zipEntry.getName(), new ReqIFDocument(new FileInputStream(newFile), filePath, zipEntry.getName()));
                    }
                }
                zipEntry = zis.getNextEntry();
            }
        }
    }
}
