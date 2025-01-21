package com.parkee.parkee.OCR.services;

import com.parkee.parkee.OCR.entity.OCRData;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OCRService {
    private final Tesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();
        String tessdataPath = System.getenv("TESSDATA_PREFIX");
        tesseract.setDatapath(tessdataPath);
        tesseract.setLanguage("ind");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
    }

    public OCRData extractTicketInfo(MultipartFile file) throws IOException, TesseractException {
        if (file.isEmpty()) {
            throw new IOException("Empty file provided");
        }

        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IOException("Invalid image file");
        }

        String result = tesseract.doOCR(image);
        OCRData ticket = new OCRData();

        Pattern ticketNumberPattern  = Pattern.compile("Nomor\\s+Tiket\\s*:\\s*([a-fA-F0-9-]+)");
        Matcher ticketNumberMatcher = ticketNumberPattern.matcher(result);
        if (ticketNumberMatcher.find()) {
            ticket.setNomorTiket(ticketNumberMatcher.group(1).trim());
        }

        Pattern datePattern = Pattern.compile("Tanggal\\s*:\\s*(\\d{2}\\s*[A-Za-z]+\\s*\\d{4})");
        Matcher dateMatcher = datePattern.matcher(result);
        if (dateMatcher.find()) {
            ticket.setTanggal(dateMatcher.group(1).trim());
        }

        Pattern timePattern = Pattern.compile("Jam Masuk\\s*:\\s*(\\d{2}:\\d{2}:\\d{2})");
        Matcher timeMatcher = timePattern.matcher(result);
        if (timeMatcher.find()) {
            ticket.setJamMasuk(timeMatcher.group(1).trim());
        }

        Pattern platePattern = Pattern.compile("Plat Nomor\\s*:\\s*([A-Z0-9]+)");
        Matcher plateMatcher = platePattern.matcher(result);
        if (plateMatcher.find()) {
            ticket.setPlatNomor(plateMatcher.group(1).trim());
        }

        Pattern typePattern = Pattern.compile("Jenis\\s*:\\s*(\\w+)");
        Matcher typeMatcher = typePattern.matcher(result);
        if (typeMatcher.find()) {
            ticket.setJenisKendaraan(typeMatcher.group(1).trim());
        }

        return ticket;
    }
}