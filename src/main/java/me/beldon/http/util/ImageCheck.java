package me.beldon.http.util;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

public class ImageCheck {
    private static MimetypesFileTypeMap mtftp;

    static {
        mtftp = new MimetypesFileTypeMap();
        mtftp.addMimeTypes("image png tif jpg jpeg bmp");
    }

    public static boolean isImage(File file) {
        String mimetype = mtftp.getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("image");
    }

}
