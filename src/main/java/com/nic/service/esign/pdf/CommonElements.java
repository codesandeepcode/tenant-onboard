package com.nic.service.esign.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

class CommonElements {

	private static final FontFamily defaultFont = Font.FontFamily.HELVETICA;

	public static void addHeading(Document document) throws DocumentException {
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

	public static void addTopContent(Document document, String applicationReferenceNo, String ownerName,
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

	public static void addDomainAndProject(Document document, String domain, String project) throws DocumentException {
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

	public static void addDeclaration(Document document) throws DocumentException {
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

	public static void addTermsAndConditions(Document document) throws DocumentException {
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
		document.add(conditions);

		String finalConsent = "This is to declare that I have read the terms and conditions given above and agree to abide by "
				+ "them. I shall be single point of contact for this account. I will be responsible for any misuse of the "
				+ "service/violation of the clauses mentioned above.";
		Paragraph finalAgreement = new Paragraph(finalConsent, textFont);
		finalAgreement.setSpacingBefore(25f);
		finalAgreement.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(finalAgreement);
	}

	public static void addFooterEnd(Document document, String applicationReferenceNo, String currentTime)
			throws DocumentException {
		Font textFont = new Font(defaultFont, 11f);

		String content = "Signature of the Publisher Admin";
		Paragraph paragraph = new Paragraph(content, textFont);
		paragraph.setAlignment(Element.ALIGN_RIGHT);
		paragraph.setSpacingBefore(100f);
		document.add(paragraph);

		Paragraph emptySpace = new Paragraph(" ");
		emptySpace.setSpacingBefore(210f);
		document.add(emptySpace);

		Phrase phrase = new Phrase();
		phrase.add(new Phrase("Application Ref. No. : ", textFont));
		phrase.add(new Phrase(applicationReferenceNo, new Font(defaultFont, 11f, Font.BOLD)));
		phrase.add(new Chunk(new VerticalPositionMark()));
		phrase.add(new Phrase("Signup Date : " + currentTime, textFont));
		document.add(phrase);
	}

}
