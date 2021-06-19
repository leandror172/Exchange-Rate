package org.leandror.jaya.exchange_rate.apis.clients;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.leandror.jaya.exchange_rate.dtos.RatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "rates", url = "http://api.exchangeratesapi.io/v1")
public interface ExchangeRatesApiClient {
  
  @RequestMapping(method = GET, value = "/latest")
  RatesResponse latest(@PathVariable("access_key") String accessKey, @PathVariable String symbols);

}
