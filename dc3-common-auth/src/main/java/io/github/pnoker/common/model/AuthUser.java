package io.github.pnoker.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author linys
 * @since 2023.04.16
 */
@Data
public class AuthUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    private String userId;

    /**
     * name
     */
    private String userName;

    /**
     * tenant
     */
    private String tenantId;

    /**
     * roles
     */
    private Set<String> roleCodeSet;

    /**
     * resources
     */
    private Set<String> resourceCodeSet;

}
