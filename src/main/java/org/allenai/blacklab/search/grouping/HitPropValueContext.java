package org.allenai.blacklab.search.grouping;

import org.allenai.blacklab.forwardindex.Terms;
import org.allenai.blacklab.index.complex.ComplexFieldUtil;
import org.allenai.blacklab.search.Hits;

public abstract class HitPropValueContext extends HitPropValue {

	protected String fieldName;

	protected Terms terms;

	protected String propName;

	public HitPropValueContext(Hits hits, String propName) {
		this.fieldName = hits.getConcordanceFieldName();
		this.propName = propName;
		this.terms = hits.getSearcher().getForwardIndex(ComplexFieldUtil.propertyField(fieldName, propName)).getTerms();
	}
}
