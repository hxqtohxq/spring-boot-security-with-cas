package vn.com.vndirect.onlineuserservice.controller;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.vndirect.onlineuserservice.authen.CasServiceTicketValidator;
import vn.com.vndirect.onlineuserservice.model.WatchedListEntity;
import vn.com.vndirect.onlineuserservice.model.WatchedSymbol;
import vn.com.vndirect.onlineuserservice.service.CRUDService;
import vn.com.vndirect.service.response.ResponseStatus;
import vn.com.vndirect.service.response.ServiceResponse;

@RestController
@RequestMapping("/watchedList")
public class WatchedListController {

	@Autowired
	private CRUDService<WatchedSymbol, WatchedListEntity> watchedListService;

	@RequestMapping(value = "/get")
	public ServiceResponse<WatchedListEntity> get(WatchedSymbol request) throws TicketValidationException {
		String casServerUrlPrefix = "http://suat.vndirect.com.vn/login/";
		CasServiceTicketValidator validator = new CasServiceTicketValidator(casServerUrlPrefix);
		Assertion assertion = validator.validate(request.getCustomerId(), "http://dev.ipa.com.vn:8686/");
		
		System.out.println(assertion);
		
		ServiceResponse<WatchedListEntity> response = new ServiceResponse<WatchedListEntity>();
		response.setResponseStatus(ResponseStatus.SUCCESS);
		response.setResult(watchedListService.get(request));

		return response;
	}

	@RequestMapping(value = "/add")
	public ServiceResponse<WatchedListEntity> add(WatchedSymbol request) {
		ServiceResponse<WatchedListEntity> response = new ServiceResponse<WatchedListEntity>();
		response.setResponseStatus(ResponseStatus.SUCCESS);
		watchedListService.add(request);

		return response;
	}

	@RequestMapping(value = "/remove")
	public ServiceResponse<WatchedListEntity> remove(WatchedSymbol request) {
		ServiceResponse<WatchedListEntity> response = new ServiceResponse<WatchedListEntity>();
		response.setResponseStatus(ResponseStatus.SUCCESS);
		watchedListService.remove(request);

		return response;
	}
}
