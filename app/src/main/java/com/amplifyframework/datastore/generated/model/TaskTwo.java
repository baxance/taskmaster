package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the TaskTwo type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "TaskTwos")
@Index(name = "forTeam", fields = {"teamId"})
public final class TaskTwo implements Model {
  public static final QueryField ID = field("TaskTwo", "id");
  public static final QueryField TITLE = field("TaskTwo", "title");
  public static final QueryField BODY = field("TaskTwo", "body");
  public static final QueryField STATE = field("TaskTwo", "state");
  public static final QueryField KEY = field("TaskTwo", "key");
  public static final QueryField TEAM = field("TaskTwo", "teamId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String body;
  private final @ModelField(targetType="String") String state;
  private final @ModelField(targetType="String") String key;
  private final @ModelField(targetType="Team", isRequired = true) @BelongsTo(targetName = "teamId", type = Team.class) Team team;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getBody() {
      return body;
  }
  
  public String getState() {
      return state;
  }
  
  public String getKey() {
      return key;
  }
  
  public Team getTeam() {
      return team;
  }
  
  private TaskTwo(String id, String title, String body, String state, String key, Team team) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.state = state;
    this.key = key;
    this.team = team;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      TaskTwo taskTwo = (TaskTwo) obj;
      return ObjectsCompat.equals(getId(), taskTwo.getId()) &&
              ObjectsCompat.equals(getTitle(), taskTwo.getTitle()) &&
              ObjectsCompat.equals(getBody(), taskTwo.getBody()) &&
              ObjectsCompat.equals(getState(), taskTwo.getState()) &&
              ObjectsCompat.equals(getKey(), taskTwo.getKey()) &&
              ObjectsCompat.equals(getTeam(), taskTwo.getTeam());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getBody())
      .append(getState())
      .append(getKey())
      .append(getTeam())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("TaskTwo {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("state=" + String.valueOf(getState()) + ", ")
      .append("key=" + String.valueOf(getKey()) + ", ")
      .append("team=" + String.valueOf(getTeam()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static TaskTwo justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new TaskTwo(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      body,
      state,
      key,
      team);
  }
  public interface TitleStep {
    TeamStep title(String title);
  }
  

  public interface TeamStep {
    BuildStep team(Team team);
  }
  

  public interface BuildStep {
    TaskTwo build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep body(String body);
    BuildStep state(String state);
    BuildStep key(String key);
  }
  

  public static class Builder implements TitleStep, TeamStep, BuildStep {
    private String id;
    private String title;
    private Team team;
    private String body;
    private String state;
    private String key;
    @Override
     public TaskTwo build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new TaskTwo(
          id,
          title,
          body,
          state,
          key,
          team);
    }
    
    @Override
     public TeamStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep team(Team team) {
        Objects.requireNonNull(team);
        this.team = team;
        return this;
    }
    
    @Override
     public BuildStep body(String body) {
        this.body = body;
        return this;
    }
    
    @Override
     public BuildStep state(String state) {
        this.state = state;
        return this;
    }
    
    @Override
     public BuildStep key(String key) {
        this.key = key;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String body, String state, String key, Team team) {
      super.id(id);
      super.title(title)
        .team(team)
        .body(body)
        .state(state)
        .key(key);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder team(Team team) {
      return (CopyOfBuilder) super.team(team);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }
    
    @Override
     public CopyOfBuilder state(String state) {
      return (CopyOfBuilder) super.state(state);
    }
    
    @Override
     public CopyOfBuilder key(String key) {
      return (CopyOfBuilder) super.key(key);
    }
  }
  
}
