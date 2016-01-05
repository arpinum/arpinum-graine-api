package fr.arpinum.seed.command;

import com.google.common.collect.Lists;

import java.util.List;

public class ExceptionValidation extends RuntimeException {


    public ExceptionValidation(List<String> messages) {
        this.messages.addAll(messages);
    }

    public List<String> messages() {
        return messages;
    }

    private List<String> messages = Lists.newArrayList();
}
