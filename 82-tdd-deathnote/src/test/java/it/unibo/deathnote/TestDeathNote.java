package it.unibo.deathnote;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class TestDeathNote {

    private DeathNote deathNote;

    @BeforeEach
    void setUp() {
        this.deathNote = new DeathNoteImpl();
    }

    @Test
    void testRuleZeroThrowsException() {
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deathNote.getRule(0);
        });
        assertNotNull(exception.getMessage());
        assertFalse(exception.getMessage().isEmpty());
        assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testRuleNegativeThrowsException() {
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deathNote.getRule(-1);
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    void testRuleTooLargeThrowsException() {
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deathNote.getRule(DeathNote.RULES.size() + 1);
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    void testValidRulesAreNotBlank() {
        for (int i = 1; i <= DeathNote.RULES.size(); i++) {
            final String rule = deathNote.getRule(i); 
            assertNotNull(rule);
            assertFalse(rule.isEmpty());
            assertFalse(rule.isBlank());
        }
    }

    @Test
    void testWriteName() {

        final String human = "Light Yagami";
        final String anotherHuman = "L";

        assertFalse(deathNote.isNameWritten(human));
        assertFalse(deathNote.isNameWritten(anotherHuman));

        deathNote.writeName(human);

        assertTrue(deathNote.isNameWritten(human));
        assertFalse(deathNote.isNameWritten(anotherHuman));

        assertFalse(deathNote.isNameWritten(""));
    }

    @Test
    void testSetCauseBeforeNameThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            deathNote.writeDeathCause("example");
        });
    }

    @Test
    void testDefaultCauseIsHeartAttack() {
        final String human = "victim n1";
        deathNote.writeName(human);
        assertEquals("heart attack", deathNote.getDeathCause(human));
    }

    @Test
    void testSetCauseWithinTime() {
        final String human = "victim n2";
        final String cause = "karting accident";
        deathNote.writeName(human);

        final boolean causeSet = deathNote.writeDeathCause(cause);

        assertTrue(causeSet, "writeDeathCause dovrebbe ritornare true");
        assertEquals(cause, deathNote.getDeathCause(human));
    }

    @Test
    void testSetCauseAfterTimeFails() throws InterruptedException {
        final String human = "victim n3";
        deathNote.writeName(human);

        Thread.sleep(100);

        final boolean causeSet = deathNote.writeDeathCause("new cause");
        assertFalse(causeSet, "writeDeathCause dovrebbe ritornare false");
        assertEquals("Heart Attack", deathNote.getDeathCause(human));
    }

    @Test
    void testSetDetailsBeforeNameThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            deathNote.writeDetails("example");
        });
    }

    @Test
    void testDefaultDetailsAreEmpty() {
        final String human = "victim n4";
        deathNote.writeName(human);
        
        assertEquals("", deathNote.getDeathDetails(human));
    }

    @Test
    void testSetDetailsWithinTime() {
        final String human = "victim n5";
        final String details = "run for too long";

        deathNote.writeName(human);

        final boolean detailsSet = deathNote.writeDetails(details);

        assertTrue(detailsSet, "writeDetails dovrebbe ritornare true");
        assertEquals(details, deathNote.getDeathDetails(human));
    }

    @Test
    void testSetDetailsAfterTimeFails() throws InterruptedException {
        final String human = "victim n6";
        deathNote.writeName(human);

        Thread.sleep(6100);

        final boolean causeDetails = deathNote.writeDetails("new details");
        assertFalse(causeDetails, "writeDetails dovrebbe ritornare false");
        assertEquals("", deathNote.getDeathDetails(human));
    }
}
