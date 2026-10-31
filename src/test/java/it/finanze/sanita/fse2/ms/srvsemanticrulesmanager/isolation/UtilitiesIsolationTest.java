package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.isolation;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.validators.impl.*;

@Tag("ISOLATION")
class UtilitiesIsolationTest {
    @Test void versionComparisonUsesMajorThenMinorAndRejectsMalformedVersions() {
        assertTrue(ValidationUtility.isMajorVersion("2.0", "1.9"));
        assertTrue(ValidationUtility.isMajorVersion("1.10", "1.9"));
        assertFalse(ValidationUtility.isMajorVersion("1.9", "1.9"));
        assertFalse(ValidationUtility.isMajorVersion("1.8", "1.9"));
        assertFalse(ValidationUtility.isMajorVersion("0.9", "1.0"));
        assertThrows(NumberFormatException.class, () -> ValidationUtility.isMajorVersion("x.0", "1.0"));
    }
    @Test void validatorsHandleNullValidInvalidAndFutureInputs() {
        var ids = new ObjectIdValidator(); ids.initialize(null);
        assertFalse(ids.isValid(null, null)); assertFalse(ids.isValid("bad", null));
        assertTrue(ids.isValid("507f1f77bcf86cd799439011", null));
        var dates = new NoFutureDateValidator(); dates.initialize(null);
        assertTrue(dates.isValid(null, null)); assertTrue(dates.isValid(new Date(0), null));
        assertFalse(dates.isValid(new Date(System.currentTimeMillis() + 60000), null));
    }
    @Test void stringAndIdentifierUtilitiesHaveStableFormatsAndUniqueValues() {
        assertTrue(StringUtility.isNullOrEmpty(null)); assertTrue(StringUtility.isNullOrEmpty(""));
        assertFalse(StringUtility.isNullOrEmpty(" "));
        assertTrue(StringUtility.generateTransactionUID(null).matches("[a-f0-9]{32}"));
        for (UIDModeEnum mode : UIDModeEnum.values()) {
            String first = StringUtility.generateTransactionUID(mode);
            assertFalse(first.isEmpty()); assertNotEquals(first, StringUtility.generateTransactionUID(mode));
        }
        assertEquals(64, StringUtility.generateTransactionUID(UIDModeEnum.UUID_UUID).length());
        assertNull(SystemTypeEnum.NONE.value()); assertEquals("TS", SystemTypeEnum.TS.value());
    }

    @Test void identifierModesAndLogCodesRetainTheirPublicContract() {
        assertSame(UIDModeEnum.IP_UUID, UIDModeEnum.get(1));
        assertSame(UIDModeEnum.HOSTNAME_UUID, UIDModeEnum.get(2));
        assertSame(UIDModeEnum.UUID_UUID, UIDModeEnum.get(3));
        assertNull(UIDModeEnum.get(99)); assertNull(UIDModeEnum.get(null));
        assertEquals("IP_UUID", UIDModeEnum.IP_UUID.getDescription());
        assertEquals("POST-SCHEMATRON", OperationLogEnum.POST_SCHEMATRON.getCode());
        assertEquals("Aggiunta Schematron", OperationLogEnum.POST_SCHEMATRON.getDescription());
        assertEquals("KO-SCHEMATRON-CREATE", ErrorLogEnum.KO_SCHEMATRON_CREATE.getCode());
        assertEquals("Error while creating Schematron", ErrorLogEnum.KO_SCHEMATRON_CREATE.getDescription());
        assertEquals("OK", ResultLogEnum.OK.getCode());
        assertEquals("Operazione eseguita con successo", ResultLogEnum.OK.getDescription());
    }
}
