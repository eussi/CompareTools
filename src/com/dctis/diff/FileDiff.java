package com.dctis.diff;

import java.util.List;
/**
 * 文本对比实现，来自网络
 * @author wangxueming
 *
 */
public class FileDiff {
	
	public FileDiff(String[] fromStrs, String[] toStrs) {
		List<Difference> diffs = (new Diff(fromStrs, toStrs)).diff();

		for (Difference diff : diffs) {
			int delStart = diff.getDeletedStart();
			int delEnd = diff.getDeletedEnd();
			int addStart = diff.getAddedStart();
			int addEnd = diff.getAddedEnd();
			String from = toString(delStart, delEnd);
			String to = toString(addStart, addEnd);
			String type = delEnd != Difference.NONE
					&& addEnd != Difference.NONE ? "c"
					: (delEnd == Difference.NONE ? "a" : "d");

			System.out.println(from + type + to);

			if (delEnd != Difference.NONE) {
				printLines(delStart, delEnd, "<", fromStrs);
				if (addEnd != Difference.NONE) {
					System.out.println("---");
				}
			}
			if (addEnd != Difference.NONE) {
				printLines(addStart, addEnd, ">", toStrs);
			}
		}
	}

	private void printLines(int start, int end, String ind, String[] lines) {
		for (int lnum = start; lnum <= end; ++lnum) {
			System.out.println(ind + " " + lines[lnum]);
		}
	}

	private String toString(int start, int end) {
		// adjusted, because file lines are one-indexed, not zero.
		StringBuffer buf = new StringBuffer();
		// match the line numbering from diff(1):
		buf.append(end == Difference.NONE ? start : (1 + start));
		if (end != Difference.NONE && start != end) {
			buf.append(",").append(1 + end);
		}
		return buf.toString();
	}

}
