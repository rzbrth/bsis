package model.bloodtyping;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class RawBloodTestGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @Column(length=30)
  private String testGroupName;

  @ManyToMany
  private List<BloodTypingTest> bloodTestsInGroup;

  private Boolean isDeleted;

  public Integer getId() {
    return id;
  }

  public String getTestGroupName() {
    return testGroupName;
  }

  public List<BloodTypingTest> getBloodTestsInGroup() {
    return bloodTestsInGroup;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setTestGroupName(String testGroupName) {
    this.testGroupName = testGroupName;
  }

  public void setBloodTestsInGroup(List<BloodTypingTest> bloodTestsInGroup) {
    this.bloodTestsInGroup = bloodTestsInGroup;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}