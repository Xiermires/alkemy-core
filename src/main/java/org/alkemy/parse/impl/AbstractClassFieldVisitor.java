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

import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM5;

import java.util.LinkedHashMap;
import java.util.Map;

import org.alkemy.parse.impl.FieldAccessorWriter.FieldProperties;
import org.alkemy.util.AlkemyUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public abstract class AbstractClassFieldVisitor extends ClassVisitor
{
    protected final String className;
    protected final Map<String, FieldProperties> fieldMap = new LinkedHashMap<>();
    
    public AbstractClassFieldVisitor(ClassVisitor cv, String className)
    {
        super(ASM5, cv);
        this.className = className;
    }
    
    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
    {
        boolean isStatic = (access & ACC_STATIC) != 0;
        boolean isEnum = AlkemizerUtils.isType(desc) ? AlkemizerUtils.isEnum(desc) : false;
        
        fieldMap.put(name, new FieldProperties(desc, isEnum, isStatic));
        return super.visitField(access, name, desc, signature, value);
    }
    
    // Compile check so changing proxy'ed classes doesn't miss this class.
    // If any changes in the behaviour, change also the instrumentation accordingly.
    public static class Proxy
    {
        public static Object toEnum(Class<?> type, Object value)
        {
            return AlkemyUtils.toEnum(type, value);
        }
    }
}
