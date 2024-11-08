package com.library.service;

import com.library.IService.PatronService;
import com.library.UnitOfWork.UnitOfWork;
import com.library.dto.RequestStatus;
import com.library.model.Patron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatronServiceImpl implements PatronService {

    private static final Logger logger = LoggerFactory.getLogger(PatronServiceImpl.class);

    private final UnitOfWork unitOfWork;

    @Autowired
    public PatronServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional(readOnly = true) // Marking as read-only since it's a read operation
    public List<Patron> getAllPatrons() {
        return unitOfWork.getPatronRepository().findAll();
    }

    @Override
    @Transactional(readOnly = true) // Marking as read-only for a read operation
    public Optional<Patron> getPatronById(Long id) {
        return unitOfWork.getPatronRepository().findById(id);
    }

    @Override
    @Transactional // Default transaction management for add operation
    public RequestStatus addPatron(Patron patron) {
        try {
            unitOfWork.getPatronRepository().save(patron);
            return new RequestStatus(true, "Patron added successfully.");
        } catch (Exception e) {
            logger.error("Error adding patron", e);
            // Since this is in a transaction, any exception will trigger a rollback
            return new RequestStatus(false, "Unable to add the patron. Please try again.");
        }
    }

    @Override
    @Transactional // Transactional management for update operation
    public RequestStatus updatePatron(Long id, Patron patron) {
        try {
            Optional<Patron> existingPatron = unitOfWork.getPatronRepository().findById(id);
            if (existingPatron.isPresent()) {
                patron.setId(id);
                unitOfWork.getPatronRepository().save(patron);
                return new RequestStatus(true, "Patron updated successfully.");
            } else {
                logger.warn("Attempted to update non-existing patron with ID " + id);
                return new RequestStatus(false, "Patron does not exist.");
            }
        } catch (Exception e) {
            logger.error("Error updating patron with ID " + id, e);
            // Rollback in case of error
            return new RequestStatus(false, "Unable to update the patron. Please try again.");
        }
    }

    @Override
    @Transactional // Transactional management for delete operation
    public RequestStatus deletePatron(Long id) {
        try {
            if (unitOfWork.getPatronRepository().existsById(id)) {
                unitOfWork.getPatronRepository().deleteById(id);
                return new RequestStatus(true, "Patron deleted successfully.");
            } else {
                logger.warn("Attempted to delete non-existing patron with ID " + id);
                return new RequestStatus(false, "Patron does not exist.");
            }
        } catch (Exception e) {
            logger.error("Error deleting patron with ID " + id, e);
            // Rollback in case of error
            return new RequestStatus(false, "Unable to delete the patron. Please try again.");
        }
    }
}
