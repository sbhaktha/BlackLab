package org.allenai.blacklab.search.grouping;

import org.allenai.blacklab.forwardindex.Terms;
import org.allenai.blacklab.index.complex.ComplexFieldUtil;
import org.allenai.blacklab.search.Hits;

public class HitPropValueContextWord extends HitPropValueContext {
	int valueTokenId;

	int valueSortOrder;

	boolean sensitive;

	public HitPropValueContextWord(Hits hits, String propName, int value, boolean sensitive) {
		super(hits, propName);
		this.valueTokenId = value;
		this.sensitive = sensitive;
		valueSortOrder = value < 0 ? value : terms.idToSortPosition(value, sensitive);
	}

	@Override
	public int compareTo(Object o) {
		int a = valueSortOrder, b = ((HitPropValueContextWord)o).valueSortOrder;
		return a == b ? 0 : (a < b ? -1 : 1);
	}

	@Override
	public int hashCode() {
		return ((Integer)valueSortOrder).hashCode();
	}

	@Override
	public String toString() {
		return valueTokenId < 0 ? "" : terms.get(valueTokenId);
	}

	public static HitPropValue deserialize(Hits hits, String info) {
		String[] parts = PropValSerializeUtil.splitParts(info);
		String fieldName = hits.getConcordanceFieldName();
		String propName = parts[0];
		boolean sensitive = parts[1].equalsIgnoreCase("s");
		Terms termsObj = hits.getSearcher().getForwardIndex(ComplexFieldUtil.propertyField(fieldName, propName)).getTerms();
		int id;
		if (parts[2].length() == 0)
			id = -1; // no token
		else
			id = termsObj.indexOf(parts[2]);
		return new HitPropValueContextWord(hits, propName, id, sensitive);
	}

	@Override
	public String serialize() {
		String token;
		if (valueTokenId < 0)
			token = ""; // no token
		else
			token = terms.get(valueTokenId);
		return PropValSerializeUtil.combineParts(
			"cwo", propName,
			(sensitive ? "s" : "i"),
			token);
	}
}
