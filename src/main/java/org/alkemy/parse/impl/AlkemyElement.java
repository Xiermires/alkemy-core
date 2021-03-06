/*******************************************************************************
 * Copyright (c) 2017, Xavier Miret Andres <xavier.mires@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package org.alkemy.parse.impl;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alkemy.exception.AccessException;
import org.alkemy.exception.AlkemyException;
import org.alkemy.parse.MethodInvoker;
import org.alkemy.parse.NodeFactory;
import org.alkemy.parse.ValueAccessor;
import org.alkemy.util.Assertions;

public class AlkemyElement implements ValueAccessor, NodeFactory
{
    private final AnnotatedMember desc;
    private final ValueAccessor valueAccessor;
    private final NodeFactory nodeFactory;
    private final Map<String, MethodInvoker> methodInvokers;
    private final Class<? extends Annotation> alkemyType;
    private final boolean node;

    // Concrete references to lambdas are much faster than through an interface.
    private final StringReference strref;
    private final DoubleReference dref;
    private final FloatReference fref;
    private final LongReference jref;
    private final IntReference iref;
    private final ShortReference sref;
    private final CharReference cref;
    private final ByteReference bref;
    private final BooleanReference zref;

    AlkemyElement(AnnotatedMember desc, NodeFactory nodeFactory, ValueAccessor valueAccessor, List<MethodInvoker> methodInvokers, Class<? extends Annotation> alkemyType, boolean node)
    {
        this.desc = desc;
        this.valueAccessor = valueAccessor;
        if (valueAccessor instanceof StringReference) strref = (StringReference) valueAccessor;
        else
            strref = null;
        if (valueAccessor instanceof DoubleReference) dref = (DoubleReference) valueAccessor;
        else
            dref = null;
        if (valueAccessor instanceof FloatReference) fref = (FloatReference) valueAccessor;
        else
            fref = null;
        if (valueAccessor instanceof LongReference) jref = (LongReference) valueAccessor;
        else
            jref = null;
        if (valueAccessor instanceof IntReference) iref = (IntReference) valueAccessor;
        else
            iref = null;
        if (valueAccessor instanceof ShortReference) sref = (ShortReference) valueAccessor;
        else
            sref = null;
        if (valueAccessor instanceof CharReference) cref = (CharReference) valueAccessor;
        else
            cref = null;
        if (valueAccessor instanceof ByteReference) bref = (ByteReference) valueAccessor;
        else
            bref = null;
        if (valueAccessor instanceof BooleanReference) zref = (BooleanReference) valueAccessor;
        else
            zref = null;

        this.nodeFactory = nodeFactory;
        this.methodInvokers = new HashMap<String, MethodInvoker>();
        methodInvokers.forEach(c -> this.methodInvokers.put(c.name(), c));
        this.alkemyType = alkemyType;
        this.node = node;
    }

    protected AlkemyElement(AlkemyElement other)
    {
        Assertions.nonNull(other);

        this.desc = other.desc;
        this.valueAccessor = other.valueAccessor;
        this.strref = other.strref;
        this.dref = other.dref;
        this.fref = other.fref;
        this.jref = other.jref;
        this.iref = other.iref;
        this.sref = other.sref;
        this.cref = other.cref;
        this.bref = other.bref;
        this.zref = other.zref;
        this.nodeFactory = other.nodeFactory;
        this.methodInvokers = other.methodInvokers;
        this.alkemyType = other.alkemyType;
        this.node = other.node;
    }

    public AnnotatedMember desc()
    {
        return desc;
    }

    public Class<? extends Annotation> alkemyType()
    {
        return alkemyType;
    }

    @Override
    public Class<?> type() throws AlkemyException
    {
        return valueAccessor.type();
    }

    @Override
    public Object newInstance(Object... args) throws AlkemyException
    {
        if (node) { return nodeFactory.newInstance(args); }
        throw new AlkemyException("Alkemy elements w/o children cannot be instantiated");
    }

    @Override
    public <E> E newInstance(Class<E> type, Object... args) throws AlkemyException
    {
        return nodeFactory.newInstance(type, args);
    }

    @Override
    public Object get(Object parent) throws AccessException
    {
        return valueAccessor.get(parent);
    }

    @Override
    public void set(Object value, Object parent) throws AccessException
    {
        valueAccessor.set(value, parent);
    }
    
    @Override
    public void set(String value, Object parent) throws AlkemyException
    {
        if (strref != null) strref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public String getString(Object parent) throws AlkemyException
    {
        if (strref != null) return strref.getString(parent);
        else
            return valueAccessor.getString(parent);
    }

    @Override
    public Class<?> componentType()
    {
        return nodeFactory.componentType();
    }

    @Override
    public Object newComponentInstance(Object... args) throws AlkemyException
    {
        return nodeFactory.newComponentInstance(args);
    }

    @Override
    public <E> E newComponentInstance(Class<E> type, Object... args) throws AlkemyException
    {
        return nodeFactory.newComponentInstance(type, args);
    }

    public Collection<MethodInvoker> getMethodInvokers()
    {
        return methodInvokers.values();
    }

    public MethodInvoker getMethodInvoker(String name)
    {
        return methodInvokers.get(name);
    }

    @Override
    public String valueName()
    {
        return valueAccessor.valueName();
    }

    public boolean isNode()
    {
        return node;
    }

    @Override
    public String toString()
    {
        return valueAccessor.valueName();
    }

    @Override
    public boolean isCollection()
    {
        return nodeFactory.isCollection();
    }

    @Override
    @SafeVarargs
    public final <E> void addAll(Object parent, E first, E... others) throws AlkemyException
    {
        nodeFactory.addAll(parent, first, others);
    }

    @Override
    public final <E> void addAll(Object parent, List<E> others) throws AlkemyException
    {
        nodeFactory.addAll(parent, others);
    }

    @Override
    public void set(double value, Object parent) throws AlkemyException
    {
        if (dref != null) dref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public double getDouble(Object parent) throws AlkemyException
    {
        if (dref != null) return dref.getDouble(parent);
        else
            return valueAccessor.getDouble(parent);
    }

    @Override
    public void set(float value, Object parent) throws AlkemyException
    {
        if (fref != null) fref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public float getFloat(Object parent) throws AlkemyException
    {
        if (fref != null) return fref.getFloat(parent);
        else
            return valueAccessor.getFloat(parent);
    }

    @Override
    public void set(long value, Object parent) throws AlkemyException
    {
        if (jref != null) jref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public long getLong(Object parent) throws AlkemyException
    {
        if (jref != null) return jref.getLong(parent);
        else
            return valueAccessor.getLong(parent);
    }

    @Override
    public void set(int value, Object parent) throws AlkemyException
    {
        if (iref != null) iref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public int getInt(Object parent) throws AlkemyException
    {
        if (iref != null) return iref.getInt(parent);
        else
            return valueAccessor.getInt(parent);
    }

    @Override
    public void set(short value, Object parent) throws AlkemyException
    {
        if (sref != null) sref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public short getShort(Object parent) throws AlkemyException
    {
        if (sref != null) return sref.getShort(parent);
        else
            return valueAccessor.getShort(parent);
    }

    @Override
    public void set(char value, Object parent) throws AlkemyException
    {
        if (cref != null) cref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public char getChar(Object parent) throws AlkemyException
    {
        if (cref != null) return cref.getChar(parent);
        else
            return valueAccessor.getChar(parent);
    }

    @Override
    public void set(byte value, Object parent) throws AlkemyException
    {
        if (bref != null) bref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public byte getByte(Object parent) throws AlkemyException
    {
        if (bref != null) return bref.getByte(parent);
        else
            return valueAccessor.getByte(parent);
    }

    @Override
    public void set(boolean value, Object parent) throws AlkemyException
    {
        if (zref != null) zref.set(value, parent);
        else
            valueAccessor.set(value, parent);
    }

    @Override
    public boolean getBoolean(Object parent) throws AlkemyException
    {
        if (zref != null) return zref.getBoolean(parent);
        else
            return valueAccessor.getBoolean(parent);
    }
}
