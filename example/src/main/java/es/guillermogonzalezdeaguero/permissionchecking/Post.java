package es.guillermogonzalezdeaguero.permissionchecking;

/**
 * Sample entity
 *
 * @author Guillermo González de Agüero
 */
public class Post {

    private int id;
    private String author;

    public Post() {
    }

    public Post(int id, String author) {
        this.id = id;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
