package org.allenai.blacklab.search.grouping;

import org.allenai.blacklab.forwardindex.Terms;
import org.allenai.blacklab.index.complex.ComplexFieldUtil;
import org.allenai.blacklab.search.Hits;
import org.allenai.util.ArrayUtil;

public class HitPropValueContextWords extends HitPropValueContext {
	int[] valueTokenId;

	int[] valueSortOrder;

	boolean sensitive;

	public HitPropValueContextWords(Hits hits, String propName, int[] value, boolean sensitive) {
		super(hits, propName);
		this.valueTokenId = value;
		this.sensitive = sensitive;
		valueSortOrder = new int[value.length];
		terms.toSortOrder(value, valueSortOrder, sensitive);
	}

	@Override
	public int compareTo(Object o) {
		return ArrayUtil.compareArrays(valueSortOrder, ((HitPropValueContextWords) o).valueSortOrder);
	}

	@Override
	public int hashCode() {
		int result = 0;
		for (int v: valueSortOrder) {
			result ^= ((Integer) v).hashCode();
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int v: valueTokenId) {
			String word = v < 0 ? "" : terms.get(v);
			if (word.length() > 0) {
				if (b.length() > 0)
					b.append(" ");
				b.append(word);
			}
		}
		return b.toString();
	}

	public static HitPropValue deserialize(Hits hits, String info) {
		String[] parts = PropValSerializeUtil.splitParts(info);
		String fieldName = hits.getConcordanceFieldName();
		String propName = parts[0];
		boolean sensitive = parts[1].equalsIgnoreCase("s");
		int[] ids = new int[parts.length - 2];
		Terms termsObj = hits.getSearcher().getForwardIndex(ComplexFieldUtil.propertyField(fieldName, propName)).getTerms();
		for (int i = 2; i < parts.length; i++) {
			int tokenId;
			if (parts[i].length() == 0)
				tokenId = -1; // no token
			else
				tokenId = termsObj.indexOf(parts[i]);
			ids[i - 2] = tokenId;
		}
		return new HitPropValueContextWords(hits, propName, ids, sensitive);
	}

	@Override
	public String serialize() {
		String[] parts = new String[valueTokenId.length + 3];
		parts[0] = "cws";
		parts[1] = propName;
		parts[2] = (sensitive ? "s" : "i");
		for (int i = 0; i < valueTokenId.length; i++) {
			int v = valueTokenId[i];
			if (v < 0)
				parts[i + 3] = ""; // no token
			else
				parts[i + 3] = terms.get(v);
		}
		return PropValSerializeUtil.combineParts(parts);
	}
}
