package com.nic.service.esign;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

@Service
class PdfEsignService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final KeyStore.PrivateKeyEntry jksPrivateKeyEntry;

	private final XMLSignatureFactory xmlSignfactory;

	private final SignedInfo signedInfo;

	private final KeyInfo keyInfo;

	private final XmlMapper xmlMapper;

	PdfEsignService(Environment environment, MappingJackson2XmlHttpMessageConverter xmlConverter)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, CertificateException, IOException,
			UnrecoverableEntryException, KeyStoreException {
		String jksFileAliasName = environment.getRequiredProperty("nic-esign.secret.jks.alias-name");
		String jksFilePassword = environment.getRequiredProperty("nic-esign.secret.jks.password");
		String jksFilePath = environment.getRequiredProperty("nic-esign.secret.jks.file-path");

		this.xmlMapper = (XmlMapper) xmlConverter.getObjectMapper();

		// Create a DOM XMLSignatureFactory that will be used to generate the enveloped
		// signature.
		this.xmlSignfactory = XMLSignatureFactory.getInstance("DOM");

		// Create a Reference to the enveloped document (in this case, you are signing
		// the whole document, so a URI of "" signifies that, and also specify the
		// SHA256 digest algorithm and the ENVELOPED Transform.
		Reference ref = xmlSignfactory
				.newReference("", xmlSignfactory.newDigestMethod(DigestMethod.SHA1, null),
						Collections.singletonList(
								xmlSignfactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
						null, null);

		// Create the SignedInfo.
		this.signedInfo = xmlSignfactory.newSignedInfo(
				xmlSignfactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
						(C14NMethodParameterSpec) null),
				xmlSignfactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

		// Load the KeyStore and get the signing key.
		Resource jksFile = new ClassPathResource(jksFilePath);
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(jksFile.getInputStream(), jksFilePassword.toCharArray());

		this.jksPrivateKeyEntry = (PrivateKeyEntry) ks.getEntry(jksFileAliasName,
				new KeyStore.PasswordProtection(jksFilePassword.toCharArray()));

		// Create the KeyInfo containing the X509Data.
		X509Certificate cert = (X509Certificate) jksPrivateKeyEntry.getCertificate();
		KeyInfoFactory kif = xmlSignfactory.getKeyInfoFactory();
		List<Object> x509Content = new ArrayList<>();
		x509Content.add(cert.getSubjectX500Principal().getName());
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		this.keyInfo = kif.newKeyInfo(Collections.singletonList(xd));
	}

	/**
	 * This method use the provided model to generate the signed request in XML
	 */
	public String signRequestInXml(ESignRequestModel requestModel) {
		try {
			String signedRequest = signXmlRequest(requestModel);
			log.debug("Signed Xml : {}", signedRequest);

			return signedRequest;
		} catch (SAXException | IOException | ParserConfigurationException | MarshalException | XMLSignatureException
				| TransformerException e) {
			throw new RuntimeException("Error in signing request in xml", e);
		}
	}

	// TODO: instead of writeasbytes.. you can use writeasstring and use inputsource
	// class to parse it
	/**
	 * From the data model provided, generate signed XML request
	 */
	private String signXmlRequest(ESignRequestModel requestModel) throws SAXException, IOException,
			ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException {

		byte[] reqByteArray = xmlMapper.writeValueAsBytes(requestModel);

		// Instantiate the document to be signed.
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(reqByteArray));

		DOMSignContext dsc = new DOMSignContext(jksPrivateKeyEntry.getPrivateKey(), doc.getDocumentElement());

		// Create the XMLSignature, but don't sign it yet.
		XMLSignature signature = xmlSignfactory.newXMLSignature(signedInfo, keyInfo);

		// Marshal, generate, and sign the enveloped signature.
		signature.sign(dsc);

		// Output the resulting document. generate back into xml ..
		StringWriter writer = new StringWriter();

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(writer));

		return writer.toString();
	}

	@SuppressWarnings("deprecation")
	public byte[] signPdfReport(byte[] document, String signingPerson, ESignResponseModel responseModel)
			throws IOException, DocumentException {
		int contentEstimation = 8192;

		try (ByteArrayOutputStream baous = new ByteArrayOutputStream()) {
			PdfReader pdfReader = new PdfReader(document);
			PdfStamper stamper = PdfStamper.createSignature(pdfReader, baous, '\0');

			ZonedDateTime instantTime = ZonedDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
			String describeText = "Signed by : " + signingPerson + "\nDate : "
					+ instantTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss"));

			// create signature appearance
			PdfSignatureAppearance signApperance = stamper.getSignatureAppearance();
			signApperance.setLayer2Text(describeText);
			signApperance.setLayer2Font(new Font(FontFamily.TIMES_ROMAN));
			signApperance.setReason("Publisher On-Boarding");
			signApperance.setRenderingMode(RenderingMode.DESCRIPTION);
			signApperance.setAcro6Layers(false);
			signApperance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
			signApperance.setImage((Image) null);

			int lastPageNo = pdfReader.getNumberOfPages();
			signApperance.setVisibleSignature(new Rectangle(400f, 220f, 600f, 330f), lastPageNo, (String) null);

			HashMap<PdfName, Integer> exc1 = new HashMap<PdfName, Integer>();
			exc1.put(PdfName.CONTENTS, Integer.valueOf(contentEstimation * 2 + 2));

			PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
			dic.setDate(new PdfDate(signApperance.getSignDate()));
			signApperance.setCryptoDictionary(dic);

			signApperance.preClose(exc1);

			byte[] PKCS7Response = Base64
					.decode(responseModel.getSignaturesObject().getDocSignObject().getSignedData().getBytes("UTF8"));
			byte[] paddedSig = new byte[contentEstimation];
			System.arraycopy(PKCS7Response, 0, paddedSig, 0, PKCS7Response.length);
			PdfDictionary dic2 = new PdfDictionary();
			dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));

			signApperance.close(dic2);

			return baous.toByteArray();
		}
	}

}
