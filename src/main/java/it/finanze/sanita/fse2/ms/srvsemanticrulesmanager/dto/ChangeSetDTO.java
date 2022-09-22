package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.io.Serializable;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetDTO implements Serializable {

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 8943366877983853446L; 
	

	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String id; 

	Payload description;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload implements Serializable {
    	
        /**
		 * Serial Version UID 
		 */
		private static final long serialVersionUID = -525579157952607584L; 
		
		@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
        String templateIdRoot;
        @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
        String templateIdExtension;
    }

}