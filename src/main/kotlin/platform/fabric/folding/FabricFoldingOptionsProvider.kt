package com.demonwav.mcdev.platform.fabric.folding

import com.intellij.application.options.editor.CodeFoldingOptionsProvider
import com.intellij.openapi.options.BeanConfigurable

class FabricFoldingOptionsProvider :
        BeanConfigurable<FabricFoldingSettings.State>(FabricFoldingSettings.instance.state), CodeFoldingOptionsProvider {

    init {
        val settings = FabricFoldingSettings.instance
        checkBox(
                "Fabric: Fold probably GL/GLFW constants",
                { settings.state.enableConstantFolding },
                { b -> settings.state.enableConstantFolding = b }
        )
    }
}
