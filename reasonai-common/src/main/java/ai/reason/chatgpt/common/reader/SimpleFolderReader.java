package ai.reason.chatgpt.common.reader;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
public class SimpleFolderReader {

    private final String from;

    private final List<String> allowedExts = List.of("txt", "md", "pdf");

    public void run(BiFunction<String, String, Void> handler) throws IOException {
        Files.walk(Paths.get(from))
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    String fileName = file.getFileName().toString();
                    String ext = getFileExtension(fileName);
                    if (allowedExts.contains(ext)) {
                        String content = null;
                        try {
                            if (ext.equalsIgnoreCase("pdf")) {
                                File f = new File(String.valueOf(file));
                                PDFParser parser = new PDFParser(new RandomAccessFile(f, "r"));
                                parser.parse();

                                COSDocument cosDoc = parser.getDocument();
                                PDFTextStripper pdfStripper = new PDFTextStripper();
                                PDDocument pdDoc = new PDDocument(cosDoc);
                                content = pdfStripper.getText(pdDoc);

                                if (cosDoc != null) {
                                    cosDoc.close();
                                    pdDoc.close();
                                }
                            } else {
                                content = Files.readString(file);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        handler.apply(fileName, content);
                    }
                });
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "";
        }
    }
}
