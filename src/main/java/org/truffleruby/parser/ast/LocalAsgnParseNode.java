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
 * Copyright (C) 2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2002 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
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

import java.util.List;

import org.truffleruby.language.SourceIndexLength;
import org.truffleruby.parser.ast.types.INameNode;
import org.truffleruby.parser.ast.visitor.NodeVisitor;

/** An assignment to a local variable. */
public final class LocalAsgnParseNode extends AssignableParseNode implements INameNode, IScopedNode {
    // The name of the variable
    private String name;

    // A scoped location of this variable (high 16 bits is how many scopes down and low 16 bits
    // is what index in the right scope to set the value.
    private final int location;

    public LocalAsgnParseNode(SourceIndexLength position, String name, int location, ParseNode valueNode) {
        super(position, valueNode);
        this.name = name;
        this.location = location;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.LOCALASGNNODE;
    }

    /** Accept for the visitor pattern.
     * 
     * @param iVisitor the visitor **/
    @Override
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitLocalAsgnNode(this);
    }

    /** Name of the local assignment. **/
    public String getName() {
        return name;
    }

    /** Change the name of this local assignment (for refactoring)
     * 
     * @param name */
    public void setName(String name) {
        this.name = name;
    }

    /** How many scopes should we burrow down to until we need to set the block variable value.
     *
     * @return 0 for current scope, 1 for one down, ... */
    public int getDepth() {
        return location >> 16;
    }

    /** Gets the index within the scope construct that actually holds the eval'd value of this local variable
     *
     * @return Returns an int offset into storage structure */
    public int getIndex() {
        return location & 0xffff;
    }

    @Override
    public List<ParseNode> childNodes() {
        return createList(getValueNode());
    }

    @Override
    public boolean needsDefinitionCheck() {
        return false;
    }
}
