/*
 * @(#) net.bis5.memotter.subcommand.SubCommand
 * Copyright (c) 2016 bBreak Systems Co, Ltd. All Rights Reserved.
 *
 * THE SOFTWARE IS PROVIDED BY bBreak Systems Co, Ltd.,
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package net.bis5.memotter.subcommand;

import net.bis5.memotter.model.ErrorObject;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/14
 */
public interface SubCommand<T> {

    T execute( String... args);

    ErrorObject generateHelp();
}
