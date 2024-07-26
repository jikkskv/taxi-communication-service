package com.test.taxicompany.user;

import com.test.taxicompany.location.CoOrdinate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@MappedSuperclass
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
    @Email
    @NotEmpty
    private String emailId;
    private String phoneNo;
    private RegistrationType registrationType;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private CoOrdinate coOrdinate = new CoOrdinate();
}
