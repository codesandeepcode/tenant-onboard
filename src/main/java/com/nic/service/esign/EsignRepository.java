package com.nic.service.esign;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Repository
class EsignRepository {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final EsignAuditRepository repository;

	private final ObjectWriter writer;

	EsignRepository(EsignAuditRepository repository, ObjectMapper mapper) {
		this.repository = repository;
		this.writer = mapper.writer();
	}

	Integer getEsignTransactionSequenceNo() {
		log.debug("Generating sequence no for Esign transaction");
		return repository.getTransactionSequenceNo();
	}

	@Transactional
	void saveEsignTransactionDetails(Integer applicationRefId, String signingPerson, ESignRequestModel requestModel) {
		EsignTransactionEntity entity = new EsignTransactionEntity(applicationRefId, signingPerson, requestModel);

		if (log.isDebugEnabled()) {
			try {
				log.debug("For Application ref id {}, saving request model to db : {}", applicationRefId,
						writer.writeValueAsString(entity));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		repository.save(entity);
	}

	/**
	 * @return Application Reference Id
	 */
	@Transactional
	Integer updateEsignTransactionDetails(ESignResponseModel responseModel) {
		EsignTransactionEntity transactionDetails = repository.findByTransactionId(responseModel.getTransactionId())
				.orElseThrow(() -> new IllegalArgumentException("Given Transaction Id cannot be retrieved"));

		transactionDetails.updateAuditDetails(responseModel);

		if (log.isDebugEnabled()) {
			try {
				log.debug("For the complete transaction details from esp collected {}",
						writer.writeValueAsString(transactionDetails));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return repository.save(transactionDetails).getApplicationId();
	}

	@Transactional(readOnly = true)
	Optional<ESignResponseModel> fetchEsignResponse(Integer applicationReferenceId) {
		Optional<EsignTransactionEntity> transactionDetails = repository
				.returnLatestESignResponse(applicationReferenceId);
		if (!transactionDetails.isPresent())
			return Optional.empty();

		return Optional.of(new ESignResponseModel(transactionDetails.get()));
	}

	@Transactional(readOnly = true)
	Optional<String> getSigningPerson(Integer applicationReferenceId) {
		Optional<EsignTransactionEntity> esignResponse = repository.returnLatestESignResponse(applicationReferenceId);
		if (!esignResponse.isPresent())
			return Optional.empty();

		return Optional.of(esignResponse.get().getSigningPerson());
	}

}
