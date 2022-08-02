package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.DateNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ChangeSetResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;

/** 
 * 
 * @author Riccardo Bonesi
 */
@RestController
public class ChangeSetCTL extends AbstractCTL implements IChangeSetCTL{

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -805992420464600570L;

    @Autowired
    private ISchematronSRV schematronSRV;


    @Override
    public Object getSchematronChangeSet(HttpServletRequest httpServletRequest, Date lastUpdate) throws OperationException, DateNotValidException {

        // Verify not in the future
        if(lastUpdate != null && lastUpdate.after(new Date())) {
            throw new DateNotValidException("The last update date cannot be in the future");
        }

        List<ChangeSetDTO> insertions = schematronSRV.getInsertions(lastUpdate);
        List<ChangeSetDTO> deletions = schematronSRV.getDeletions(lastUpdate);
        List<ChangeSetDTO> modifications = schematronSRV.getModifications(lastUpdate);
        

        ChangeSetResponseDTO response = new ChangeSetResponseDTO();
        LogTraceInfoDTO info = getLogTraceInfo();
        response.setSpanID(info.getSpanID());
        response.setTraceID(info.getTraceID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(new Date());
        response.setInsertions(insertions);
        response.setDeletions(deletions);
        response.setModifications(modifications);
        response.setTotalNumberOfElements(insertions.size() + deletions.size() + modifications.size());

        return response;
    }
    
}
