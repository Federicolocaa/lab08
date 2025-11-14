package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.deathnote.api.DeathNote;

/**
 * Implements the DeathNote interface.
 */
public final class DeathNoteImpl implements DeathNote {

    private static final int CAUSE_TIMEOUT = 40;
    private static final int DETAILS_TIMEOUT = 6040;
    private final Map<String, DeathData> notebook = new HashMap<>();

    // Lo uso per ricordami del nome
    private String lastWrittenName;

    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()) { 
            throw new IllegalArgumentException("Numero di regola non valido: " + ruleNumber);
        } else { 
            return RULES.get(ruleNumber - 1);
        }
    }

    @Override
    public void writeName(final String name) {
        if (name == null) {
            throw new NullPointerException("Nome non può essere nullo."); //NOPMD
        } else {
            this.notebook.put(name, new DeathData());
            this.lastWrittenName = name;
        }
    }

    @Override
    public boolean writeDeathCause(final String cause) {
        if (cause == null || this.lastWrittenName == null) {
            throw new IllegalStateException("Cause nulle o nessun nome scritto a cui applicarli");
        }
        final DeathData data = this.notebook.get(this.lastWrittenName); // Pacchetto dati dell'ultimo nome inserito
        final long timePassed = System.currentTimeMillis() - data.writeTime; // Tempo passato
        if (timePassed > CAUSE_TIMEOUT) {
            return false;
        } else {
            data.setCause(cause);
            return true;
        }
    }

    @Override
    public boolean writeDetails(final String details) {
        if (details == null || this.lastWrittenName == null) {
            throw new IllegalStateException("Dettagli nulli o nessun nome scritto a cui applicarli");
        }
        final DeathData data = this.notebook.get(this.lastWrittenName);
        final long timePassed = System.currentTimeMillis() - data.writeTime; 
        if (timePassed > DETAILS_TIMEOUT) {
            return false;
        } else {
            data.setDetails(details);
            return true;
        }
    }

    @Override
    public String getDeathCause(final String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("Nome non scritto sul deathNote");
        }
        return this.notebook.get(name).getCause();
    }

    @Override
    public String getDeathDetails(final String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("Nome non scritto sul deathNote");
        }
        return this.notebook.get(name).getDetails();
    }

    @Override
    public boolean isNameWritten(final String name) {
        return this.notebook.containsKey(name);
    }

    private static class DeathData {
        private String cause;
        private String details;
        private final long writeTime;

        DeathData() {
            this.writeTime = System.currentTimeMillis();
            this.cause = "heart attack";
            this.details = "";
        }

        String getCause() {
            return this.cause;
        }

        String getDetails() {
            return this.details;
        }

        long getWriteTime() {
            return this.writeTime;
        }

        void setCause(final String cause) {
            this.cause = cause;
        }

        void setDetails(final String details) {
            this.details = details;
        }
    }
}
