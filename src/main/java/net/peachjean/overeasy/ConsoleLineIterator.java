package net.peachjean.overeasy;

import jline.console.ConsoleReader;
import jline.console.UserInterruptException;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

class ConsoleLineIterator implements Iterator<String> {
    private final ConsoleReader reader;
    private final Prompt prompt;

    private String next;
    ConsoleLineIterator(ConsoleReader reader, Prompt prompt) {
        this.reader = reader;
        this.prompt = prompt;
    }

    private void updateNext() {
        while(!readNextLine());
    }

    private boolean readNextLine() {
        try {
            this.next = this.reader.readLine(prompt.getPrompt());
            return true;
        } catch (UserInterruptException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read line.", e);
        }
    }


    @Override
    public boolean hasNext() {
        if(this.next == null)
        {
            this.updateNext();
        }
        return next != null;
    }

    @Override
    public String next() {
        if(next == null)
        {
            throw new NoSuchElementException();
        }
        String localNext = this.next;
        this.next = null;
        return localNext;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove from this.");
    }
}
