/*
 * @(#) net.bis5.memotter.model.MemotterObject
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
package net.bis5.memotter.model;

import twitter4j.JSONObject;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/14
 */
public abstract class MemotterObject extends JSONObject {

    public abstract String getMessage();
}
