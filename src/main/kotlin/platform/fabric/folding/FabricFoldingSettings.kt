package com.demonwav.mcdev.platform.fabric.folding

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "FabricFoldingSettings", storages = [Storage("minecraft_dev.xml")])
class FabricFoldingSettings : PersistentStateComponent<FabricFoldingSettings.State> {

    data class State(
            var enableConstantFolding: Boolean = true
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        val instance: FabricFoldingSettings
            get() = ServiceManager.getService(FabricFoldingSettings::class.java)
    }
}
