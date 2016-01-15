/*
 * @(#) net.bis5.memotter.core.ArgsList
 * Copyright (c) 2016 T.Maruyama
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 */
package net.bis5.memotter.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/15
 */
public class ArgsList extends ArrayList<String> {

    /** $Comment$ */
    private static final long serialVersionUID = 1L;

    public ArgsList() {
        super();
    }

    public ArgsList( Collection<String> collection) {
        super( collection);
    }

    public ArgsList shift() {
        return subList( 1, size());
    }

    public String getFirst() {
        return stream().findFirst().orElse( null);
    }

    /**
     * @see java.util.ArrayList#subList(int, int)
     */
    @Override
    public ArgsList subList( int fromIndex, int toIndex) {
        return new ArgsList( super.subList( fromIndex, toIndex));
    }

}
