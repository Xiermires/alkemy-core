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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.alkemy.exception.AccessException;
import org.alkemy.exception.AlkemyException;

public class ReflectionBasedConstructorAccessor implements NodeConstructor
{
    private final Constructor<?> typeCtor;
    private final Constructor<?> componentTypeCtor;

    ReflectionBasedConstructorAccessor(Class<?> type, Class<?> componentType) throws NoSuchMethodException, SecurityException
    {
        this.typeCtor = type.getDeclaredConstructor();
        this.componentTypeCtor = componentType.getDeclaredConstructor();

        typeCtor.setAccessible(true);
        componentTypeCtor.setAccessible(true);
    }

    @Override
    public Object newInstance(Object... args) throws AlkemyException
    {
        return newInstance(typeCtor, args);
    }

    @Override
    public Class<?> type() throws AlkemyException
    {
        return typeCtor.getDeclaringClass();
    }

    @Override
    public Class<?> componentType() throws AlkemyException
    {
        return componentType();
    }

    @Override
    public Object newComponentInstance(Object... args) throws AlkemyException
    {
        return newInstance(componentTypeCtor, args);
    }

    private Object newInstance(Constructor<?> ctor, Object... args)
    {
        if (args.length > 0) { throw new AccessException(
                "Reflection based constructor of type '%s' expect no parameters, but received '%s'.", type(), Arrays.asList(args)); }
        try
        {
            return ctor.newInstance();
        }
        catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new AccessException("Couldn't create instance for constructor '%s' with arguments '%s'", e, ctor.getName(),
                    Arrays.asList(args));
        }
    }
}
