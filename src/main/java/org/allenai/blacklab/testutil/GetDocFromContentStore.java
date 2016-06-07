package org.allenai.blacklab.testutil;

import java.io.File;

import org.allenai.blacklab.externalstorage.ContentStore;
import org.allenai.blacklab.externalstorage.ContentStoreDirZip;

/**
 * Retrieves and displays a document from a BlackLab content store.
 */
public class GetDocFromContentStore {

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: GetDocFromContentStore <contentStoreDir> <docId>");
			return;
		}

		File csDir = new File(args[0]);
		int id = Integer.parseInt(args[1]);

		ContentStore cs = new ContentStoreDirZip(csDir);
		String content = cs.retrieve(id);
		System.out.println(content);
	}
}
