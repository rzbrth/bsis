package model.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class FormField {

  public static final String FIELD = "field";
  public static final String DISPLAY_NAME = "displayName";
  public static final String DEFAULT_DISPLAY_NAME = "defaultDisplayName";
  public static final String DEFAULT_VALUE = "defaultValue";
  public static final String HIDDEN = "hidden";
  public static final String DERIVED = "derived";
  public static final String SOURCE_FIELD = "sourceField";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  @Column(length=30)
  private String form;

  @Column(length=30)
  private String field;

  @Column(length=30)
  private String displayName;

  @Column(length=30)
  private String defaultDisplayName;

  @Column
  private Integer maxLength;

  @Lob
  private String defaultValue;

  private Boolean hidden = false;

  private Boolean derived = false;

  private Boolean isRequired = false;

  private Boolean autoGenerate = false;

  // copy value from another field
  @Column(length=30)
  private String sourceField;

  public Long getId() {
    return id;
  }

  public String getForm() {
    return form;
  }

  public String getField() {
    return field;
  }

  public String getDisplayName() {
    if (displayName == null || displayName.trim().equals(""))
      return getDefaultDisplayName();
    return displayName;
  }

  public String getDefaultDisplayName() {
    return defaultDisplayName;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public Boolean getDerived() {
    return derived;
  }

  public String getSourceField() {
    return sourceField;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setForm(String form) {
    this.form = form;
  }

  public void setField(String field) {
    this.field = field;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setDefaultDisplayName(String defaultDisplayName) {
    this.defaultDisplayName = defaultDisplayName;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public void setDerived(Boolean derived) {
    this.derived = derived;
  }

  public void setSourceField(String sourceField) {
    this.sourceField = sourceField;
  }

  public void copy(FormField formField) {
    this.displayName = formField.displayName;
    this.defaultValue = formField.defaultValue;
    this.hidden = formField.hidden;
    this.isRequired = formField.isRequired;
    this.autoGenerate = formField.autoGenerate;
    this.derived = formField.derived;
    this.sourceField = formField.sourceField;
    this.maxLength = formField.maxLength;
  }

  public Boolean getIsRequired() {
    return isRequired;
  }

  public void setIsRequired(Boolean isRequired) {
    this.isRequired = isRequired;
  }

  public Boolean getAutoGenerate() {
    return autoGenerate;
  }

  public void setAutoGenerate(Boolean autoGenerate) {
    this.autoGenerate = autoGenerate;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }
}