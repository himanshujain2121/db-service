package com.techprimers.stock.dbservice.resource;

import java.util.List;
import java.util.stream.Collectors;

import com.techprimers.stock.dbservice.model.Quote;
import com.techprimers.stock.dbservice.model.Quotes;
import com.techprimers.stock.dbservice.repository.QuotesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/db")
public class DbServiceResource {

	private QuotesRepository quotesRepository;

	@Autowired
	public DbServiceResource(QuotesRepository quotesRepository) {
		this.quotesRepository = quotesRepository;
	}

	@GetMapping("/{username}")
	public List<String> getQuotes(@PathVariable("username") String username) {
		return getQuotesByUsername(username);
	}

	@PostMapping("/add")
	public List<String> add(@RequestBody final Quotes quotes) {
		quotes.getQuotes().stream().map(quote -> new Quote(quotes.getUsername(), quote))
				.forEach(quote -> quotesRepository.save(quote));
		return getQuotesByUsername(quotes.getUsername());

	}
	
	@PostMapping("/delete/{username}")
	public List<String> deleteUserName(@PathVariable("username") final String username) {
		List<Quote> quote = quotesRepository.findByUsername(username);
		quotesRepository.deleteByUsername(username);
		return getQuotesByUsername(username);
	}

	public List<String> getQuotesByUsername(String username) {
		return quotesRepository.findByUsername(username).stream().map(quote -> quote.getQuote())
				.collect(Collectors.toList());
	}
}
