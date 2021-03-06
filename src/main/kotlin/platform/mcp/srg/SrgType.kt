/*
 * Minecraft Dev for IntelliJ
 *
 * https://minecraftdev.org
 *
 * Copyright (c) 2020 minecraft-dev
 *
 * MIT License
 */

package com.demonwav.mcdev.platform.mcp.srg

enum class SrgType(val srgParser: SrgParser) {
    SRG(StandardSrgParser),
    TSRG(TinySrgParser);
}
