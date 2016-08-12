package es.guillermogonzalezdeaguero.permissionchecking;

import es.guillermogonzalezdeaguero.permissionchecking.api.RequiredPermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 *
 * @author Guillermo González de Agüero
 */
@Stateless
public class PostsRepository {

    private List<Post> posts;

    @PostConstruct
    private void init() {
        posts = new ArrayList<>();
        posts.add(new Post(1, "someoneelse"));
        posts.add(new Post(2, "guillermo"));
        posts.add(new Post(3, "guillermo"));
        posts.add(new Post(4, "arjan"));
    }

    public Optional<Post> findById(int id) {
        return posts.stream().
                filter(p -> p.getId() == id).
                findFirst();
    }

    public void update(@RequiredPermissions(PostPermissions.UPDATE) Post post) {
        // Would propagate changes to the database here
    }

    public void delete(@RequiredPermissions(PostPermissions.DELETE) Post post) {
        // Would propagate changes to the database here
    }
}
