package com.serendipity.ecommerce.event;

import com.serendipity.ecommerce.enumeration.EventoType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class NewUsuarioEvento extends ApplicationEvent {
    private EventoType eventoType;
    private String email;

    public NewUsuarioEvento(EventoType eventoType, String email) {
        super(email);
        this.eventoType = eventoType;
        this.email = email;
    }
}
