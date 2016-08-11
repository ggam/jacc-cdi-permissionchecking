package es.guillermogonzalezdeaguero.permissionchecking.api;

import java.security.Permission;
import java.util.Objects;

/**
 *
 * @author Guillermo González de Agüero
 */
public class ObjectPermission extends Permission {

    private Object object;
    private String permissionToCheck;

    public ObjectPermission(Object object, String permissionToCheck) {
        super(object.toString() + permissionToCheck);
        this.object = object;
        this.permissionToCheck = permissionToCheck;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String getActions() {
        return permissionToCheck;
    }

    @Override
    public boolean implies(Permission permission) {
        return equals(permission);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.object);
        hash = 67 * hash + Objects.hashCode(this.permissionToCheck);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectPermission other = (ObjectPermission) obj;
        if (!Objects.equals(this.object, other.object)) {
            return false;
        }
        if (!Objects.equals(this.permissionToCheck, other.permissionToCheck)) {
            return false;
        }
        return true;
    }

}
