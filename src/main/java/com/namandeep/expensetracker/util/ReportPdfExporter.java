package com.namandeep.expensetracker.util;

import com.namandeep.expensetracker.dto.ReportResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/** Builds a PDF representation of a financial report. */
public final class ReportPdfExporter {

    private static final float MARGIN = 50f;
    private static final float TITLE_FONT_SIZE = 22f;
    private static final float LABEL_FONT_SIZE = 12f;
    private static final float VALUE_FONT_SIZE = 12f;
    private static final float LINE_HEIGHT = 24f;
    private static final DateTimeFormatter GENERATED_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");

    private ReportPdfExporter() {
    }

    public static byte[] toPdf(ReportResponse report) {
        try (PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font labelFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font valueFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float y = page.getMediaBox().getHeight() - MARGIN;

                y = writeTitle(contentStream, titleFont, y);
                y -= LINE_HEIGHT;

                y = writeRow(contentStream, labelFont, valueFont, y, "Total Income", format(report.getTotalIncome()));
                y = writeRow(contentStream, labelFont, valueFont, y, "Total Expense", format(report.getTotalExpense()));
                y = writeRow(contentStream, labelFont, valueFont, y, "Net Savings", format(report.getNetSavings()));
                y = writeRow(
                        contentStream,
                        labelFont,
                        valueFont,
                        y,
                        "Total Transactions",
                        formatTransactions(report.getTotalTransactions()));

                y -= LINE_HEIGHT;
                writeRow(
                        contentStream,
                        labelFont,
                        valueFont,
                        y,
                        "Generated Date",
                        LocalDateTime.now().format(GENERATED_DATE_FORMAT));
            }

            document.save(output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to generate financial report PDF", ex);
        }
    }

    private static float writeTitle(PDPageContentStream contentStream, PDType1Font font, float y)
            throws IOException {
        contentStream.setFont(font, TITLE_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, y);
        contentStream.showText("Financial Report");
        contentStream.endText();
        return y - TITLE_FONT_SIZE - 8f;
    }

    private static float writeRow(
            PDPageContentStream contentStream,
            PDType1Font labelFont,
            PDType1Font valueFont,
            float y,
            String label,
            String value)
            throws IOException {
        contentStream.setFont(labelFont, LABEL_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, y);
        contentStream.showText(label + ":");
        contentStream.endText();

        contentStream.setFont(valueFont, VALUE_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN + 180f, y);
        contentStream.showText(value);
        contentStream.endText();

        return y - LINE_HEIGHT;
    }

    private static String format(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }

    private static String formatTransactions(Integer value) {
        return value == null ? "" : value.toString();
    }
}
