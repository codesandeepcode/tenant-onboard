package com.nic.service.esign.pdf;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.nic.service.esign.pdf.GovtApplicationModel.Office;
import com.nic.service.esign.pdf.GovtApplicationModel.ProjectHead;
import com.nic.service.esign.pdf.GovtApplicationModel.TechnicalHead;
import com.nic.service.utils.GovernmentType;

/**
 * This generate application report in PDF for Government using ITextPdf
 * 
 * @author Georgie
 */
public class GovtApplicationReportInPdf {

	private static final FontFamily defaultFont = Font.FontFamily.HELVETICA;

	public static byte[] generateReport(String applicationRefNo, GovtApplicationModel govt) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

		try {
			Document document = new Document(PageSize.A4, 30, 30, 12, 12);
			PdfWriter.getInstance(document, byteArrayOutputStream);

			document.open();

			// 1st Page
			addHeading(document);
			CommonElements.addTopContent(document, applicationRefNo, govt.getProjectHead().getName(), currentTime);
			addOfficeSection(document, govt.getGovernmentType(), govt.getOffice());
			addProjectHead(document, govt.getProjectHead());
			addTechnicalAdminSection(document, govt.getTechnicalHeadList());
			CommonElements.addDomainAndProject(document, govt.getDomainName(), govt.getProjectName());
			addDeclaration(document);

			// 2nd Page
			document.newPage();
			CommonElements.addTermsAndConditions(document);
			addFooterEnd(document, applicationRefNo, currentTime);

			document.close();
		} catch (DocumentException e) {
			throw new RuntimeException("Error in generating pdf using ITextPdf - Govt", e);
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

		PdfPCell cell3 = new PdfPCell(new Phrase("Group/Project Name :", cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthRight(0f);
		table.addCell(cell3);
		cell3 = new PdfPCell(new Phrase(head.getGroup(), cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthLeft(0f);
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Phrase("Contact Details :", cellFont));
		cell4.setPaddingTop(2f);
		cell4.setPaddingBottom(5f);
		cell4.setUseVariableBorders(true);
		cell4.setBorderWidthRight(0f);
		table.addCell(cell4);
		Phrase ph3 = new Phrase(head.getEmailId() + ", " + head.getMobileNo() + " (Mobile No)", cellFont);
		ph3.add(head.getLandlineNo().trim().length() != 0 ? (", " + head.getLandlineNo() + " (Landline No)") : "");
		cell4 = new PdfPCell(ph3);
		cell4.setPaddingTop(2f);
		cell4.setPaddingBottom(5f);
		cell4.setUseVariableBorders(true);
		cell4.setBorderWidthLeft(0f);
		table.addCell(cell4);

		document.add(table);
	}

	private static void addOfficeSection(Document document, GovernmentType type, Office office)
			throws DocumentException {
		final Font headingFont = new Font(defaultFont, 12f, Font.BOLD);
		final Font cellFont = new Font(defaultFont, 10f);

		PdfPTable table = new PdfPTable(4);
		table.setSpacingBefore(10f);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 10f, 15f, 4f, 21f });

		PdfPCell headerCell = new PdfPCell(new Phrase("Office", headingFont));
		headerCell.setPaddingTop(3f);
		headerCell.setPaddingBottom(6f);
		headerCell.setColspan(4);
		headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(headerCell);
		table.setHeaderRows(1);

		PdfPCell cell1 = new PdfPCell(new Phrase("Type of Government :", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(type.getName(), cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("State :", cellFont)); // PUT STATE AS A SEPARATE ROW
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(office.getState(), cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Phrase("Ministry/Department :", cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthRight(0f);
		table.addCell(cell2);
		cell2 = new PdfPCell(new Phrase(office.getDepartment(), cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthLeft(0f);
		cell2.setColspan(3);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase("Category :", cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthRight(0f);
		table.addCell(cell3);
		cell3 = new PdfPCell(new Phrase(office.getCategory(), cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthLeft(0f);
		cell3.setColspan(3);
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Phrase("Office Name :", cellFont));
		cell4.setPaddingTop(2f);
		cell4.setPaddingBottom(5f);
		cell4.setUseVariableBorders(true);
		cell4.setBorderWidthRight(0f);
		table.addCell(cell4);
		cell4 = new PdfPCell(new Phrase(office.getName(), cellFont));
		cell4.setPaddingTop(2f);
		cell4.setPaddingBottom(5f);
		cell4.setUseVariableBorders(true);
		cell4.setBorderWidthLeft(0f);
		cell4.setColspan(3);
		table.addCell(cell4);

		PdfPCell cell5 = new PdfPCell(new Phrase("Address :", cellFont));
		cell5.setPaddingTop(2f);
		cell5.setPaddingBottom(5f);
		cell5.setUseVariableBorders(true);
		cell5.setBorderWidthRight(0f);
		table.addCell(cell5);
		cell5 = new PdfPCell(new Phrase(formatAddress(office.getAddress()), cellFont));
		cell5.setPaddingTop(2f);
		cell5.setPaddingBottom(5f);
		cell5.setUseVariableBorders(true);
		cell5.setBorderWidthLeft(0f);
		cell5.setColspan(3);
		table.addCell(cell5);

		document.add(table);
	}

	private static String formatAddress(String address) {
		address = address.replaceAll("[\r\n]+", "\n");
		address = address.replaceAll("\n", "; ");
		return address;
	}

	private static void addHeading(Document document) throws DocumentException {
		Paragraph para = new Paragraph("Government of India", new Font(defaultFont, 12f));
		para.setAlignment(Element.ALIGN_CENTER);
		document.add(para);

		para = new Paragraph("National Informatics Centre", new Font(defaultFont, 12f));
		para.setAlignment(Element.ALIGN_CENTER);
		document.add(para);

		para = new Paragraph("Ministry of Electronics & Information Technology", new Font(defaultFont, 13f));
		para.setAlignment(Element.ALIGN_CENTER);
		document.add(para);

		para = new Paragraph("National API Exchange Platform (NAPIX)", new Font(defaultFont, 14f, Font.BOLD));
		para.setAlignment(Element.ALIGN_CENTER);
		document.add(para);

		para = new Paragraph("On-Boarding Request Form (Publisher)", new Font(defaultFont, 15f));
		para.setSpacingBefore(10f);
		para.setSpacingAfter(18f);
		para.setAlignment(Element.ALIGN_CENTER);
		document.add(para);
	}

	private static void addDeclaration(Document document) throws DocumentException {
		Font textFont = new Font(defaultFont, 11f);

		String content = "This is to declare that I have read the terms and conditions given on the next page and agree to "
				+ "abide by them. I shall be the single point of contact for this account. I will be responsible for any "
				+ "misuse of the service/violation of the clauses mentioned below. NIC reserves the right to deactivate the "
				+ "service in case of any violation.";
		Paragraph paragraph = new Paragraph(content, textFont);
		paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
		paragraph.setSpacingBefore(5f);
		document.add(paragraph);

		content = "I also further confirm that I have deployed the API(s) on port 443.";
		paragraph = new Paragraph(content, textFont);
		paragraph.setSpacingBefore(5f);
		document.add(paragraph);
	}

	private static void addFooterEnd(Document document, String applicationReferenceNo, String currentTime)
			throws DocumentException {
		Font textFont = new Font(defaultFont, 11f);

		String content = "Signature of the Publisher Admin";
		Paragraph paragraph = new Paragraph(content, textFont);
		paragraph.setAlignment(Element.ALIGN_RIGHT);
		paragraph.setSpacingBefore(100f);
		document.add(paragraph);

		Paragraph emptySpace = new Paragraph(" ");
		emptySpace.setSpacingBefore(230f);
		document.add(emptySpace);

		Phrase phrase = new Phrase();
		phrase.add(new Phrase("Application Ref. No. : ", textFont));
		phrase.add(new Phrase(applicationReferenceNo, new Font(defaultFont, 11f, Font.BOLD)));
		phrase.add(new Chunk(new VerticalPositionMark()));
		phrase.add(new Phrase("Signup Date : " + currentTime, textFont));
		document.add(phrase);
	}

}
