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
package org.alkemy.util;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A list w/o any range check / concurrent mod. constraints.
 */
public class NoRangeCheckList<E> extends AbstractList<E>
{
    private final int size;
    private final E[] buffer;

    @SuppressWarnings("unchecked")
    public NoRangeCheckList(List<E> list)
    {
        size = list.size();
        buffer = (E[]) list.toArray();
    }

    @Override
    public E get(int index)
    {
        return buffer[index];
    }

    @Override
    public E set(int index, E element)
    {
        final E old = buffer[index];
        buffer[index] = element;
        return old;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public void forEach(Consumer<? super E> action)
    {
        for (int i = 0; i < size; i++)
        {
            action.accept(buffer[i]);
        }
    }
}