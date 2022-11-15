package com.epam.jwd.audiotrack_ordering.web.command;

public interface Command {

    CommandResponse execute(CommandRequest request);

    static Command of(String name){
        return CommandRegistry.of(name);
    };
}
