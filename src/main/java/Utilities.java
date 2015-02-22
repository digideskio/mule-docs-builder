import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class Utilities {
    static Logger logger = Logger.getLogger(Utilities.class);

    public static String getFileContentsFromFile(File file) {
        String contents = "";
        try {
            FileReader reader = new FileReader(file);
            contents = IOUtils.toString(reader);
        } catch (FileNotFoundException ffe) {
            String error = "The file \"" + file.getName() + "\" was not found.";
            logger.fatal(error, ffe);
            throw new DocBuildException(error);
        } catch (IOException ioe) {
            String error = "Cannot get file contents for \"" + file.getName() + "\".";
            logger.fatal(error, ioe);
            throw new DocBuildException(error);
        }
        return contents;
    }

    public static List<String> getFileContentsFromFiles(List<File> files) {
        List<String> fileContents = new ArrayList<String>();
        for (File file : files) {
            try {
                FileReader reader = new FileReader(file);
                fileContents.add(IOUtils.toString(reader));
            } catch (FileNotFoundException ffe) {
                String error = "The file \"" + file.getName() + "\" was not found.";
                logger.fatal(error, ffe);
                throw new DocBuildException(error);
            } catch (IOException ioe) {
                String error = "Cannot get file contents for \"" + file.getName() + "\".";
                logger.fatal(error, ioe);
                throw new DocBuildException(error);
            }
        }
        return fileContents;
    }

    public static boolean fileEndsWithValidAsciidocExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        if(extension.equalsIgnoreCase("ad") || extension.equalsIgnoreCase("asciidoc") || extension.equalsIgnoreCase("adoc"))
            return true;
        return false;
    }

    public static void validateAsciiDocFile(File asciiDocFile) {
        if(!asciiDocFile.exists()) {
            String error = "AsciiDoc file does not exist: \"" + asciiDocFile.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
        if(!fileEndsWithValidAsciidocExtension(asciiDocFile.getName())) {
            String error = "Presumed AsciiDoc file does not have valid extension: \"" + asciiDocFile.getName() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static String getConcatPath(String[] filesOrDirectoriesToAppend) {
        String temp = filesOrDirectoriesToAppend[0];
        for(int i = 1; i < filesOrDirectoriesToAppend.length; i++) {
            if(!temp.endsWith("/")) {
                temp = temp.concat("/").concat(filesOrDirectoriesToAppend[i]);
            } else {
                temp = temp.concat(filesOrDirectoriesToAppend[i]);
            }
        }
        return temp;
    }

    public static boolean isStringNullOrEmptyOrWhitespace(String string) {
        if((string == null) || (string.isEmpty() || (StringUtils.isWhitespace(string)))) {
            return true;
        }
        return false;
    }

    public static String getOnlyContentDivFromHtml(String html) {
        Document doc = Jsoup.parse(html, "UTF-8");
        return doc.getElementById("content").html();
    }

    public static void validateCtorStringInputParam(String[] params, String className) {
        for(String param : params) {
            if (Utilities.isStringNullOrEmptyOrWhitespace(param)) {
                String error = "Constructor input parameter for " + className + " cannot be null, empty, or whitespace.";
                logger.fatal(error);
                throw new DocBuildException(error);
            }
        }
    }

    public static void validateCtorObjectsAreNotNull(Object[] params, String className) {
        for(Object obj : params) {
            if (obj == null) {
                String error = "Constructor input parameter for " + className + " cannot be null.";
                logger.fatal(error);
                throw new DocBuildException(error);
            }
        }
    }
}
