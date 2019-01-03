package org.mv.po.model;

import java.io.Serializable;

/**
 * 'ProductionLine' model for API.
 *
 * @author Mikhail Vasilko
 * @since 1.0
 */
public class ProductionLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
