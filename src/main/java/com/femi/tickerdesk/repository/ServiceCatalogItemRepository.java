package com.femi.tickerdesk.repository;

import com.femi.tickerdesk.enumFolder.TicketCategory;
import com.femi.tickerdesk.model.ServiceCatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCatalogItemRepository extends JpaRepository<ServiceCatalogItem, Long> {

    List<ServiceCatalogItem> findByCategory(TicketCategory category);

    List<ServiceCatalogItem> findByIsActiveTrue();

    List<ServiceCatalogItem> findByIsActiveFalse();

    List<ServiceCatalogItem> findByCategoryAndIsActiveTrue(TicketCategory category);

    Optional<ServiceCatalogItem> findByNameAndIsActiveTrue(String name);

    @Query("SELECT s FROM ServiceCatalogItem s WHERE s.name LIKE %:name% AND s.isActive = true")
    List<ServiceCatalogItem> searchByName(@Param("name") String name);

    @Query("SELECT COUNT(s) FROM ServiceCatalogItem s WHERE s.category = :category AND s.isActive = true")
    Long countActiveByCatgeory(@Param("category") TicketCategory category);

    boolean existsByName(String name);

    @Query("SELECT s FROM ServiceCatalogItem s WHERE s.autoAssignToDepartment = :department AND s.isActive = true")
    List<ServiceCatalogItem> findByAutoAssignToDepartment(@Param("department") String department);
}
