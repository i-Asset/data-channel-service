package eu.nimble.service.datachannel.repository;

import eu.nimble.service.datachannel.entity.tracing.EpcCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EpcCodesRepository extends JpaRepository<EpcCodes, Long> {

    EpcCodes findOneByOrderId(String orderId);

    List<EpcCodes> findByOrderIdIn(List<String> orderIds);

    Set<EpcCodes> findByCodes(String code);
}