/**
 * 
 */
package controller;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityExistsException;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import repository.CollectedSampleRepository;
import repository.ProductRepository;
import repository.ProductTypeRepository;
import model.collectedsample.CollectedSample;
import model.product.Product;
import model.product.ProductStatus;
import model.producttype.ProductType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class ProductControllerTest {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CollectedSampleRepository collectedSampleRepository;
	
	@Autowired
	private ProductTypeRepository productTypeRepository;
	
	// Test case for record new productComponents
	@Test
	public void recordNewProductComponentsTest() {
		
		 Product savedProduct = null;
		
				ProductType productType2 = productRepository.findProductTypeBySelectedProductType(1);
	      String collectionNumber = "D0001";
	      String status = "QUARANTINED";
	      long productId = 1L;
	      
	      if(collectionNumber.contains("-")){
	      	collectionNumber = collectionNumber.split("-")[0];
	      }
	      String sortName = productType2.getProductTypeCode();
	      int noOfUnits = 3;
	      long collectedSampleID = 1;
	      
	      String createdPackNumber = collectionNumber +"-"+sortName;
	      
	      // Add New product
	      if(!status.equalsIgnoreCase("PROCESSED")){
	      if(noOfUnits > 0 ){
	      	
	      	for(int i=1; i <= noOfUnits ; i++){
	      		try{
		        	Product product = new Product();
		          product.setIsDeleted(false);
		          product.setDonationIdentificationNumber(createdPackNumber+"-"+i);
		          Calendar c=new GregorianCalendar();
		          System.out.println("after :"+ productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
		          c.add(Calendar.MINUTE, productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
		          Date expiredate=c.getTime();
		          
		          
		          product.setCreatedOn(new Date());
		          product.setExpiresOn(expiredate);
		          ProductType productType = new ProductType();
		          productType.setProductType(productType2.getProductType());
		          productType.setId(productType2.getId());
		          product.setProductType(productType);
		          CollectedSample collectedSample = new CollectedSample();
		          collectedSample.setId(collectedSampleID);
		          product.setCollectedSample(collectedSample);
		          product.setStatus(ProductStatus.QUARANTINED);
			        productRepository.addProduct(product);
	
			        // Once product save successfully update selected product status with processed
			        productRepository.updateProductByProductId(productId);
			        
			      } catch (EntityExistsException ex) {
			        ex.printStackTrace();
			      } catch (Exception ex) {
			        ex.printStackTrace();
			      }
	      	}
	      }
	      else{
	      	
	      	try{
		        	Product product = new Product();
		          product.setIsDeleted(false);
		          product.setDonationIdentificationNumber(createdPackNumber);

		          Calendar c=new GregorianCalendar();
		          System.out.println("after :"+ productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
		          c.add(Calendar.MINUTE, productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
		          Date expiredate=c.getTime();
		          
		          product.setCreatedOn(new Date());
		          product.setExpiresOn(expiredate);
		          ProductType productType = new ProductType();
		          productType.setProductType(productType2.getProductType());
		          productType.setId(productType2.getId());
		          product.setProductType(productType);
		          CollectedSample collectedSample = new CollectedSample();
		          collectedSample.setId(collectedSampleID);
		          product.setCollectedSample(collectedSample);
		          product.setStatus(ProductStatus.QUARANTINED);
			        productRepository.addProduct(product);
			        productRepository.updateProductByProductId(productId);
			        
			      } catch (EntityExistsException ex) {
			        ex.printStackTrace();
			      } catch (Exception ex) {
			        ex.printStackTrace();
			      }
		    	}
	      }
		}
	
}


