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

    @Override
    public void setCreationDate(Date date){
        this.creationDate = new Date(date.getTime());
    }

    @Override
    public Date getCreationDate(){
        return new Date(creationDate.getTime());
    }

    @Override
    public void setLastModificationDate(Date date){
        this.lastModificationDate = new Date(date.getTime());
    }

    @Override
    public Date getLastModificationDate(){
        return new Date(lastModificationDate.getTime());
    }


}
