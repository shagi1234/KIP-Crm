package tm.payhas.crm.domain.helpers;

import static tm.payhas.crm.domain.statics.StaticConstants.EXCEL;
import static tm.payhas.crm.domain.statics.StaticConstants.PDF;
import static tm.payhas.crm.domain.statics.StaticConstants.POWER_POINT;
import static tm.payhas.crm.domain.statics.StaticConstants.TEXT;
import static tm.payhas.crm.domain.statics.StaticConstants.WORD;

public class FileFormatUtil {

    public interface FileFormatListener {
        void onFileFormatIdentified(String fileFormat);
    }

    public static void getFileFormatFromUrl(String url, FileFormatListener listener) {
        String[] parts = url.split("\\.");
        if (parts.length > 0) {
            String extension = parts[parts.length - 1];
            String fileFormat = mapExtensionToFileFormat(extension);
            listener.onFileFormatIdentified(fileFormat);
        } else {
            listener.onFileFormatIdentified("Unknown");
        }
    }

    private static String mapExtensionToFileFormat(String extension) {
        // Implement your mapping logic here
        switch (extension) {
            case "txt":
                return TEXT;
            case "doc":
            case "docx":
                return WORD;
            case "ppt":
            case "pptx":
                return POWER_POINT;
            case "xls":
            case "xlsx":
                return EXCEL;
            case "pdf":
                return PDF;
            default:
                return "Unknown";
        }
    }
}
