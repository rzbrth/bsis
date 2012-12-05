package model.testresults;

import java.util.Arrays;

import model.CustomDateFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class TestResultBackingFormValidator implements Validator {

  private Validator validator;

  public TestResultBackingFormValidator(Validator validator) {
    super();
    this.validator = validator;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(TestResultBackingForm.class, TestResult.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    System.out.println("validating");
    System.out.println(validator);
    System.out.println(obj.getClass());
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    TestResultBackingForm form = (TestResultBackingForm) obj;
    String testedOn = form.getTestedOn();
    if (!CustomDateFormatter.isDateStringValid(testedOn))
      errors.rejectValue("testResult.testedOn", "dateFormat.incorrect",
          CustomDateFormatter.getErrorMessage());
  }
}
