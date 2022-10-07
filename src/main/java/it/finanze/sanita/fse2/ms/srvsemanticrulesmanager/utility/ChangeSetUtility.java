package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;

public final class ChangeSetUtility {

	private ChangeSetUtility() {
		
	}

	/**
	 * Creates a ChangesetDTO from a SchematronETY.
	 * @param entity
	 * @return
	 */
	public static ChangeSetDTO schematronToChangeset(SchematronETY entity) {
		return new ChangeSetDTO(entity.getId(), new ChangeSetDTO.Payload(entity.getTemplateIdRoot(), entity.getVersion()));
    }

}
