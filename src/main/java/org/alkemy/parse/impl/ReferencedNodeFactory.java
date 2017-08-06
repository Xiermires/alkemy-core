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
package org.alkemy.parse.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import org.alkemy.exception.AccessException;
import org.alkemy.exception.AlkemyException;
import org.alkemy.parse.AutoCastValueAccessor;
import org.alkemy.parse.ConstructorFunction;
import org.alkemy.parse.NodeFactory;
import org.alkemy.util.Assertions;

public class ReferencedNodeFactory implements NodeFactory
{
    private final Class<?> type;
    private final Class<?> componentType;
    private final Supplier<?> noargsCtor;
    private final Supplier<?> noargsComponentCtor;
    private final ConstructorFunction staticFactory;
    private AutoCastValueAccessor valueAccessor;
    private final boolean collection;

    ReferencedNodeFactory(Class<?> type, Class<?> componentType, Supplier<?> noargsCtor,
            Supplier<?> noargsComponentCtor, ConstructorFunction staticFactory, AutoCastValueAccessor valueAccessor)
    {
        Assertions.noneNull(type, noargsCtor, staticFactory);

        this.type = type;
        this.componentType = componentType;
        this.noargsCtor = noargsCtor;
        this.noargsComponentCtor = noargsComponentCtor;
        this.staticFactory = staticFactory;
        this.valueAccessor = valueAccessor;
        this.collection = Collection.class.isAssignableFrom(type);
    }

    @Override
    public Class<?> type() throws AlkemyException
    {
        return type;
    }

    @Override
    public Object newInstance(Object... args) throws AlkemyException
    {
        try
        {
            if (args.length == 0)
            {
                return noargsCtor.get();
            }
            else
            {
                return staticFactory.newInstance(args);
            }
        }
        catch (Throwable e)
        {
            throw new AccessException("Provided arguments '%s' do not match the ctor expected arguments of type '%s'.", e, Arrays
                    .asList(args), type);
        }
    }

    @Override
    public Class<?> componentType() throws AlkemyException
    {
        return componentType;
    }

    @Override
    public Object newComponentInstance(Object... args) throws AlkemyException
    {
        try
        {
            if (args.length == 0)
            {
                return noargsComponentCtor.get();
            }
            else
            {
                return staticFactory.newInstance(args);
            }
        }
        catch (Throwable e)
        {
            throw new AccessException("Provided arguments '%s' do not match the ctor expected arguments of type '%s'.", e, Arrays
                    .asList(args), type);
        }
    }

    @Override
    public Object get(Object parent) throws AlkemyException
    {
        return valueAccessor.get(parent);
    }

    @Override
    public void set(Object value, Object parent) throws AlkemyException
    {
        valueAccessor.set(value, parent);
    }

    @Override
    public String valueName()
    {
        return valueAccessor.valueName();
    }

    @Override
    public boolean isCollection()
    {
        return collection;
    }
}