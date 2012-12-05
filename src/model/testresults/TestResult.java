package model.testresults;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import model.bloodtest.BloodTest;
import model.bloodtest.BloodTestExists;
import model.bloodtest.BloodTestResult;
import model.bloodtest.BloodTestResultExists;
import model.collectedsample.CollectedSample;
import model.collectedsample.CollectedSampleExists;
import model.modificationtracker.ModificationTracker;
import model.user.User;

@Entity
public class TestResult implements ModificationTracker {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
	private Long id;

	@NotNull
	@CollectedSampleExists
	@ManyToOne(optional=false)
	private CollectedSample collectedSample;

  @Temporal(TemporalType.TIMESTAMP)
	private Date testedOn;

  @BloodTestExists
  @ManyToOne
	private BloodTest bloodTest;

  @BloodTestResultExists
  @ManyToOne
	private BloodTestResult bloodTestResult;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdated;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @ManyToOne
  private User createdBy;

  @ManyToOne
  private User lastUpdatedBy;

	@Lob
	private String notes;

  private Boolean isDeleted;

  public TestResult() {
  }

  public Long getId() {
    return id;
  }

  public void copy(TestResult testResult) {
    assert (this.id == testResult.id);
    this.bloodTest = testResult.bloodTest;
    this.testedOn = testResult.testedOn;
    this.bloodTestResult = testResult.bloodTestResult;
    this.notes = testResult.notes;
    this.isDeleted = testResult.isDeleted;
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }

  public Date getTestedOn() {
    return testedOn;
  }

  public BloodTest getBloodTest() {
    return bloodTest;
  }

  public BloodTestResult getBloodTestResult() {
    return bloodTestResult;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public User getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    this.collectedSample = collectedSample;
  }

  public void setTestedOn(Date testedOn) {
    this.testedOn = testedOn;
  }

  public void setName(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
  }

  public void setBloodTestResult(BloodTestResult bloodTestResult) {
    this.bloodTestResult = bloodTestResult;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
  }
}