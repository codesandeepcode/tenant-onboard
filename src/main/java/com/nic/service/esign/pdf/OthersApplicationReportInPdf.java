package com.nic.service.esign.pdf;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nic.service.esign.pdf.OthersApplicationModel.Office;
import com.nic.service.esign.pdf.OthersApplicationModel.ProjectHead;
import com.nic.service.esign.pdf.OthersApplicationModel.TechnicalHead;

public class OthersApplicationReportInPdf {

	private static final FontFamily defaultFont = Font.FontFamily.HELVETICA;

	public static byte[] generateReports(String applicationRefNo, OthersApplicationModel others) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

		try {
			Document document = new Document(PageSize.A4, 36, 36, 20, 20);
			PdfWriter.getInstance(document, byteArrayOutputStream);

			document.open();

			// 1st Page
			CommonElements.addHeading(document);
			CommonElements.addTopContent(document, applicationRefNo, others.getProjectHead().getName(), currentTime);
			addOfficeSection(document, others.getOffice());
			addProjectHead(document, others.getProjectHead());
			addTechnicalAdminSection(document, others.getTechnicalHead());
			CommonElements.addDomainAndProject(document, others.getDomainName(), others.getProjectName());
			CommonElements.addDeclaration(document);

			// 2st Page
			document.newPage();
			CommonElements.addTermsAndConditions(document);
			CommonElements.addFooterEnd(document, applicationRefNo, currentTime);

			document.close();
		} catch (DocumentException e) {
			throw new RuntimeException("Error in generating pdf using ITextPdf", e);
		}

		return byteArrayOutputStream.toByteArray();
	}

	private static void addTechnicalAdminSection(Document document, List<TechnicalHead> list) throws DocumentException {
		final Font headingFont = new Font(defaultFont, 12f, Font.BOLD);
		final Font subHeadingFont = new Font(defaultFont, 10f, Font.BOLD);

		PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
		emptyCell.setColspan(2);

		PdfPTable table = new PdfPTable(2);
		table.setSpacingBefore(10f);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 10f, 40f });

		PdfPCell headerCell = new PdfPCell(new Phrase("Technical Administrators", headingFont));
		headerCell.setPaddingTop(2f);
		headerCell.setPaddingBottom(5f);
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		headerCell.setColspan(2);
		table.addCell(headerCell);
		table.setHeaderRows(1);

		final AtomicInteger count = new AtomicInteger(0);
		list.forEach(head -> {
			if (count.get() != 0) {
				table.addCell(emptyCell);
			}

			PdfPCell subHeadCell = new PdfPCell(
					new Phrase("Technical Head - " + count.incrementAndGet(), subHeadingFont));
			subHeadCell.setPaddingTop(2f);
			subHeadCell.setPaddingBottom(5f);
			subHeadCell.setColspan(2);
			table.addCell(subHeadCell);
			addTechnicalHead(table, head);
		});

		// Add for missing Tech Head
		while (count.get() < 3) { // 3 - max no of Tech Head
			if (count.get() != 0) {
				table.addCell(emptyCell);
			}

			PdfPCell subHeadCell = new PdfPCell(
					new Phrase("Technical Head - " + count.incrementAndGet() + "    (Not Opted)", subHeadingFont));
			subHeadCell.setPaddingTop(2f);
			subHeadCell.setPaddingBottom(5f);
			subHeadCell.setColspan(2);
			table.addCell(subHeadCell);
			addTechnicalHead(table, null);
		}

		document.add(table);

	}

	private static void addTechnicalHead(PdfPTable table, TechnicalHead techHead) {
		final Font cellFont = new Font(defaultFont, 10f);
		final Phrase blankCell = new Phrase("              -----",
				new Font(defaultFont, 10f, Font.NORMAL, BaseColor.GRAY));

		PdfPCell cell1 = new PdfPCell(new Phrase("Name (Emp. Code) : ", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		Phrase pr1 = (techHead == null) ? blankCell
				: new Phrase(techHead.getName() + " (" + techHead.getCode() + ")", cellFont);
		cell1 = new PdfPCell(pr1);
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Phrase("Designation :", cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthRight(0f);
		table.addCell(cell2);
		Phrase pr2 = (techHead == null) ? blankCell : new Phrase(techHead.getDesignation(), cellFont);
		cell2 = new PdfPCell(pr2);
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthLeft(0f);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase("Contact Details :", cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthRight(0f);
		table.addCell(cell3);
		Phrase ph3 = blankCell;
		if (techHead != null) {
			ph3 = new Phrase(techHead.getEmailId() + ", " + techHead.getMobileNo() + " (Mobile No)", cellFont);
			ph3.add(techHead.getLandlineNo().trim().length() != 0 ? (", " + techHead.getLandlineNo() + " (Landline No)")
					: "");
		}
		cell3 = new PdfPCell(ph3);
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthLeft(0f);
		table.addCell(cell3);
	}

	private static void addProjectHead(Document document, ProjectHead head) throws DocumentException {
		final Font headingFont = new Font(defaultFont, 12f, Font.BOLD);
		final Font cellFont = new Font(defaultFont, 10f);

		PdfPTable table = new PdfPTable(2);
		table.setSpacingBefore(10f);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 10f, 40f });

		PdfPCell headerCell = new PdfPCell(new Phrase("Project Head", headingFont));
		headerCell.setPaddingTop(3f);
		headerCell.setPaddingBottom(6f);
		headerCell.setColspan(2);
		headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(headerCell);
		table.setHeaderRows(1);

		PdfPCell cell1 = new PdfPCell(new Phrase("Name (Emp. Code) :", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(head.getName() + " (" + head.getCode() + ")", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Phrase("Designation :", cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthRight(0f);
		table.addCell(cell2);
		cell2 = new PdfPCell(new Phrase(head.getDesignation(), cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthLeft(0f);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase("Contact Details :", cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthRight(0f);
		table.addCell(cell3);
		Phrase ph3 = new Phrase(head.getEmailId() + ", " + head.getMobileNo() + " (Mobile No)", cellFont);
		ph3.add(head.getLandlineNo().trim().length() != 0 ? (", " + head.getLandlineNo() + " (Landline No)") : "");
		cell3 = new PdfPCell(ph3);
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthLeft(0f);
		table.addCell(cell3);

		document.add(table);
	}

	private static void addOfficeSection(Document document, Office office) throws DocumentException {
		final Font headingFont = new Font(defaultFont, 12f, Font.BOLD);
		final Font cellFont = new Font(defaultFont, 10f);

		PdfPTable table = new PdfPTable(2);
		table.setSpacingBefore(10f);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 10f, 40f });

		PdfPCell headerCell = new PdfPCell(new Phrase("Office", headingFont));
		headerCell.setPaddingTop(3f);
		headerCell.setPaddingBottom(6f);
		headerCell.setColspan(2);
		headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(headerCell);
		table.setHeaderRows(1);

		PdfPCell cell1 = new PdfPCell(new Phrase("Company Name :", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(office.getName(), cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Phrase("State :", cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthRight(0f);
		table.addCell(cell2);
		cell2 = new PdfPCell(new Phrase(office.getState(), cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthLeft(0f);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase("Address (State) :", cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthRight(0f);
		table.addCell(cell3);
		cell3 = new PdfPCell(new Phrase(formatAddress(office.getAddress()), cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthLeft(0f);
		table.addCell(cell3);

		document.add(table);
	}

	private static String formatAddress(String address) {
		address = address.replaceAll("[\r\n]+", "\n");
		address = address.replaceAll("\n", " ");
		return address;
	}

}
