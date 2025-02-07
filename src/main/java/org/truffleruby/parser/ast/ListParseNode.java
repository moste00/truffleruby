/*
 ***** BEGIN LICENSE BLOCK *****
 * Version: EPL 2.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Eclipse Public
 * License Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/epl-v20.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2004-2005 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.truffleruby.parser.ast;

import java.util.Arrays;
import java.util.List;

import org.truffleruby.language.SourceIndexLength;
import org.truffleruby.parser.ast.visitor.NodeVisitor;

/** All Nodes which have a list representation inherit this. This is also used as generic container for additional
 * information that is not directly evaluated. In particular, f_arg production rule uses this to capture arg information
 * for the editor projects who want position info saved. */
public abstract class ListParseNode extends ParseNode {
    private static final ParseNode[] EMPTY = ParseNode.EMPTY_ARRAY;
    private static final int INITIAL_SIZE = 4;
    private ParseNode[] list;
    private int size = 0;

    /** Create a new ListParseNode.
     *
     * @param position where list is
     * @param firstNode first element of the list */
    @SuppressWarnings("this-escape")
    protected ListParseNode(SourceIndexLength position, ParseNode firstNode) {
        super(position);

        list = new ParseNode[INITIAL_SIZE];
        addInternal(firstNode);
    }

    protected ListParseNode(SourceIndexLength position) {
        super(position);

        list = EMPTY;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.LISTNODE;
    }

    private void growList(int mustBeDelta) {
        int newSize = list.length * 2;
        // Fairly arbitrary to scale 1.5 here but this means we are adding a lot so I think
        // we can taper the multiplier
        if (size + mustBeDelta >= newSize) {
            newSize = (int) ((size + mustBeDelta) * 1.5);
        }

        ParseNode[] newList = new ParseNode[newSize];
        System.arraycopy(list, 0, newList, 0, size);
        list = newList;
    }

    private void addInternal(ParseNode node) {
        if (size >= list.length) {
            growList(1);
        }

        list[size++] = node;
        extendPosition(node);
    }

    protected void addAllInternal(ListParseNode other) {
        if (size + other.size() >= list.length) {
            growList(other.size);
        }

        System.arraycopy(other.list, 0, list, size, other.size);
        size += other.size;
        extendPosition(other);
    }

    /** Add a node to this list
     *
     * @param node the node to add
     * @return this instance for method chaining */
    public ListParseNode add(ParseNode node) {
        // Ruby Grammar productions return plenty of nulls.
        if (node == null || node == NilImplicitParseNode.NIL) {
            addInternal(NilImplicitParseNode.NIL);
            return this;
        }

        addInternal(node);
        return this;
    }

    public int size() {
        return size;
    }


    /** Add all elements in other list to this list node.
     *
     * @param other list which has elements
     * @return this instance for method chaining */
    public ListParseNode addAll(ListParseNode other) {
        if (other != null && other.size() > 0) {
            addAllInternal(other);
        }
        return this;
    }

    public ListParseNode addAll(ParseNode[] other, int index, int length) {
        for (int i = 0; i < length; i++) {
            addInternal(other[index + i]);
        }

        return this;
    }

    public ParseNode getLast() {
        return size == 0 ? null : list[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public ParseNode[] children() {
        ParseNode[] properList = new ParseNode[size];
        System.arraycopy(list, 0, properList, 0, size);
        return properList;
    }

    @Override
    @Deprecated
    public List<ParseNode> childNodes() {
        return Arrays.asList(children());
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitListNode(this);
    }

    public ParseNode get(int idx) {
        return list[idx];
    }
}
