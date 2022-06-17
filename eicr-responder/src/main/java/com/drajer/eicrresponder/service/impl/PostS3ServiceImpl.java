package com.drajer.eicrresponder.service.impl;

import java.util.Arrays;
import javax.transaction.Transactional;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drajer.eicrresponder.model.MetaData;
import com.drajer.eicrresponder.model.ResponderRequest;
import com.drajer.eicrresponder.parser.EicrResponderParserContant;
import com.drajer.eicrresponder.service.AmazonClientService;
import com.drajer.eicrresponder.service.Interface.PostS3Service;
import com.drajer.eicrresponder.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

/**
 * @author Girish Rao
 *
 */
@Service
@Transactional
public class PostS3ServiceImpl implements PostS3Service {

	private static final Logger logger = LoggerFactory.getLogger(PostS3ServiceImpl.class);

	protected FhirContext r4Context = FhirContext.forR4();

	@Autowired
	AmazonClientService amazonClientService;

	@Override
	public String[] postToS3(ResponderRequest responderRequest,String folderName) {
		String[] s3PostResponse = new String[3];

		// create reporting bundle
		if (CommonUtil.postToS3()) {
			try {
				// create reporting bundle
				Bundle reportingBundle = getBundle((String) responderRequest.getRrObject(),
						responderRequest.getMetadata(), "rr");
				String request = r4Context.newJsonParser().encodeResourceToString(reportingBundle);
				// Check for Message Id and Error Handling
				logger.info("before uploads3bucket::::" + amazonClientService);
				s3PostResponse[0] = amazonClientService.uploads3bucket(
						folderName+EicrResponderParserContant.RR_JSON,
					 getOutput(request));
				logger.info("after upload RR_XML response::::" + s3PostResponse[0]);

				reportingBundle = getBundle((String) responderRequest.getEicrObject(), responderRequest.getMetadata(),
						"eicr");
				request = r4Context.newJsonParser().encodeResourceToString(reportingBundle);
				s3PostResponse[1] = amazonClientService.uploads3bucket(
						folderName+EicrResponderParserContant.EICR_FHIR_JSON,
						getOutput(request));
				logger.info("after upload EICR_FHIR_XML response ::::" + s3PostResponse[1]);

				ObjectMapper mapper = new ObjectMapper();
				String jsonStr = mapper.writeValueAsString(responderRequest.getMetadata());
				logger.info("before metadata josn uploads3bucket ::::"+reportingBundle.fhirType());
				s3PostResponse[2] = amazonClientService.uploads3bucket(
						folderName+EicrResponderParserContant.META_DATA_JSON,
						jsonStr);
				logger.info("after upload META_DATA_JSON response ::::" + s3PostResponse[2]);

			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Error posting to s3 bucket" + e.getMessage());
			}
		}
		logger.info("s3PostResponse postToS3::::"+Arrays.asList(s3PostResponse).toString());
		return s3PostResponse;
	}

	private String getOutput(String request) {
		IParser ip = r4Context.newJsonParser();
		IBaseResource ri = ip.parseResource(request);
		logger.info("before encodeResourceToString::::" + ri);
		String output = ip.setPrettyPrint(true).encodeResourceToString(ri); //
		return output;
	}

	private Bundle getBundle(String bundleObj, MetaData metaData, String fileName) {
		// create reporting bundle
		IParser target = r4Context.newJsonParser(); // new JSON parser
		Bundle rrBundle = target.parseResource(Bundle.class, bundleObj);
		return (Bundle) CommonUtil.getBundle(rrBundle, metaData, fileName);
	}

}
