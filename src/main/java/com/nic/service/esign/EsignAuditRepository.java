package com.nic.service.esign;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface EsignAuditRepository extends CrudRepository<EsignTransactionEntity, Integer> {

	Optional<EsignTransactionEntity> findByTransactionId(String transactionId);

	Optional<EsignTransactionEntity> findByApplicationId(Integer applicationId);

	// TODO: need to modify so that based on latest valid response, generate
	// signature... confirm it with pooja ma'am...
	@Query(value = "SELECT a.* FROM esp_transaction_details a "
			+ "INNER JOIN (SELECT application_id, MAX(response_timestamp) AS max_timestamp FROM esp_transaction_details GROUP BY application_id) b ON a.application_id=b.application_id "
			+ "WHERE a.application_id=:applicationId AND a.response_status='1' AND a.response_timestamp=b.max_timestamp", nativeQuery = true)
	Optional<EsignTransactionEntity> returnLatestESignResponse(@Param("applicationId") Integer applicationId);

	@Query(value = "SELECT nextval('esp_transaction_number_seq')", nativeQuery = true)
	Integer getTransactionSequenceNo();

}
