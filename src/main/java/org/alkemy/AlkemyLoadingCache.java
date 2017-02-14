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
package org.alkemy;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.agenttools.AgentTools;
import org.alkemy.exception.AlkemyException;
import org.alkemy.parse.AlkemyParser;
import org.alkemy.util.Node;
import org.alkemy.visitor.AlkemyElementVisitor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

class AlkemyLoadingCache
{
    // TODO: Allow external configuration
    private static final int MAXIMUM_BYTE_SIZE = 2 * 1024 * 1024;

    private final AlkemyParser<? extends AlkemyElement<?>> parser;

    // Keep a cached version of created AlkemyTypeVisitor to enhance performance.
    private final LoadingCache<Class<?>, Node<? extends AlkemyElement<?>>> cache;

    private final Map<Class<? extends AlkemyElementVisitor<?>>, AlkemyElementVisitor<?>> mappers;

    AlkemyLoadingCache(AlkemyParser<? extends AlkemyElement<?>> parser, Map<Class<? extends AlkemyElementVisitor<?>>, AlkemyElementVisitor<?>> mappers)
    {
        this.parser = parser;
        this.mappers = mappers;

        cache = CacheBuilder.newBuilder().maximumWeight(MAXIMUM_BYTE_SIZE).weigher((k, v) -> Number.class.cast(AgentTools.getObjectSize(k)).intValue())
                .expireAfterAccess(15, TimeUnit.MINUTES).build(new CacheLoader<Class<?>, Node<? extends AlkemyElement<?>>>()
                {
                    @Override
                    public Node<? extends AlkemyElement<?>> load(Class<?> key) throws AlkemyException
                    {
                        return _create(key);
                    }
                });
    }

    private final Node<? extends AlkemyElement<?>> _create(Class<?> type)
    {
        return typify(parser.parse(type));
    }

    private Node<? extends AlkemyElement<?>> typify(Node<? extends AlkemyElement<?>> root)
    {
        if (mappers.isEmpty())
        {
            return root;
        }
        else
        {
            /*return Node.copy(root, Nodes.arborescence(root.data()), f ->
            {
                final Function<AlkemyElement<?>, ? extends AlkemyElement<?>> mapper = mappers.get(f.visitorType());
                return mapper == null ? f : mapper.apply(f);
            }).build();*/
            return null;
        }
    }

    Node<? extends AlkemyElement<?>> get(Class<?> type)
    {
        try
        {
            return cache.get(type);
        }
        catch (ExecutionException e)
        {
            throw new AlkemyException("Cache error.", e); // TODO
        }
    }
}
