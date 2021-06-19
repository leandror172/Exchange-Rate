package org.leandror.jaya.exchange_rate.apis.clients;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Map;

import org.leandror.jaya.exchange_rate.dtos.RatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import feign.QueryMap;

@FeignClient(name = "rates", url = "http://api.exchangeratesapi.io/v1")
public interface ExchangeRatesApiClient {

  @RequestMapping(method = GET, value = "/latest")
RatesResponse latest(@SpringQueryMap Map<String, String> params);
//RatesResponse latest(@RequestParam("access_key") String accessKey,
//                       @RequestParam String base, @RequestParam String symbols);

}
