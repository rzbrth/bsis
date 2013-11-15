package backingform;

public class FindTestBatchBackingForm {

	private String firstDIN;

	private String lastDIN;
	
	private String testBatchNumber;
	
	private String createdBeforeDate;
	
	private String createdAfterDate;

	public FindTestBatchBackingForm() {
	}

	public String getFirstDIN() {
		return firstDIN;
	}

	public void setFirstDIN(String firstDIN) {
		this.firstDIN = firstDIN;
	}

	public String getLastDIN() {
		return lastDIN;
	}

	public void setLastDIN(String lastDIN) {
		this.lastDIN = lastDIN;
	}

	public String getTestBatchNumber() {
		return testBatchNumber;
	}

	public void setTestBatchNumber(String testBatchNumber) {
		this.testBatchNumber = testBatchNumber;
	}

	public String getCreatedBeforeDate() {
		return createdBeforeDate;
	}

	public void setCreatedBeforeDate(String createdBeforeDate) {
		this.createdBeforeDate = createdBeforeDate;
	}

	public String getCreatedAfterDate() {
		return createdAfterDate;
	}

	public void setCreatedAfterDate(String createdAfterDate) {
		this.createdAfterDate = createdAfterDate;
	}

}
