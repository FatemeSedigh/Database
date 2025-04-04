package example;

import db.Entity;
import db.Trackable;
import java.util.Date;

public class Document extends Entity implements Trackable {

    private String content;
    private Date creationDate;
    private Date lastModificationDate;

    public Document(String content) {
        this.content = content;
    }



}
