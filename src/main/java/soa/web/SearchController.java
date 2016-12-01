package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class SearchController {

	@Autowired
	  private ProducerTemplate producerTemplate;

	@RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String query) {
        
        //Map with the headers of the CamelRequest
        Map headers = new HashMap();

        //String query "kewword max:n" -> Now separate the keyword (keyword) of count (max:n)
        if (query.contains(" max:")) {

             /*
             * SPLIT, the query contains " max:n" then:
             * 
             * Headers: {
             *    CamelTwitterKeywords: querySplit[0],
             *    CamelTwitterCount: querySplit[1]
             * }
             */
            String[] queryParts = query.split(" max:");

            //Put the specifics headers into Map of CamelRequest
            headers.put("CamelTwitterKeywords", queryParts[0]);
            headers.put("CamelTwitterCount", queryParts[1]);
        } else {
            
            /*
             * NOT SPLIT, the query not contains " max:n" then:
             * 
             * Headers: {
             *    CamelTwitterKeywords: query
             *
             * }
             */
            headers.put("CamelTwitterKeywords", query);       
        }

        return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);
    }
}