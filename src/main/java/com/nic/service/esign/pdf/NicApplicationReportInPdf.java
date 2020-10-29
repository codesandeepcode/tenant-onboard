package com.nic.service.esign.pdf;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.nic.service.onboarding.publisher.NicApplicationFormModel;
import com.nic.service.onboarding.publisher.NicApplicationFormModel.PersonalDetails;

/**
 * This generate application report in PDF for NIC using ITextPdf
 * 
 * @author Georgie
 *
 */
public class NicApplicationReportInPdf {

	private static final FontFamily defaultFont = Font.FontFamily.HELVETICA;

	public static byte[] generateReport(String applicationRefNo, NicApplicationFormModel nic) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

		try {
			Document document = new Document(PageSize.A4, 36, 36, 20, 20);
			PdfWriter.getInstance(document, byteArrayOutputStream);

			document.open();

			addHeading(document);
			addTopContent(document, applicationRefNo, nic.getHog().getName(), currentTime);
			addHogSection(document, nic.getHog(), nic.getGroup());
			addTechnicalAdminSection(document, nic.getHod(), nic.getTechAdmin1(), nic.getTechAdmin2());
			addDomainAndProject(document, nic.getDomain(), nic.getProject());
			addDeclaration(document);

			document.newPage();
			addTermsAndConditions(document);
			addFooterEnd(document, applicationRefNo, currentTime);

			document.close();
		} catch (DocumentException e) {
			throw new RuntimeException("Error in generating pdf using ITextPdf", e);
		}

		return byteArrayOutputStream.toByteArray();
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
		emptySpace.setSpacingBefore(130f);
		document.add(emptySpace);

		Phrase phrase = new Phrase();
		phrase.add(new Phrase("Application Ref. No. : ", textFont));
		phrase.add(new Phrase(applicationReferenceNo, new Font(defaultFont, 11f, Font.BOLD)));
		phrase.add(new Chunk(new VerticalPositionMark()));
		phrase.add(new Phrase("Signup Date : " + currentTime, textFont));
		document.add(phrase);
	}

	private static void addTermsAndConditions(Document document) throws DocumentException {
		Font textFont = new Font(defaultFont, 11f);

		String heading = "Obligations and Responsibilities of National API Exchange Platform (NAPIX) users from concerned Government Ministry/Department/Organization";
		Paragraph headPara = new Paragraph(heading, new Font(defaultFont, 14f));
		headPara.setAlignment(Element.ALIGN_CENTER);
		headPara.setSpacingAfter(15f);
		document.add(headPara);

		List conditions = new List(List.ORDERED);

		ListItem item1 = new ListItem(
				"Concerned Government Ministry/Department/Organization shall be solely responsible for all the information, "
						+ "content and data stored on the Servers. Concerned Government Ministry/Department/Organization further "
						+ "acknowledges that it shall be solely responsible to undertake and maintain complete authenticity of the "
						+ "information/data sent and/or received. Concerned Government Ministry/Department/Organization shall also "
						+ "take necessary measures to ensure that information/data transmitted is authentic and consistent.",
				textFont);
		item1.setAlignment(Element.ALIGN_JUSTIFIED);
		conditions.add(item1);

		ListItem item2 = new ListItem(
				"Concerned Government Ministry/Department/Organization shall keep the account information such as userid, "
						+ "password provided for accessing National API Exchange Platform (NAPIX) in safe custody to avoid any misuse by "
						+ "unauthorised users.",
				textFont);
		item2.setAlignment(Element.ALIGN_JUSTIFIED);
		conditions.add(item2);

		ListItem item3 = new ListItem("I hereby authorize NAPIX team to deactivate the id in case of misuse/abuse.",
				textFont);
		item3.setAlignment(Element.ALIGN_JUSTIFIED);
		conditions.add(item3);

		ListItem item4 = new ListItem(
				"I confirm that I will not host any application/content which belongs to Top Secret and Secret Categories of "
						+ "classification.",
				textFont);
		item4.setAlignment(Element.ALIGN_JUSTIFIED);
		conditions.add(item4);

		ListItem item5 = new ListItem(
				"I confirm that the API(s) to be hosted/deployed on National API Exchange Platform (NAPIX) follow "
						+ "both policies - 'Policy on Open Application Programming Interfaces (APIs) for Government of India' and "
						+ "'National Data Sharing and Accessibility Policy (NDSAP) - 2012'.",
				textFont);
		item5.setAlignment(Element.ALIGN_JUSTIFIED);
		conditions.add(item5);

		ListItem item6 = new ListItem(
				"I confirm that I have already obtained consent for hosting/publishing API(s) in National API Exchange "
						+ "Platform (NAPIX).",
				textFont);
		item6.setAlignment(Element.ALIGN_JUSTIFIED);
		conditions.add(item6);

		ListItem item7 = new ListItem(
				"It is at the discrete level of API Owners(Publishers) to verify the subscriber’s/developer’s details "
						+ "submitted before authorising them to subscribe/consume their API.",
				textFont);
		item7.setAlignment(Element.ALIGN_JUSTIFIED);
		conditions.add(item7);

		ListItem item8 = new ListItem(
				"NAPIX team doesn’t holds any responsibility in whatsoever manner how the subscriber(s)/developer(s) will be using "
						+ "the data/service provided by API. The usage is the consent between the owner of API and the respective subscriber/developer.",
				textFont);
		item8.setAlignment(Element.ALIGN_JUSTIFIED);
		conditions.add(item8);
		document.add(conditions);

		String finalConsent = "This is to declare that I have read the terms and conditions given above and agree to abide by "
				+ "them. I shall be single point of contact for this account. I will be responsible for any misuse of the "
				+ "service/violation of the clauses mentioned above.";
		Paragraph finalAgreement = new Paragraph(finalConsent, textFont);
		finalAgreement.setSpacingBefore(25f);
		finalAgreement.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(finalAgreement);
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
		paragraph.setSpacingBefore(10f);
		document.add(paragraph);
	}

	private static void addDomainAndProject(Document document, String domain, String project) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setSpacingBefore(10f);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 8f, 17f, 8f, 17f });

		PdfPCell headerCell = new PdfPCell(
				new Phrase("Publisher Domain details", new Font(defaultFont, 12f, Font.BOLD)));
		headerCell.setColspan(4);
		headerCell.setPaddingTop(2f);
		headerCell.setPaddingBottom(5f);
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(headerCell);
		table.setHeaderRows(1);

		final Font cellFont = new Font(defaultFont, 10f);
		PdfPCell cell1 = new PdfPCell(new Phrase("Domain Name :", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(domain, cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Phrase("Project Name :", cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthLeft(0f);
		cell2.setBorderWidthRight(0f);
		table.addCell(cell2);
		cell2 = new PdfPCell(new Phrase(project, cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthLeft(0f);
		table.addCell(cell2);

		document.add(table);
	}

	private static void addTechnicalAdminSection(Document document, PersonalDetails hod, PersonalDetails techAdmin1,
			PersonalDetails techAdmin2) throws DocumentException {
		Font headingFont = new Font(defaultFont, 12f, Font.BOLD);
		Font subHeadingFont = new Font(defaultFont, 10f, Font.BOLD);

		PdfPTable table = new PdfPTable(4);
		table.setSpacingBefore(10f);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 12f, 25f, 6.5f, 6.5f });

		PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
		emptyCell.setColspan(4);

		PdfPCell headerCell = new PdfPCell(new Phrase("Technical Administrators", headingFont));
		headerCell.setColspan(4);
		headerCell.setPaddingTop(2f);
		headerCell.setPaddingBottom(5f);
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(headerCell);
		table.setHeaderRows(1);

		PdfPCell subHeadCell = new PdfPCell(new Phrase("Head of Division (HoD)", subHeadingFont));
		subHeadCell.setPaddingTop(2f);
		subHeadCell.setPaddingBottom(5f);
		subHeadCell.setColspan(4);
		table.addCell(subHeadCell);
		addTechnicalAdmin(table, hod);
		table.addCell(emptyCell);

		subHeadCell = new PdfPCell(new Phrase("Technical Admin - 1", subHeadingFont));
		subHeadCell.setPaddingTop(2f);
		subHeadCell.setPaddingBottom(5f);
		subHeadCell.setColspan(4);
		table.addCell(subHeadCell);
		addTechnicalAdmin(table, techAdmin1);
		table.addCell(emptyCell);

		boolean admin2IsNull = (techAdmin2 == null || techAdmin2.getCode().trim().length() == 0);
		String techAdmin2Title = admin2IsNull ? "Technical Admin - 2    (Not Opted)" : "Technical Admin - 2";

		subHeadCell = new PdfPCell(new Phrase(techAdmin2Title, subHeadingFont));
		subHeadCell.setPaddingTop(2f);
		subHeadCell.setPaddingBottom(5f);
		subHeadCell.setColspan(4);
		table.addCell(subHeadCell);

		if (admin2IsNull) {
			addTechnicalAdmin(table, null);
		} else {
			addTechnicalAdmin(table, techAdmin2);
		}

		document.add(table);
	}

	private static void addTechnicalAdmin(PdfPTable table, PersonalDetails techAdmin) {
		final Font cellFont = new Font(defaultFont, 10f);
		final Phrase blankCell = new Phrase("        ---", new Font(defaultFont, 10f, Font.NORMAL, BaseColor.GRAY));

		PdfPCell cell1 = new PdfPCell(new Phrase("Name (Emp. Code) :", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		Phrase pr1 = (techAdmin == null) ? blankCell
				: new Phrase(techAdmin.getName() + " (" + techAdmin.getCode() + ")", cellFont);
		cell1 = new PdfPCell(pr1);
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("Designation :", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		pr1 = (techAdmin == null) ? blankCell : new Phrase(techAdmin.getDesignation(), cellFont);
		cell1 = new PdfPCell(pr1);
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Phrase("Department/Organization :", cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthRight(0f);
		table.addCell(cell2);
		Phrase pr2 = (techAdmin == null) ? blankCell : new Phrase(techAdmin.getDepartment(), cellFont);
		cell2 = new PdfPCell(pr2);
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthLeft(0f);
		cell2.setColspan(3);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase("Contact Details :", cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthRight(0f);
		table.addCell(cell3);
		Phrase ph3 = blankCell;
		if (techAdmin != null) {
			ph3 = new Phrase(techAdmin.getEmailId() + ", " + techAdmin.getMobileNo() + " (Mobile No)", cellFont);
			ph3.add(techAdmin.getLandlineNo().trim().length() != 0
					? (", " + techAdmin.getLandlineNo() + " (Landline No)")
					: "");
		}
		cell3 = new PdfPCell(ph3);
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthLeft(0f);
		cell3.setColspan(3);
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Phrase("Address :", cellFont));
		cell4.setPaddingTop(2f);
		cell4.setPaddingBottom(5f);
		cell4.setUseVariableBorders(true);
		cell4.setBorderWidthRight(0f);
		table.addCell(cell4);
		Phrase pr4 = (techAdmin == null) ? blankCell : new Phrase(techAdmin.getAddress(), cellFont);
		cell4 = new PdfPCell(pr4);
		cell4.setPaddingTop(2f);
		cell4.setPaddingBottom(5f);
		cell4.setUseVariableBorders(true);
		cell4.setBorderWidthLeft(0f);
		cell4.setColspan(3);
		table.addCell(cell4);
	}

	private static void addHogSection(Document document, PersonalDetails hog, String group) throws DocumentException {
		final Font cellFont = new Font(defaultFont, 10f);

		PdfPTable table = new PdfPTable(4);
		table.setSpacingBefore(10f);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 12f, 25f, 6.5f, 6.5f });

		PdfPCell headerCell = new PdfPCell(new Phrase("Head of Group (HoG)", new Font(defaultFont, 12f, Font.BOLD)));
		headerCell.setPaddingTop(3f);
		headerCell.setPaddingBottom(6f);
		headerCell.setColspan(4);
		headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(headerCell);
		table.setHeaderRows(1);

		PdfPCell cell1 = new PdfPCell(new Phrase("Name (Emp. Code) :", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(hog.getName() + " (" + hog.getCode() + ")", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("Designation :", cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		cell1.setBorderWidthRight(0f);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(hog.getDesignation(), cellFont));
		cell1.setPaddingTop(2f);
		cell1.setPaddingBottom(5f);
		cell1.setUseVariableBorders(true);
		cell1.setBorderWidthLeft(0f);
		table.addCell(cell1);

		PdfPCell cell1a = new PdfPCell(new Phrase("Group :", cellFont));
		cell1a.setPaddingTop(2f);
		cell1a.setPaddingBottom(5f);
		cell1a.setUseVariableBorders(true);
		cell1a.setBorderWidthRight(0f);
		table.addCell(cell1a);
		cell1a = new PdfPCell(new Phrase(group, cellFont));
		cell1a.setPaddingTop(2f);
		cell1a.setPaddingBottom(5f);
		cell1a.setUseVariableBorders(true);
		cell1a.setBorderWidthLeft(0f);
		cell1a.setColspan(3);
		table.addCell(cell1a);

		PdfPCell cell2 = new PdfPCell(new Phrase("Department/Organization :", cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthRight(0f);
		table.addCell(cell2);
		cell2 = new PdfPCell(new Phrase(hog.getDepartment(), cellFont));
		cell2.setPaddingTop(2f);
		cell2.setPaddingBottom(5f);
		cell2.setUseVariableBorders(true);
		cell2.setBorderWidthLeft(0f);
		cell2.setColspan(3);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase("Contact Details :", cellFont));
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthRight(0f);
		table.addCell(cell3);
		Phrase ph3 = new Phrase(hog.getEmailId() + ", " + hog.getMobileNo() + " (Mobile No)", cellFont);
		ph3.add(hog.getLandlineNo().trim().length() != 0 ? (", " + hog.getLandlineNo() + " (Landline No)") : "");
		cell3 = new PdfPCell(ph3);
		cell3.setPaddingTop(2f);
		cell3.setPaddingBottom(5f);
		cell3.setUseVariableBorders(true);
		cell3.setBorderWidthLeft(0f);
		cell3.setColspan(3);
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Phrase("Address :", cellFont));
		cell4.setPaddingTop(2f);
		cell4.setPaddingBottom(5f);
		cell4.setUseVariableBorders(true);
		cell4.setBorderWidthRight(0f);
		table.addCell(cell4);
		cell4 = new PdfPCell(new Phrase(hog.getAddress(), cellFont));
		cell4.setPaddingTop(2f);
		cell4.setPaddingBottom(5f);
		cell4.setUseVariableBorders(true);
		cell4.setBorderWidthLeft(0f);
		cell4.setColspan(3);
		table.addCell(cell4);

		document.add(table);
	}

	private static void addTopContent(Document document, String applicationReferenceNo, String ownerName,
			String currentTime) throws DocumentException {
		Font textFont = new Font(defaultFont, 11f);
		Font textFontBold = new Font(defaultFont, 11f, Font.BOLD);

		Phrase phrase = new Phrase();
		phrase.add(new Phrase("Application Ref. No. : ", textFont));
		phrase.add(new Phrase(applicationReferenceNo, textFontBold));
		phrase.add(new Chunk(new VerticalPositionMark()));
		phrase.add(new Phrase("Signup Date : " + currentTime, textFont));
		document.add(phrase);

		String content = "I, " + ownerName
				+ " would like to on board as Publisher Admin on National API Exchange Platform (NAPIX). The "
				+ "details for on-boarding are as given below :- ";
		Paragraph para = new Paragraph(content, textFont);
		para.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(para);
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
		para.setSpacingBefore(18f);
		para.setSpacingAfter(28f);
		para.setAlignment(Element.ALIGN_CENTER);
		document.add(para);
	}

}
