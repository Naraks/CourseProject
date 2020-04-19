package com.github.naraks.myappserver.dto.session;

import com.github.naraks.myappserver.dto.journal.JournalItemDTO;
import com.github.naraks.myappserver.entity.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class SessionItemDTO extends JournalItemDTO {

    private String id;
    private String name;
    private Date insertDate;
    private Double result;

    public SessionItemDTO(Session session){
        this.id = session.getId().toString();
        this.name = session.getFullName();
        this.result = session.getPercent();
    }
}
