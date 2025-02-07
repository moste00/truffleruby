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
 * Copyright (C) 2001-2002 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2001-2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
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
import org.truffleruby.parser.ast.visitor.NodeVisitor;

/** Represents the contents of a rescue to be evaluated */
public final class RescueBodyParseNode extends ParseNode {
    private final ParseNode exceptionNodes;
    private final ParseNode bodyNode;
    private final RescueBodyParseNode optRescueNode;

    public RescueBodyParseNode(
            SourceIndexLength position,
            ParseNode exceptionNodes,
            ParseNode bodyNode,
            RescueBodyParseNode optRescueNode) {
        super(position);

        assert bodyNode != null : "bodyNode is not null";

        this.exceptionNodes = exceptionNodes;
        this.bodyNode = bodyNode;
        this.optRescueNode = optRescueNode;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RESCUEBODYNODE;
    }

    /** Accept for the visitor pattern.
     * 
     * @param iVisitor the visitor **/
    @Override
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitRescueBodyNode(this);
    }

    /** Gets the bodyNode.
     * 
     * @return Returns a ParseNode */
    public ParseNode getBodyNode() {
        return bodyNode;
    }

    /** Get the next rescue node (if any). */
    public RescueBodyParseNode getOptRescueNode() {
        return optRescueNode;
    }

    /** Gets the exceptionNodes.
     * 
     * @return Returns a ParseNode */
    public ParseNode getExceptionNodes() {
        return exceptionNodes;
    }

    @Override
    public List<ParseNode> childNodes() {
        if (optRescueNode != null) {
            return ParseNode.createList(exceptionNodes, bodyNode, optRescueNode);
        }

        return ParseNode.createList(exceptionNodes, bodyNode);
    }
}
