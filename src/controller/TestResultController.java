package controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bloodtesting.TestBatch;
import model.collectedsample.CollectedSample;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import repository.SequenceNumberRepository;
import repository.TestBatchRepository;
import viewmodel.CollectedSampleViewModel;
import backingform.FindTestBatchBackingForm;
import backingform.FindTestResultBackingForm;

@Controller
public class TestResultController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private UtilController utilController;
  
  @Autowired
  private TestBatchRepository testBatchRepository;
  
  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;

  public TestResultController() {
  }

  @RequestMapping(value = "/findTestResultFormGenerator", method = RequestMethod.GET)
  public ModelAndView findTestResultFormGenerator(HttpServletRequest request) {

    FindTestResultBackingForm form = new FindTestResultBackingForm();

    ModelAndView mv = new ModelAndView("testresults/findTestResultForm");
    mv.addObject("findTestResultForm", form);

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "testResults.find");
    mv.addObject("tips", tips);

    // to ensure custom field names are displayed in the form
    mv.addObject("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping("/findTestResult")
  public ModelAndView findTestResult(HttpServletRequest request,
      @ModelAttribute("findTestResultForm") FindTestResultBackingForm form) {

    ModelAndView mv = new ModelAndView("testresults/testResultsForCollection");

    String collectionNumber = form.getCollectionNumber();
    CollectedSample c = null;
    c = collectedSampleRepository.findCollectedSampleByCollectionNumber(collectionNumber);
    if (c == null) {
      mv.addObject("collectionFound", false);
    }
    else {
      mv.addObject("collectionFound", true);
      mv.addObject("collectionId", c.getId());
    }
    return mv;
  }
  
  @RequestMapping(value = "/findAndAddTestBatchFormGenerator", method = RequestMethod.GET)
  public ModelAndView findAndAddTestBatchFormGenerator(HttpServletRequest request) {

    FindTestBatchBackingForm form = new FindTestBatchBackingForm();

    ModelAndView mv = new ModelAndView("testresults/findTestBatchForm");
    mv.addObject("findTestBatchForm", form);
    List<TestBatch> allTestBatch = testBatchRepository.getAllTestBatch();
    mv.addObject("allTestBatch", allTestBatch);
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }
  
  @RequestMapping("/addTestBatch")
  public ModelAndView addTestBatch(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="firstDIN") String paramsAsJsonFirstDIN,
      @RequestParam(value="lastDIN") String paramsAsJsonLastDIN,Model model) {
	  
	  FindTestBatchBackingForm form = new FindTestBatchBackingForm();

	  ModelAndView mv = new ModelAndView("testresults/findTestBatchForm");  
	  mv.addObject("findTestBatchForm", form);
	  testBatchRepository.saveTestBatch(paramsAsJsonFirstDIN, paramsAsJsonLastDIN,getNextTestBatchNumber());

	  mv.addObject("model", model);
	  return mv;
  }

	public String getNextTestBatchNumber() {
		return sequenceNumberRepository.getNextTestBatchNumber();
	}
	
	 @RequestMapping("/findTestBatch")
	  public ModelAndView findTestBatch(HttpServletRequest request,
	      @ModelAttribute("findTestBatchForm") FindTestBatchBackingForm form,
	      BindingResult result, Model model) {

	    List<CollectedSample> collections = Arrays.asList(new CollectedSample[0]);

	    ModelAndView modelAndView = new ModelAndView("testresults/testBatchTable");
	    Map<String, Object> m = model.asMap();
	    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
	    m.put("allCollectedSamples", getCollectionByTestBatchViewModels(collections));
	    m.put("refreshUrl", getUrl(request));
	    m.put("nextPageUrl", getNextPageUrl(request));
	    modelAndView.addObject("model", m);
	    return modelAndView;
	  }
	 
	 public static List<CollectedSampleViewModel> getCollectionByTestBatchViewModels(
		      List<CollectedSample> collections) {
		    if (collections == null)
		      return Arrays.asList(new CollectedSampleViewModel[0]);
		    List<CollectedSampleViewModel> collectionViewModels = new ArrayList<CollectedSampleViewModel>();
		    for (CollectedSample collection : collections) {
		      collectionViewModels.add(new CollectedSampleViewModel(collection));
		    }
		    return collectionViewModels;
		  }
	 
	 private String getNextPageUrl(HttpServletRequest request) {
		    String reqUrl = request.getRequestURL().toString().replaceFirst("findTestBatch.html", "findTestBatchPagination.html");
		    String queryString = request.getQueryString();   // d=789
		    if (queryString != null) {
		        reqUrl += "?"+queryString;
		    }
		    return reqUrl;
		  }
	 
	 @RequestMapping("/findTestBatchPagination")
	  public @ResponseBody Map<String, Object> findTestBatchPagination(HttpServletRequest request,
	      @ModelAttribute("findTestBatchForm") FindTestBatchBackingForm form,
	      BindingResult result, Model model) {
		

	    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);

	    String testBatchNumber = form.getTestBatchNumber();
	   
	    String createdAfterDate = form.getCreatedAfterDate();
	    String createdBeforeDate = form.getCreatedBeforeDate();

	    List<Object> results = testBatchRepository.findCollectedSamplesByTestBatch(
	    		testBatchNumber,
	    		createdAfterDate, createdBeforeDate, pagingParams);

	    @SuppressWarnings("unchecked")
	    List<CollectedSample> collectedSamples = (List<CollectedSample>) results.get(0);
	    Long totalRecords = (Long) results.get(1);
	    System.out.println("collectedSamples________________"+collectedSamples.size());
	    System.out.println("totalRecords________________"+totalRecords);
	    return generateDatatablesMapForFindTestBatch(collectedSamples, totalRecords);
	    
	    
	    
	    
	    
	  }
	 
	 private Map<String, Object> generateDatatablesMapForFindTestBatch(List<CollectedSample> collectedSamples, Long totalRecords) {
		    Map<String, Object> collectionsMap = new HashMap<String, Object>();

		    ArrayList<Object> collectionList = new ArrayList<Object>();
		    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectedSample");
		    for (CollectedSampleViewModel collection : getCollectionByTestBatchViewModels(collectedSamples)) {

		      List<Object> row = new ArrayList<Object>();
		      
		      row.add(collection.getId().toString());

		      for (String property : Arrays.asList("collectionNumber", "ttiStatus", "bloodTypingStatus", "bloodAbo", "bloodRh", "donor")) {
		        if (formFields.containsKey(property)) {
		          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
		          if (properties.get("hidden").equals(false)) {
		            String propertyValue = property;
		            try {
		              propertyValue = BeanUtils.getProperty(collection, property);
		            } catch (IllegalAccessException e) {
		              e.printStackTrace();
		            } catch (InvocationTargetException e) {
		              e.printStackTrace();
		            } catch (NoSuchMethodException e) {
		              e.printStackTrace();
		            }
		            row.add(propertyValue.toString());
		          }
		        }
		      }

		      collectionList.add(row);
		    }
		    collectionsMap.put("aaData", collectionList);
		    collectionsMap.put("iTotalRecords", totalRecords);
		    collectionsMap.put("iTotalDisplayRecords", totalRecords);
		    return collectionsMap;
		  }
}
