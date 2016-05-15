package me.l.puppy.dict.impl.stardict;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import me.l.puppy.dict.core.SearchStrategy;

public class IdxSearcherKeyTree extends IdxSearcher {
    TreeNode root;

    public IdxSearcherKeyTree(IdxReader reader) {
        super(reader);
        root = new TreeNode('#');
        IdxInfo info;
        while ((info = reader.next()) != null) {
            insert(info);
        }
    }

    /**
     * Insert idxInfo into the tree
     *
     * @param idx
     */
    private void insert(IdxInfo idx) {
        TreeNode node = root;
        String word = "#" + idx.word;
        int startIdx = 0;
        TreeNode parent = root;
        while (node != null) {
            char ch = word.charAt(startIdx);
            if (node.ch == ch) {
                startIdx++;
                parent = node;
                node = node.child;
            } else {
                node = node.findSibling(ch);
                if (node == null) {
                    break;
                } else {
                    parent = node;
                    startIdx++;
                    node = node.child;
                }
            }
        }
        String sub = word.substring(startIdx);
        for (char ch : sub.toCharArray()) {
            node = new TreeNode(ch, null, null);
            if (parent.child != null)
                parent.child.addSibling(node);
            else
                parent.child = node;
            parent = node;
        }
        node.info = idx;
    }

    public IdxInfo search(String string) {
        string = "#" + string;
        int startIdxOfWord = 0;
        int len = string.length();
        TreeNode node = root;
        while (true) {
            char ch = string.charAt(startIdxOfWord);
            if (node.ch != ch) {
                node = node.findSibling(ch);
                if (node == null)
                    return null;
            }
            if (++startIdxOfWord == len)
                break;
            node = node.child;
        }
        return node.info;
    }

    private List<IdxInfo> searchStartsWith(String string, int maxResults) {
        List<IdxInfo> idxInfos = new ArrayList<IdxInfo>();
        TreeNode node = root;
        int startIdx = 0;
        string = "#" + string;
        int len = string.length();
        char ch;
        while (true) {
            ch = string.charAt(startIdx);
            if (node.ch != ch) {
                node = node.findSibling(ch);
                if (node == null)
                    break;
            }
            node = node.child;
            if (++startIdx == len) {
                break;
            }
        }
        if (node != null) {
            //there are words which are start with string located in the sub-tree of node
            Queue<TreeNode> nodes = new ArrayDeque<TreeNode>();
            nodes.add(node);
            node = null;
            while ((!nodes.isEmpty() || node != null) && maxResults > 0) {
                if (node == null)
                    node = nodes.poll();
                if (node.info != null) {
                    idxInfos.add(node.info);
                    maxResults--;
                }
                for (TreeNode n : node.getSiblings()) {
                    nodes.add(n);
                }
                node = node.child;
            }
        }
        return idxInfos;
    }

    private List<IdxInfo> searchWild(TreeNode node, String word, int maxResult, int startIdx) {
        List<IdxInfo> idxInfos = new ArrayList<IdxInfo>();
        if(node==null)
            return idxInfos;
        int len = word.length();
        char ch;
        if(startIdx<len) {
            while (true) {
                ch = word.charAt(startIdx);
                if (node.ch != ch && ch != '?') {
                    node = node.findSibling(ch);
                    if (node == null)
                        return idxInfos;
                }
                if (ch == '?') {
                    List<IdxInfo> tmp = searchWild(node.child, word, maxResult, startIdx + 1);
                    for (int i = 0; i < tmp.size() && i < maxResult; i++) {
                        idxInfos.add(tmp.get(i));
                    }
                    for (TreeNode n : node.getSiblings()) {
                        if (maxResult > idxInfos.size()) {
                            tmp = searchWild(n.child, word, maxResult - idxInfos.size(), startIdx + 1);
                            idxInfos.addAll(tmp);
                        }
                    }
                }
                node = node.child;
                if (++startIdx == len) {
                    break;
                }

            }
        }
        if (node != null) {
            Queue<TreeNode> nodes = new ArrayDeque<TreeNode>();
            nodes.add(node);
            node = null;
            while ((!nodes.isEmpty() || node != null) && maxResult > 0) {
                if (node == null)
                    node = nodes.poll();
                if (node.info != null) {
                    idxInfos.add(node.info);
                    maxResult--;
                }
                for (TreeNode n : node.getSiblings()) {
                    nodes.add(n);
                }
                node = node.child;
            }
        }
        return idxInfos;
    }

    private List<IdxInfo> searchWild(String word, int maxResults) {
      return searchWild(root,"#"+word,maxResults,0);
    }

    @Override
    public List<IdxInfo> search(String string, SearchStrategy strategy, int maxResults) {
        if (strategy == SearchStrategy.StartsWith)
            return searchStartsWith(string, maxResults);
        else if (strategy == SearchStrategy.WilwidCard) {
            return searchWild(string, maxResults);
        }
        return new ArrayList<IdxInfo>();
    }
}

class TreeNode {
    public char ch;
    public IdxInfo info;
    public TreeNode child;
    private List<TreeNode> siblings;

    public TreeNode(char ch, IdxInfo info, TreeNode child) {
        this.ch = ch;
        this.info = info;
        this.child = child;
        siblings = new ArrayList<TreeNode>();
    }

    /**
     * Sorted adding
     *
     * @param node
     */
    public void addSibling(TreeNode node) {
        siblings.add(node);
        if (siblings.size() == 1)
            return;
        int size = siblings.size();
        int i = size - 2;
        while (i >= 0) {
            TreeNode tmp = siblings.get(i);
            if (node.ch < tmp.ch) {
                siblings.set(i + 1, tmp);
                i--;
            } else {
                break;
            }
        }
        siblings.set(i + 1, node);
    }

    public TreeNode[] getSiblings() {
        TreeNode[] ret = new TreeNode[siblings.size()];
        int idx = 0;
        for (TreeNode n : siblings) {
            ret[idx++] = n;
        }
        return ret;
    }

    /**
     * Find is there a node which it's ch == ch
     *
     * @param ch
     * @return null if there is not one or the node if find
     */
    public TreeNode findSibling(char ch) {
        int l = 0, h = siblings.size() - 1, m;
        while (l <= h) {
            m = (h + l) / 2;
            TreeNode middle = siblings.get(m);
            int cmp = ch - middle.ch;
            if (cmp == 0) {
                return middle;
            } else if (cmp > 0) {
                l = m + 1;
            } else {
                h = m - 1;
            }
        }
        return null;
    }

    public TreeNode(char ch) {
        this(ch, null, null);
    }

    @Override
    public String toString() {
        return ch + "";
    }
}
