package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.deathnote.api.DeathNote;

/**
 * Implements the DeathNote interface.
 */
public final class DeathNoteImpl implements DeathNote {

    
    private final Map<String, DeathData> notebook = new HashMap<>();

    // Lo uso per ricordami del nome
    private String lastWrittenName = null;

    private static class DeathData {
        String cause;
        String details;
        final long writeTime;

        DeathData() {
            this.writeTime = System.currentTimeMillis();
            this.cause = "heart attack";
            this.details = "";
        }
    }

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
        if (name == null){
            throw new NullPointerException("Nome: " + name + " non valido.");
        }else {
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
        if (timePassed > 40) {
            return false;
        } else {
            data.cause = cause;
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
        if (timePassed > 6040) {
            return false;
        } else {
            data.details = details;
            return true;
        }

    }

    @Override
    public String getDeathCause(final String name) {
        if (isNameWritten(name) == false) {
            throw new IllegalArgumentException("Nome non scritto sul deathNote");
        }
        return this.notebook.get(name).cause;
    }

    @Override
    public String getDeathDetails(final String name) {
        if(isNameWritten(name) == false) {
            throw new IllegalArgumentException("Nome non scritto sul deathNote");
        }
        return this.notebook.get(name).details;
    }

    @Override
    public boolean isNameWritten(final String name) {
        return this.notebook.containsKey(name);
    }

}
