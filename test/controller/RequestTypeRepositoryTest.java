package test;


import java.util.ArrayList;
import java.util.List;

import model.requesttype.RequestType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import repository.RequestTypeRepository;
import controller.AdminController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:**/v2v-servlet.xml" })
@WebAppConfiguration
public class RequestTypeRepositoryTest {
	
	@Autowired
	public RequestTypeRepository requestTypesRepository;
	
	@Test
	public void testSaveAllRequestTypes() {
		try{
			List<RequestType> allRequestTypes = new ArrayList<RequestType>();
			RequestType rt = new RequestType();
			rt.setId(5);
			rt.setRequestType("Urgent");
	    rt.setBulkTransfer(false);
	    allRequestTypes.add(rt);
	    requestTypesRepository.saveAllRequestTypes(allRequestTypes);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
