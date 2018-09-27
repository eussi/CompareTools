package com.dcits.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

public class Diff {
	private List<String> a;

	private List<String> b;

	private List<Difference> diffs = new ArrayList<Difference>();

	private Difference pending;

	private Comparator<String> comparator;

	private TreeMap<Integer, Integer> thresh;

	public Diff(String[] a, String[] b, Comparator<String> comp) {
		this(Arrays.asList(a), Arrays.asList(b), comp);
	}

	public Diff(String[] a, String[] b) {
		this(a, b, null);
	}

	public Diff(List<String> a, List<String> b, Comparator<String> comp) {
		this.a = a;
		this.b = b;
		this.comparator = comp;
		this.thresh = null;
	}

	public Diff(List<String> a, List<String> b) {
		this(a, b, null);
	}

	public List<Difference> diff() {
		traverseSequences();

		// add the last difference, if pending:
		if (pending != null) {
			diffs.add(pending);
		}

		return diffs;
	}

	private void traverseSequences() {
		Integer[] matches = getLongestCommonSubsequences();

		int lastA = a.size() - 1;
		int lastB = b.size() - 1;
		int bi = 0;
		int ai;

		int lastMatch = matches.length - 1;

		for (ai = 0; ai <= lastMatch; ++ai) {
			Integer bLine = matches[ai];

			if (bLine == null) {
				onANotB(ai, bi);
			} else {
				while (bi < bLine) {
					onBNotA(ai, bi++);
				}

				onMatch(ai, bi++);
			}
		}

		boolean calledFinishA = false;
		boolean calledFinishB = false;

		while (ai <= lastA || bi <= lastB) {

			// last A?
			if (ai == lastA + 1 && bi <= lastB) {
				if (!calledFinishA && callFinishedA()) {
					finishedA(lastA);
					calledFinishA = true;
				} else {
					while (bi <= lastB) {
						onBNotA(ai, bi++);
					}
				}
			}

			// last B?
			if (bi == lastB + 1 && ai <= lastA) {
				if (!calledFinishB && callFinishedB()) {
					finishedB(lastB);
					calledFinishB = true;
				} else {
					while (ai <= lastA) {
						onANotB(ai++, bi);
					}
				}
			}

			if (ai <= lastA) {
				onANotB(ai++, bi);
			}

			if (bi <= lastB) {
				onBNotA(ai, bi++);
			}
		}
	}

	private boolean callFinishedA() {
		return false;
	}

	private boolean callFinishedB() {
		return false;
	}

	private void finishedA(int lastA) {
	}

	private void finishedB(int lastB) {
	}

	private void onANotB(int ai, int bi) {
		if (pending == null) {
			pending = new Difference(ai, ai, bi, -1);
		} else {
			pending.setDeleted(ai);
		}
	}

	private void onBNotA(int ai, int bi) {
		if (pending == null) {
			pending = new Difference(ai, -1, bi, bi);
		} else {
			pending.setAdded(bi);
		}
	}

	private void onMatch(int ai, int bi) {
		if (pending == null) {
			// no current pending
		} else {
			diffs.add(pending);
			pending = null;
		}
	}

	private boolean equals(String x, String y) {
		return comparator == null ? x.equals(y) : comparator.compare(x, y) == 0;
	}

	public Integer[] getLongestCommonSubsequences()
    {
        int aStart = 0;
        int aEnd = a.size() - 1;

        int bStart = 0;
        int bEnd = b.size() - 1;

        TreeMap<Integer, Integer> matches = new TreeMap<Integer, Integer>();

        while (aStart <= aEnd && bStart <= bEnd && equals(a.get(aStart), b.get(bStart))) {
            matches.put(aStart++, bStart++);
        }

        while (aStart <= aEnd && bStart <= bEnd && equals(a.get(aEnd), b.get(bEnd))) {
            matches.put(aEnd--, bEnd--);
        }

        Map<String, List<Integer>> bMatches = null;
        if (comparator == null) {
            if (a.size() > 0 && a.get(0) instanceof Comparable) {
                // this uses the Comparable interface
                bMatches = new TreeMap<String, List<Integer>>();
            }
            else {
                // this just uses hashCode()
                bMatches = new HashMap<String, List<Integer>>();
            }
        }
        else {
            // we don't really want them sorted, but this is the only Map
            // implementation (as of JDK 1.4) that takes a comparator.
            bMatches = new TreeMap<String, List<Integer>>(comparator);
        }

        for (int bi = bStart; bi <= bEnd; ++bi) {
            String element = b.get(bi);
            String key = element;
            List<Integer> positions = bMatches.get(key);
            
            if (positions == null) {
                positions = new ArrayList<Integer>();
                bMatches.put(key, positions);
            }
            
            positions.add(bi);
        }

        thresh = new TreeMap<Integer, Integer>();
        Map<Integer, Object[]> links = new HashMap<Integer, Object[]>();

        for (int i = aStart; i <= aEnd; ++i) {
            String aElement  = a.get(i);
            List<Integer> positions = bMatches.get(aElement);

            if (positions != null) {
                Integer  k   = 0;
                ListIterator<Integer> pit = positions.listIterator(positions.size());
                while (pit.hasPrevious()) {
                    Integer j = pit.previous();

                    k = insert(j, k);

                    if (k == null) {
                        // nothing
                    }
                    else {
                        Object value = k > 0 ? links.get(k - 1) : null;
                        links.put(k, new Object[] { value, i, j });
                    }   
                }
            }
        }

        if (thresh.size() > 0) {
            Integer  ti   = thresh.lastKey();
            Object[] link = (Object[])links.get(ti);
            while (link != null) {
                Integer x = (Integer)link[1];
                Integer y = (Integer)link[2];
                matches.put(x, y);
                link = (Object[])link[0];
            }
        }

        int       size = matches.size() == 0 ? 0 : 1 + matches.lastKey();
        Integer[] ary  = new Integer[size];
        for (Integer idx : matches.keySet()) {
            Integer val = matches.get(idx);
            ary[idx] = val;
        }
        return ary;
    }

	private static boolean isNonzero(Integer i) {
		return i != null && i != 0;
	}

	private boolean isGreaterThan(Integer index, Integer val) {
		Integer lhs = thresh.get(index);
		return lhs != null && val != null && lhs.compareTo(val) > 0;
	}

	private boolean isLessThan(Integer index, Integer val) {
		Integer lhs = thresh.get(index);
		return lhs != null && (val == null || lhs.compareTo(val) < 0);
	}

	private Integer getLastValue() {
		return thresh.get(thresh.lastKey());
	}

	private void append(Integer value) {
		Integer addIdx = null;
		if (thresh.size() == 0) {
			addIdx = 0;
		} else {
			Integer lastKey = thresh.lastKey();
			addIdx = lastKey + 1;
		}
		thresh.put(addIdx, value);
	}

	private Integer insert(Integer j, Integer k) {
		if (isNonzero(k) && isGreaterThan(k, j) && isLessThan(k - 1, j)) {
			thresh.put(k, j);
		} else {
			int high = -1;

			if (isNonzero(k)) {
				high = k;
			} else if (thresh.size() > 0) {
				high = thresh.lastKey();
			}
			// off the end?
			if (high == -1 || j.compareTo(getLastValue()) > 0) {
				append(j);
				k = high + 1;
			} else {
				// binary search for insertion point:
				int low = 0;

				while (low <= high) {
					int index = (high + low) / 2;
					Integer val = thresh.get(index);
					int cmp = j.compareTo(val);

					if (cmp == 0) {
						return null;
					} else if (cmp > 0) {
						low = index + 1;
					} else {
						high = index - 1;
					}
				}
				thresh.put(low, j);
				k = low;
			}
		}
		return k;
	}
}
