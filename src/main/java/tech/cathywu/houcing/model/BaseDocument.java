package tech.cathywu.houcing.model;

import org.bson.types.ObjectId;

import javax.persistence.Id;

public class BaseDocument {
    @Id
    protected ObjectId id;

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.setId(new ObjectId(id));
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
