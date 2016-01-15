/*
 * @(#) net.bis5.memotter.core.Command
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

import net.bis5.memotter.exception.CommandNotFoundException;
import net.bis5.memotter.exception.MemotterException;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/15
 */
public abstract class Command<T> {

    protected abstract String getCommandPackage();

    public <U> U callSubCommand( String subCommandName, ArgsList args) {
        try {
            Class<?> clazz = Class.forName( getCommandPackage() + "." + subCommandName);
            @SuppressWarnings( "unchecked")
            Command<U> command = ( Command<U>) clazz.newInstance();
            return command.execute( args);
        }
        catch ( ClassNotFoundException e) {
            throw new CommandNotFoundException( subCommandName, getUsage());
        }
        catch ( ReflectiveOperationException e) {
            throw new MemotterException( e);
        }
    }

    public abstract T execute( ArgsList args);

    protected String getUsage() {
        return null;
    }
}
