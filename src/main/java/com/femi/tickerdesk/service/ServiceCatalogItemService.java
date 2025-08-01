package com.femi.tickerdesk.service;

import com.femi.tickerdesk.enumFolder.TicketCategory;
import com.femi.tickerdesk.model.ServiceCatalogItem;

import java.util.List;
import java.util.Optional;

public interface ServiceCatalogItemService {

    ServiceCatalogItem createServiceCatalogItem(ServiceCatalogItem serviceCatalogItem);

    ServiceCatalogItem updateServiceCatalogItem(Long id, ServiceCatalogItem serviceCatalogItem);

    Optional<ServiceCatalogItem> findById(Long id);

    List<ServiceCatalogItem> findAll();

    List<ServiceCatalogItem> findByCategory(TicketCategory category);

    List<ServiceCatalogItem> findActiveItems();

    List<ServiceCatalogItem> findInactiveItems();

    List<ServiceCatalogItem> findActiveByCatgeory(TicketCategory category);

    Optional<ServiceCatalogItem> findActiveByName(String name);

    List<ServiceCatalogItem> searchByName(String name);

    ServiceCatalogItem activateItem(Long id);

    ServiceCatalogItem deactivateItem(Long id);

    void deleteItem(Long id);

    Long countActiveByCategory(TicketCategory category);

    boolean existsByName(String name);

    List<ServiceCatalogItem> findByAutoAssignToDepartment(String department);
}
