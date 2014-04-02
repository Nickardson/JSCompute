package com.nickardson.jscomputing.javascript.methods;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.Scriptable;

public abstract class JavaScriptFunction extends NativeFunction {
    @Override
    protected int getLanguageVersion() {
        return Context.VERSION_1_8;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (getSide() != null && FMLCommonHandler.instance().getEffectiveSide() != getSide()) {
            // TODO: Determine if error should be thrown
            return null;
        }
        return invoke(cx, scope, thisObj, args);
    }

    /**
     * Called when this function is invoked.
     * @param cx
     * The main Context the script is running in.
     * @param scope
     * The scope this function is called in.
     * @param thisObj
     * "this" object, for example, (x) in print.call(x, y)
     * @param args
     * @return
     * Return value of the function
     */
    public abstract Object invoke(Context cx, Scriptable scope, Scriptable thisObj, Object[] args);

    /**
     * Gets the side this function should have effect on.
     * @return
     * The side to run on, or null for both.
     */
    public Side getSide() {
        return null;
    }

    @Override
    protected int getParamCount() {
        return 0;
    }

    @Override
    protected int getParamAndVarCount() {
        return 0;
    }

    @Override
    protected String getParamOrVarName(int index) {
        return null;
    }
}
