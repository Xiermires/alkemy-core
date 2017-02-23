/*******************************************************************************
 * Copyright (c) 2017, Xavier Miret Andres <xavier.mires@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any 
 * purpose with or without fee is hereby granted, provided that the above 
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES 
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALLIMPLIED WARRANTIES OF 
 * MERCHANTABILITY  AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR 
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES 
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN 
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF 
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *******************************************************************************/
package org.alkemy.visitor.impl;

import org.alkemy.parse.impl.AbstractAlkemyElement;
import org.alkemy.util.AlkemyUtils;
import org.alkemy.util.Assertions;
import org.alkemy.util.Node;
import org.alkemy.visitor.AlkemyElementVisitor;

/**
 * Traverses the directed rooted tree in pre-order, in order of appearance.
 */
public class AlkemyPreorderReader<R, P> extends AbstractTraverser<R, P>
{
    private boolean includeNullNodes;
    private boolean instantiateNodes;
    private boolean visitNodes;

    public AlkemyPreorderReader(boolean includeNullNodes, boolean instantiateNodes, boolean visitNodes)
    {
        this.includeNullNodes = includeNullNodes;
        this.instantiateNodes = instantiateNodes;
        this.visitNodes = visitNodes;
    }
    
    @Override
    protected void processBranch(AlkemyElementVisitor<?> aev, Node<? extends AbstractAlkemyElement<?>> e, Object parameter, Object... args)
    {
        if (e.hasChildren())
        {
            final Object node = AlkemyUtils.getNodeInstance(e, parameter, instantiateNodes);
            if (includeNullNodes || node != null)
            {
                if (visitNodes) e.data().accept(aev, parameter, args);
                e.children().forEach(c -> processBranch(aev, c, e.data().get(parameter)));
            }
        }
        else
        {
            e.data().accept(aev, parameter, args);
        }
    }
    
    /**
     * Fluent version.
     */
    public static class FluentAlkemyPreorderReader<R> extends AlkemyPreorderReader<R, R> implements FluentAlkemyNodeReader<R>
    {
        public FluentAlkemyPreorderReader(boolean includeNullNodes, boolean instantiateNodes, boolean visitNodes)
        {
            super(includeNullNodes, instantiateNodes, visitNodes);
        }

        @Override
        public R accept(AlkemyElementVisitor<?> aev, Node<? extends AbstractAlkemyElement<?>> root, R parameter)
        {
            Assertions.nonNull(root);

            root.children().forEach(c -> processBranch(aev, c, parameter));
            return parameter;
        }
    }
}