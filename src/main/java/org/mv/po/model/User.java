package org.mv.po.model;

import java.io.Serializable;

/**
 * 'User' model for API.
 *
 * @author Mikhail Vasilko
 * @since 1.0
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
