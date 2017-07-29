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

import org.alkemy.exception.AccessException;
import org.alkemy.exception.AlkemyException;

public interface NodeConstructor
{
    /**
     * Returns the class type.
     * 
     * @throws AccessException
     *             If an error occurs while recovering the class type.
     */
    Class<?> type() throws AlkemyException;
    
    /**
     * If the type is a collection or an array it returns the type of the element's contained. It returns null otherwise.
     * 
     * @throws AccessException
     *             If an error occurs while recovering the class type.
     */
    Class<?> componentType() throws AlkemyException;
    
    /**
     * Returns a new instance of the class.
     * 
     * @throws AlkemyException
     *             If an error occurs while creating the class instance.
     */
    Object newInstance(Object... args) throws AlkemyException;
    
    /**
     * Returns a new instance of the component class, or null if not a component type.
     * 
     * @throws AlkemyException
     *             If an error occurs while creating the class instance.
     */
    Object newComponentInstance(Object... args) throws AlkemyException;
    
    /**
     * Returns a new instance of the class if is exactly of type T (not assignable!), null otherwise.
     * 
     * @throws AlkemyException
     *             If an error occurs while creating the class instance.
     */
    public <T> T safeNewInstance(Class<T> type, Object... args) throws AlkemyException;
    
    /**
     * Returns a new instance of the component class if is exactly of type T (not assignable!), null otherwise.
     * 
     * @throws AlkemyException
     *             If an error occurs while creating the class instance.
     */
    public <T> T safeNewComponentInstance(Class<T> type, Object... args) throws AlkemyException;
}
