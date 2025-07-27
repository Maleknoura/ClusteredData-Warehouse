package com.progressoft.clustereddata_warehouse.repository;

import com.progressoft.clustereddata_warehouse.entity.FxDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FxDealRepository extends JpaRepository<FxDeal, String> {

}
