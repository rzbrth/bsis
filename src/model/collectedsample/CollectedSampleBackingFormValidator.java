package model.collectedsample;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import model.collectionbatch.CollectionBatch;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.location.Location;
import model.worksheet.WorksheetBackingForm;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import utils.CustomDateFormatter;
import viewmodel.CollectedSampleViewModel;
import controller.UtilController;

public class CollectedSampleBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public CollectedSampleBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(FindCollectedSampleBackingForm.class,
                         CollectedSampleBackingForm.class,
                         CollectedSample.class,
                         CollectedSampleViewModel.class,
                         WorksheetBackingForm.class
                         ).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;

    ValidationUtils.invokeValidator(validator, obj, errors);

    CollectedSampleBackingForm form = (CollectedSampleBackingForm) obj;
    updateAutoGeneratedFields(form);

    if (utilController.isDuplicateCollectionNumber(form.getCollectedSample()))
      errors.rejectValue("collectedSample.collectionNumber", "collectionNumber.nonunique",
          "There exists a collection with the same collection number.");

    String collectedOn = form.getCollectedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(collectedOn))
      errors.rejectValue("collectedSample.collectedOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    updateRelatedEntities(form);
    inheritParametersFromCollectionBatch(form, errors);
    Donor donor = form.getDonor();
    if (donor != null) {
      String errorMessageDonorAge = utilController.verifyDonorAge(donor);
      if (StringUtils.isNotBlank(errorMessageDonorAge))
        errors.rejectValue("collectedSample.donor", "donor.age", errorMessageDonorAge);
      
      String errorMessageDonorDeferral = utilController.isDonorDeferred(donor);
      if (StringUtils.isNotBlank(errorMessageDonorDeferral))
        errors.rejectValue("collectedSample.donor", "donor.deferral", errorMessageDonorDeferral);
      
      if (donor.getDonorStatus().equals(DonorStatus.POSITIVE_TTI))
        errors.rejectValue("collectedSample.donor", "donor.tti", "Donor is not allowed to donate.");
    }
    
    utilController.commonFieldChecks(form, "collectedSample", errors);
  }

  private void inheritParametersFromCollectionBatch(
      CollectedSampleBackingForm form, Errors errors) {
    if (form.getUseParametersFromBatch()) {
      CollectionBatch collectionBatch = form.getCollectionBatch();
      if (collectionBatch == null) {
        errors.rejectValue("collectedSample.collectionBatch", "collectionbatch.notspecified", "Collection batch should be specified");
        return;
      }
      Location center = collectionBatch.getCollectionCenter();
      if (center == null) {
        errors.rejectValue("useParametersFromBatch", "collectionCenter.notspecified",
            "Collection center not present in batch and is required.");
      } else {
        form.setCollectionCenter(center.getId().toString());
      }
      Location site = collectionBatch.getCollectionSite();
      if (site == null) {
        errors.rejectValue("useParametersFromBatch", "collectionSite.notspecified",
            "Collection site not present in batch and is required.");
      } else {
        form.setCollectionSite(site.getId().toString());
      }
    }
  }

  private void updateAutoGeneratedFields(CollectedSampleBackingForm form) {
    if (StringUtils.isBlank(form.getCollectionNumber()) &&
        utilController.isFieldAutoGenerated("collectedSample", "collectionNumber")) {
      form.setCollectionNumber(utilController.getNextCollectionNumber());
    }
    if (StringUtils.isBlank(form.getCollectedOn()) &&
        utilController.doesFieldUseCurrentTime("collectedSample", "collectedOn")) {
      form.getCollectedSample().setCollectedOn(new Date());
    }
  }

  @SuppressWarnings("unchecked")
  private void updateRelatedEntities(CollectedSampleBackingForm form) {
    Map<String, Object> bean = null;
    try {
      bean = BeanUtils.describe(form);
      Donor donor = utilController.findDonorInForm(bean);
      form.setDonor(donor);
      CollectionBatch collectionBatch = utilController.findCollectionBatchInForm(bean);
      form.setCollectionBatch(collectionBatch);
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
