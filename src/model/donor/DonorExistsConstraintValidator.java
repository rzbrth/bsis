package model.donor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import repository.DonorRepository;

public class DonorExistsConstraintValidator implements
    ConstraintValidator<DonorExists, Donor> {

  @Autowired
  private DonorRepository donorRepository;

  @Override
  public void initialize(DonorExists constraint) {

  }

  public boolean isValid(Donor target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {
    Donor donor = donorRepository.findDonorById(target.getId());
    if (donor != null) {
     return true;
    }
   } catch (Exception e) {
    e.printStackTrace();
   }
   return false;
  }

  public void setDonorRepository(DonorRepository donorRepository) {
    this.donorRepository = donorRepository;
  }
}