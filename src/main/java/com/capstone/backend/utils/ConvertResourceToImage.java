package com.capstone.backend.utils;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.PageSet;
import com.aspose.words.SaveFormat;
import com.capstone.backend.exception.ApiException;
import com.spire.presentation.ISlide;
import com.spire.presentation.Presentation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Data
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConvertResourceToImage {
    MessageException messageException;

    /**
     * Convert first page pdf to image
     *
     * @param pathName link of resource
     * @param folder   name of folder
     * @return link of thumbnail image
     */
    public String ConvertFirstPagePdfToImage(String pathName, Path folder) {
        String uuid = UUID.randomUUID().toString();
        File newFile = new File(pathName);
        PDDocument pdfDocument = null;
        try {
            pdfDocument = PDDocument.load(newFile);
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            BufferedImage img = pdfRenderer.renderImage(0);
            String targetSrc = String.valueOf(folder.resolve(uuid + ".png"));
            ImageIO.write(img, "JPEG", new File(targetSrc));
            pdfDocument.close();
            return uuid + ".png";
        } catch (IOException e) {
            throw ApiException.internalServerException(messageException.MSG_FILE_CONVERT_ERROR);
        }
    }

    /**
     * Convert first page slide to image
     *
     * @param pathName link of resource
     * @param folder   name of folder
     * @return link of thumbnail image
     */
    public String ConvertFirstPageSlideToImage(String pathName, Path folder) {
        String uuid = UUID.randomUUID().toString();
        try {
            //Create a Presentation instance
            Presentation presentation = new Presentation();
            //Load a PowerPoint document
            presentation.loadFromFile(pathName);
            //Iterate through all slides in the PowerPoint document
            ISlide slide = presentation.getSlides().get(0);
            //Save each slide as PNG image
            BufferedImage image = slide.saveAsImage();
            String targetSrc = String.valueOf(folder.resolve(uuid + ".png"));
            ImageIO.write(image, "PNG", new File(targetSrc));
            return uuid + ".png";
        } catch (Exception e) {
            throw ApiException.internalServerException(messageException.MSG_FILE_CONVERT_ERROR);
        }
    }

    /**
     * Convert first page doc to image
     *
     * @param pathName link of resource
     * @param folder   name of folder
     * @return link of thumbnail image
     */
    public String ConvertFirstPageDocToImage(String pathName, Path folder) {
        String uuid = UUID.randomUUID().toString();
        // load document
        Document doc = null;
        try {
            doc = new Document(pathName);
            // set output image format using SaveFormat
            var options = new ImageSaveOptions(SaveFormat.PNG);
            // Save page as PNG
            options.setPageSet(new PageSet(0));
            String targetSrc = String.valueOf(folder.resolve(uuid + ".png"));
            doc.save(targetSrc, options);
            return uuid + ".png";
        } catch (Exception e) {
            throw ApiException.internalServerException(messageException.MSG_FILE_CONVERT_ERROR);
        }
    }
}
