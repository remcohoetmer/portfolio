package nl.remco.scope.service;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Size;

import static nl.remco.scope.util.PreCondition.*;

final class Scope {

  static final int MAX_LENGTH_NAME = 50;
  static final int MAX_LENGTH_STATUS = 20;

  @Id
  private String id;

  @Size(max = Scope.MAX_LENGTH_NAME)
  private String name;

  @NotEmpty
  @Size(max = Scope.MAX_LENGTH_STATUS)
  private String status;

  public Scope() {
  }

  private Scope(Builder builder) {
    this.name = builder.name;
    this.status = builder.status;
  }

  static Builder getBuilder() {
    return new Builder();
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

  public void update(String status, String name) {
    checkStatusAndName(status, name);

    this.status = status;
    this.name = name;
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

  static class Builder {

    private String name;
    private String status;

    private Builder() {
    }

    Builder withName(String name) {
      this.name = name;
      return this;
    }

    Builder withStatus(String status) {
      this.status = status;
      return this;
    }

    Scope build() {
      Scope build = new Scope(this);

      build.checkStatusAndName(build.getStatus(), build.getName());

      return build;
    }
  }

  private void checkStatusAndName(String status, String name) {
    notNull(status, "Status cannot be null");
    notEmpty(status, "Status cannot be empty");
    isTrue(status.length() <= MAX_LENGTH_STATUS,
      "Status cannot be longer than %d characters",
      MAX_LENGTH_STATUS
    );

    if (name != null) {
      isTrue(name.length() <= MAX_LENGTH_NAME,
        "Name cannot be longer than %d characters",
        MAX_LENGTH_NAME
      );
    }
  }
}
