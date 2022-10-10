package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.service.ISchematronSRV;


/**
 * Test ChangeSet generation at service level
 */
@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class ChangeSetServiceTest extends AbstractTest{

    @Autowired
    private ISchematronSRV schematronSRV;


 
    
    @Test
    @DisplayName("Test Schematron ChangeSet Service")
    void testSchematronChangeSet() throws Exception {
        schematronDataPreparation();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date queryDate = sdf.parse("2022-06-01T10:00:00");

        List<ChangeSetDTO> allSchematyrons = schematronSRV.getInsertions(null);
        assertEquals(4, allSchematyrons.size());

        List<ChangeSetDTO> insertions = schematronSRV.getInsertions(queryDate);
        assertEquals(2, insertions.size());

        List<ChangeSetDTO> deletions = schematronSRV.getDeletions(queryDate);
        assertEquals(1, deletions.size());
        
    }


    private void schematronDataPreparation() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date insertionDate = sdf.parse("2022-06-18T10:00:00");
        Date oldInsertionDate = sdf.parse("2021-05-4T10:00:00");
        Date lastUpdate = sdf.parse("2022-06-20T10:00:00");


        SchematronETY schematronA = new SchematronETY(); 
    	schematronA.setNameSchematron(SCHEMATRON_TEST_NAME_A); 
     	schematronA.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	schematronA.setTemplateIdRoot(SCHEMATRON_TEST_ROOT_A); 
    	schematronA.setVersion(SCHEMATRON_TEST_EXT_A); 
        schematronA.setInsertionDate(oldInsertionDate);
    	
    	SchematronETY schematronB = new SchematronETY(); 
    	schematronB.setNameSchematron(SCHEMATRON_TEST_NAME_B); 
     	schematronB.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	schematronB.setTemplateIdRoot(SCHEMATRON_TEST_ROOT_B); 
    	schematronB.setVersion(SCHEMATRON_TEST_EXT_B); 
        schematronB.setInsertionDate(insertionDate);
    	
    	SchematronETY schematronC = new SchematronETY(); 
    	schematronC.setNameSchematron(SCHEMATRON_TEST_NAME_C); 
     	schematronC.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	schematronC.setTemplateIdRoot(SCHEMATRON_TEST_ROOT_C); 
    	schematronC.setVersion(SCHEMATRON_TEST_EXT_C); 
        schematronC.setInsertionDate(insertionDate);
        schematronC.setLastUpdateDate(lastUpdate);

        SchematronETY schematronD = new SchematronETY(); 
    	schematronD.setNameSchematron(SCHEMATRON_TEST_NAME_D); 
     	schematronD.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	schematronD.setTemplateIdRoot(SCHEMATRON_TEST_ROOT_D); 
    	schematronD.setVersion(SCHEMATRON_TEST_EXT_D); 
        schematronD.setInsertionDate(oldInsertionDate);
        schematronD.setLastUpdateDate(lastUpdate);

        SchematronETY schematronE = new SchematronETY(); 
    	schematronE.setNameSchematron(SCHEMATRON_TEST_NAME_E); 
     	schematronE.setContentSchematron(new Binary(BsonBinarySubType.BINARY, SCHEMATRON_TEST_STRING.getBytes()));
    	schematronE.setTemplateIdRoot(SCHEMATRON_TEST_ROOT_E); 
    	schematronE.setVersion(SCHEMATRON_TEST_EXT_E); 
        schematronE.setInsertionDate(oldInsertionDate);
        schematronE.setLastUpdateDate(lastUpdate);
        schematronE.setDeleted(true);

        mongo.insert(schematronA);
        mongo.insert(schematronB);
        mongo.insert(schematronC);
        mongo.insert(schematronD);
        mongo.insert(schematronE);
    }


    @AfterAll
    public void teardown() {
        this.dropTestSchema();
    } 
    
    
}
