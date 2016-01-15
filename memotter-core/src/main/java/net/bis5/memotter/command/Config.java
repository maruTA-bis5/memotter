/*
 * @(#) net.bis5.memotter.command.Config
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
package net.bis5.memotter.command;

import net.bis5.memotter.core.ArgsList;
import net.bis5.memotter.core.Command;
import net.bis5.memotter.core.Memotter;
import net.bis5.memotter.model.MessageObject;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/15
 */
public class Config extends Command<MessageObject> {

    /**
     * @see net.bis5.memotter.core.Command#getCommandPackage()
     */
    @Override
    protected String getCommandPackage() {
        return null;
    }

    /**
     * @see net.bis5.memotter.core.Command#execute(net.bis5.memotter.core.ArgsList)
     */
    @Override
    public MessageObject execute( ArgsList args) {
        String key = args.getFirst();
        String value = args.shift().getFirst();
        if ( value == null) {
            // 参照
            return new MessageObject( Memotter.getSingleton().getStorageItem( key));
        }
        else {
            // 更新
            Memotter.getSingleton().putStorageItem( key, value);
            return new MessageObject( value);
        }
    }

    /**
     * @see net.bis5.memotter.core.Command#getUsage()
     */
    @Override
    protected String getUsage() {
        return String.format( "Usage: Config <config key> # get config%n       Config <config key> <value> # set config");
    }

}
