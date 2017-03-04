package nl.remco.service.groep.model;


public class Scope {

    private String id;

    private String name;

    private String status;
    public Scope() {
    }
    
    public Scope(String id2, String string) {
    	id=id2;
    	name= string;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format(
                "Scope[id=%s, name=%s, status=%s]",
                this.id,
                this.name,
                this.status
        );
    }



}
