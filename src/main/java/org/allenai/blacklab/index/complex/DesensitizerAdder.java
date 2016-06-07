package org.allenai.blacklab.index.complex;

import org.allenai.blacklab.filter.DesensitizeFilter;

import org.apache.lucene.analysis.TokenStream;

public class DesensitizerAdder implements TokenFilterAdder {

	/** Should we add a LowerCaseFilter? */
	private boolean lowerCase;

	/** Should we add a RemoveAllAccentsFilter? */
	private boolean removeAccents;

	public DesensitizerAdder(boolean lowerCase, boolean removeAccents) {
		this.lowerCase = lowerCase;
		this.removeAccents = removeAccents;
	}

	@Override
	public TokenStream addFilters(TokenStream input) {
		if (!lowerCase && !removeAccents)
			return input;
		return new DesensitizeFilter(input, lowerCase, removeAccents);
	}

}
