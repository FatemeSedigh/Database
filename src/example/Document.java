package example;

import db.Entity;
import db.Trackable;

import java.awt.font.TextHitInfo;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public abstract Document copy(){

        Document copy = new Document(this.content);
        copy.id = this.id;

        if (this.creationDate != null){
            copy.creationDate = new Date(this.creationDate.getTime());
        }

        if (this.lastModificationDate != null){
            copy.lastModificationDate = new Date(this.lastModificationDate.getTime());
        }

        return copy;
    }

    @Override
    public int getEntityCode() {
        return 15;
    }
}
