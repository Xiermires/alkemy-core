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

import java.util.List;

import org.alkemy.parse.impl.AbstractAlkemyElement;
import org.alkemy.parse.impl.AbstractAlkemyElement.AlkemyElement;
import org.alkemy.visitor.AlkemyElementVisitor;

public class AlkemyVisitorController<P> implements AlkemyElementVisitor<P, AlkemyElement>
{
    private final List<AlkemyElementVisitor<P, ? extends AbstractAlkemyElement<?>>> aevs;

    AlkemyVisitorController(List<AlkemyElementVisitor<P, ? extends AbstractAlkemyElement<?>>> aevs)
    {
        this.aevs = aevs;
    }

    @Override
    public void visit(AlkemyElement e, Object parent)
    {
        _visit(e, parent);
    }

    @SuppressWarnings("unchecked") // safe (AlkemyElementVisitor interface)
    private <E extends AbstractAlkemyElement<E>> void _visit(AlkemyElement e, Object parent)
    {
        aevs.stream().filter(p -> p.accepts(e.alkemyType()))
                .forEach(aev -> aev.map(e).accept((AlkemyElementVisitor<P, E>) aev, parent));
    }

    @Override
    public void visit(AlkemyElement e, Object parent, P parameter)
    {
        _visit(e, parent, parameter);
    }
    
    @SuppressWarnings("unchecked") // safe (AlkemyElementVisitor interface)
    private <E extends AbstractAlkemyElement<E>> void _visit(AlkemyElement e, Object parent, P parameter)
    {
        aevs.stream().filter(p -> p.accepts(e.alkemyType()))
                .forEach(aev -> aev.map(e).accept((AlkemyElementVisitor<P, E>) aev, parent, parameter));
    }

    @Override
    public AlkemyElement map(AlkemyElement e)
    {
        return e;
    }

    @Override
    public boolean accepts(Class<?> type)
    {
        return true;
    }
}
