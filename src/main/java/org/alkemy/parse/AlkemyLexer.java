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
package org.alkemy.parse;

import org.alkemy.AbstractAlkemyElement;
import org.alkemy.ValueAccessor;
import org.alkemy.parse.impl.NodeConstructor;

public interface AlkemyLexer<T>
{
    /**
     * Returns if the descriptor defines an element with additional nested alkemy elements.
     */
    boolean isNode(T desc);

    /**
     * Returns if the descriptor defines a single element.
     */
    boolean isLeaf(T desc);

    /**
     * Creates an unbound {@link AbstractAlkemyElement} for this leaf.
     */
    AbstractAlkemyElement<?> createLeaf(T desc, ValueAccessor valueAccessor);

    /**
     * Creates an unbound {@link AbstractAlkemyElement} for this node.
     */
    AbstractAlkemyElement<?> createNode(T desc, NodeConstructor valueConstructor, ValueAccessor valueAccessor, Class<?> nodeType);
}
