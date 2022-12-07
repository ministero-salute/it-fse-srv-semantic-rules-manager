/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.changes.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/** 
 * 
 */
@RestController
public class ChangeSetCTL extends AbstractCTL implements IChangeSetCTL{

    @Autowired
    private ISchematronSRV service;


    @Override
    public Object getSchematronChangeSet(HttpServletRequest httpServletRequest, Date lastUpdate) throws OperationException {

        List<ChangeSetDTO> insertions = service.getInsertions(lastUpdate);
        List<ChangeSetDTO> deletions = service.getDeletions(lastUpdate);
        long collectionSize = service.getCollectionSize();
        long totalNumberOfElements = insertions.size() + deletions.size();

        ChangeSetResDTO response = new ChangeSetResDTO();
        LogTraceInfoDTO info = getLogTraceInfo();
        response.setSpanID(info.getSpanID());
        response.setTraceID(info.getTraceID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(new Date());
        response.setInsertions(insertions);
        response.setDeletions(deletions);
        response.setTotalNumberOfElements(totalNumberOfElements);
        response.setCollectionSize(collectionSize);

        return response;
    }
    
}
